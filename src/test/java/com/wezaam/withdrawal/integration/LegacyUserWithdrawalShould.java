package com.wezaam.withdrawal.integration;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.integration.config.H2JpaConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LegacyUserWithdrawalShould {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Ignore
    @ParameterizedTest
    @CsvSource(value = {"1;1;50.0;ASAP;PENDING"}, delimiter = ';')
    public void acceptASAPWithdrawWhenUserHasPaymentMethodAndEnoughAmount(String expectedUserId,
                                                                          String expectedPaymentId,
                                                                          String expectedAmount,
                                                                          String expectedExecuteAt,
                                                                          String expectedStatus) throws Exception {
        postWithdrawAndVerifyTheResponse(
                expectedUserId,
                expectedPaymentId,
                expectedAmount,
                expectedExecuteAt,
                expectedStatus);

        verifyWithdrawWasMadeCorreclty(expectedAmount);

    }

    private void postWithdrawAndVerifyTheResponse(String expectedUserId,
                                                  String expectedPaymentId,
                                                  String expectedAmount,
                                                  String expectedExecuteAt,
                                                  String expectedStatus) throws JSONException {

        final URI createWithdrawalsEndpoint = UriComponentsBuilder
                .fromHttpUrl(
                        getEndpointUrlFor("create-withdrawals")
                )
                .queryParam("userId", expectedUserId)
                .queryParam("paymentMethodId", expectedPaymentId)
                .queryParam("amount", expectedAmount)
                .queryParam("executeAt", expectedExecuteAt)
                .build()
                .encode()
                .toUri();

        final HttpEntity<String> createWithdrawResponse = restTemplate
                .exchange(createWithdrawalsEndpoint,
                        HttpMethod.POST,
                        null,
                        String.class);

        final JSONObject createWithdrawResponseBody = new JSONObject(createWithdrawResponse.getBody());

        assertEquals(expectedUserId, createWithdrawResponseBody.getString("userId"));
        assertEquals(expectedPaymentId, createWithdrawResponseBody.getString("paymentMethodId"));
        assertEquals(expectedAmount, createWithdrawResponseBody.getString("amount"));
        assertEquals(expectedStatus, createWithdrawResponseBody.getString("status"));
    }

    private void verifyWithdrawWasMadeCorreclty(String expectedAmount) throws JSONException {
        final HttpEntity<String> findAllWithdrawsResponse = restTemplate
                .getForEntity(
                        getEndpointUrlFor("find-all-withdrawals"),
                        String.class);

        final JSONArray findAllWithdrawsResponseBody = new JSONArray(findAllWithdrawsResponse.getBody());
        assertEquals(1, findAllWithdrawsResponseBody.length());
        assertEquals(expectedAmount, findAllWithdrawsResponseBody.getJSONObject(0).getString("amount"));
    }

    private String getEndpointUrlFor(String endpoint) {
        final String baseendpointurl = "http://localhost:%d/%s";
        return String.format(baseendpointurl, port, endpoint);
    }
}
