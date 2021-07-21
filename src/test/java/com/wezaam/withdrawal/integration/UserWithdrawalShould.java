package com.wezaam.withdrawal.integration;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.integration.config.H2JpaConfig;
import com.wezaam.withdrawal.integration.dto.User;
import com.wezaam.withdrawal.integration.dto.Withdrawal;
import com.wezaam.withdrawal.integration.dto.builder.PaymentMethodBuilder;
import com.wezaam.withdrawal.integration.dto.builder.UserBuilder;
import com.wezaam.withdrawal.integration.dto.builder.WithdrawalBuilder;
import com.wezaam.withdrawal.integration.dto.converter.WithdrawalConverter;
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

@SpringBootTest(classes = {Application.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserWithdrawalShould {

    public static final String EMPTY_JSON_BODY = "{}";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void executeWithdrawalWithOnePaymentMethod() throws URISyntaxException {

        final User user = givenUserWithOnePaymentMethod();
        final JSONObject createdWithdrawal = whenUserExecutesWithdrawalASAP(user);

        thenWithdrawalIsStoredAndSentToProvider(createdWithdrawal);
    }

    private User givenUserWithOnePaymentMethod() {
        return UserBuilder.aUserBuilder()
                .withId(1L)
                .withName("John")
                .withPaymentMethod(
                        PaymentMethodBuilder.aPaymentMethodBuilder()
                                .withId(1L)
                                .withName("My payment")
                                .build()
                )
                .build();
    }

    private JSONObject whenUserExecutesWithdrawalASAP(User user) throws URISyntaxException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<String> createWithdrawalRequst = new HttpEntity(
                new WithdrawalConverter().from(
                        WithdrawalBuilder.aWithdrawalBuilder()
                                .withUser(user)
                                .withAmmount(new BigDecimal(50))
                                .build()
                ).toString(),
                headers
        );

        final HttpEntity<JSONObject> createWithdrawResponse = restTemplate
                .postForEntity(
                        new URI(
                                getEndpointUrlFor("withdrawals")
                        ),
                        createWithdrawalRequst,
                        JSONObject.class
                );

        assertNotNull(createWithdrawResponse);
        assertNotNull(createWithdrawResponse.getBody());
        assertFalse(createWithdrawResponse.getBody().toString().isEmpty());
        assertNotEquals(EMPTY_JSON_BODY, createWithdrawResponse.getBody().toString());
        return createWithdrawResponse.getBody();
    }

    private String getEndpointUrlFor(String endpoint) {
        final String baseEndpointUrl = "http://localhost:%d/%s";
        return String.format(baseEndpointUrl, port, endpoint);
    }

    private void thenWithdrawalIsStoredAndSentToProvider(JSONObject withdrawalAsJson) {
        Withdrawal withdrawal = new WithdrawalConverter().to(withdrawalAsJson);
        System.out.println(withdrawal);
    }
}
