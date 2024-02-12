package com.wezaam.withdrawal.acceptance;

import com.wezaam.withdrawal.Application;
import com.wezaam.withdrawal.acceptance.dto.User;
import com.wezaam.withdrawal.acceptance.dto.Users;
import com.wezaam.withdrawal.acceptance.dto.converter.JSONConverter;
import com.wezaam.withdrawal.acceptance.dto.converter.UsersConverter;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        List<User> users = new UsersConverter().to(getUsersResponse)
                .getUsers();

        assertEquals(3, users.size());
        assertEquals("User 1", users.get(0).getName());
        assertEquals("User 2", users.get(1).getName());
        assertEquals("User 3", users.get(2).getName());
    }
}
