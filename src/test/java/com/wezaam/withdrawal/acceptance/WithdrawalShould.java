package com.wezaam.withdrawal.acceptance;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.config.infrastructure.H2JpaConfig;
import com.wezaam.withdrawal.acceptance.dto.builder.WithdrawalBuilder;
import com.wezaam.withdrawal.acceptance.dto.converter.WithdrawalConverter;
import com.wezaam.withdrawal.domain.WithdrawalStatus;
import com.wezaam.withdrawal.infrastructure.config.RabbitMQConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class, RabbitMQConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "test.rabbitMQConfig.enabled=true"}
)
public class WithdrawalShould {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void acceptsASAPWithdrawGivenUserWithOnePaymentMethod() throws URISyntaxException, JSONException {
        final JSONObject createdWithdrawal = whenUserExecutesWithdrawalASAP();
        thenWithdrawalIsStoredAndSentToProvider(createdWithdrawal);
        andWithdrawalIsSuccessfullyProcessed((Integer) createdWithdrawal.get("id"));
    }

    private JSONObject whenUserExecutesWithdrawalASAP() throws URISyntaxException, JSONException {
        final long userId = 1L;
        final long paymentMethodId = 1L;
        final BigDecimal withdrawalAmount = BigDecimal.valueOf(50, 2);

        final JSONObject withdrawalRequestBody = new WithdrawalConverter().from(
                WithdrawalBuilder.aWithdrawalBuilder()
                        .withUserId(userId)
                        .withPaymentMethodId(paymentMethodId)
                        .withAmount(withdrawalAmount)
                        .withImmediate(Boolean.TRUE)
                        .build()
        );

        final HttpEntity<String> createWithdrawalResponse = restTemplate
                .postForEntity(
                        getEndpointURIFor("withdrawals"),
                        getRequestFor(withdrawalRequestBody),
                        String.class
                );

        assertNotNull(createWithdrawalResponse);
        assertTrue(createWithdrawalResponse.hasBody());

        final JSONObject withdrawalResponseBody =
                new JSONObject(createWithdrawalResponse.getBody());

        assertWithdrawals(withdrawalRequestBody, withdrawalResponseBody);

        return withdrawalResponseBody;
    }

    private HttpEntity<String> getRequestFor(JSONObject requestBody) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity(
                requestBody.toString(),
                headers);
    }

    private void thenWithdrawalIsStoredAndSentToProvider(JSONObject expectedWithdrawal)
            throws URISyntaxException, JSONException {

        final HttpEntity<String> getWithdrawalResponse = restTemplate
                .getForEntity(
                        getEndpointURIFor(
                                String.format("withdrawals/%d", expectedWithdrawal.get("id"))),
                        String.class
                );

        assertNotNull(getWithdrawalResponse);
        assertTrue(getWithdrawalResponse.hasBody());

        final JSONObject getWithdrawalResponseBody =
                new JSONObject(getWithdrawalResponse.getBody());

        assertWithdrawals(expectedWithdrawal, getWithdrawalResponseBody);
    }

    private void andWithdrawalIsSuccessfullyProcessed(Integer withdrawalId)
            throws JSONException, URISyntaxException {

        giveTimeForTheWithdrawalToBeProcessed();

        final HttpEntity<String> getWithdrawalResponse = restTemplate
                .getForEntity(
                        getEndpointURIFor(
                                String.format("withdrawals/%d", withdrawalId)),
                        String.class
                );
        assertNotNull(getWithdrawalResponse);
        assertTrue(getWithdrawalResponse.hasBody());

        final JSONObject getWithdrawalResponseBody =
                new JSONObject(getWithdrawalResponse.getBody());

        assertEquals(
                WithdrawalStatus.SUCCESS.toString(),
                getWithdrawalResponseBody.get("withdrawalStatus"));
    }

    private void giveTimeForTheWithdrawalToBeProcessed() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
    }

    private URI getEndpointURIFor(String endpointUrl) throws URISyntaxException {
        return new URI(getEndpointUrlFor(endpointUrl));
    }

    private String getEndpointUrlFor(String endpoint) {
        final String baseEndpointUrl = "http://localhost:%d/%s";
        return String.format(baseEndpointUrl, port, endpoint);
    }

    private static void assertWithdrawals(JSONObject expected, JSONObject actual) {
        assertWithdrawalsProperty(expected, actual, "userId");
        assertWithdrawalsProperty(expected, actual, "paymentMethodId");
        assertWithdrawalsProperty(expected, actual, "amount");
    }

    private static void assertWithdrawalsProperty(JSONObject expected, JSONObject actual, String property) {
        try {
            assertEquals(
                    expected.get(property),
                    actual.get(property),
                    String.format(
                            "Property [%s] is not the same. Expected: [%s]. Actual: [%s].",
                            property,
                            expected.get(property),
                            actual.get(property)
                    )
            );
        } catch (Exception e) {
            fail(String.format("Property %s is not the same on both objects.", property), e);
        }
    }
}
