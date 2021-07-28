package com.wezaam.withdrawal.acceptance;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.config.infrastructure.H2JpaConfig;
import com.wezaam.withdrawal.infrastructure.config.RabbitMQConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {Application.class, H2JpaConfig.class, RabbitMQConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserListShould {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void displayAListOfAllExitentUsers() throws URISyntaxException, JSONException {
        JSONObject getUsersResponse = whenGetUsersIsExecuted();
        thenAllUsersAreDsiplayed(getUsersResponse);
    }

    private JSONObject whenGetUsersIsExecuted() throws URISyntaxException, JSONException {
        final HttpEntity<String> listUsersResponse = restTemplate
                .getForEntity(
                        EndpointResolver.
                                aEndpointResolverFor(port)
                                .getEndpointURIFor("users"),
                        String.class
                );

        assertNotNull(listUsersResponse);
        assertTrue(listUsersResponse.hasBody());

        return new JSONObject(listUsersResponse.getBody());
    }

    private void thenAllUsersAreDsiplayed(JSONObject getUsersResponse) {
    }
}
