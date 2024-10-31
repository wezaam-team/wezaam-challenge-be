package com.wezaam.withdrawal.infrastructure.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MvcResult;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import com.wezaam.withdrawal.application.UserService;
import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.entity.User;
import com.wezaam.withdrawal.domain.exception.NotFoundException;
import com.wezaam.withdrawal.infrastructure.rest.adapter.UserController;
import com.wezaam.withdrawal.infrastructure.rest.dto.response.UserResponse;
import com.wezaam.withdrawal.infrastructure.rest.handler.ProblemDetails;
import com.wezaam.withdrawal.infrastructure.rest.mapper.UserMapper;

@WebMvcTest(UserController.class)
@Import({UserMapper.class})
public class UserControllerTest extends ExtendedControllerTest {

    @MockBean UserService userService;

    @Test
    void shouldGetAllUsersEmpty() throws Exception {

        // arrange
        when(userService.getAllUsers()).thenReturn(List.of());

        // act
        MvcResult resultActions = request(get("/users")).andExpect(status().isOk()).andReturn();
        List<UserResponse> userResponses =
                mapper.readValue(
                        resultActions.getResponse().getContentAsString(), new TypeReference<>() {});

        // assert
        assertThat(userResponses).hasSize(0);
    }

    @Test
    void shouldGetAllUsers() throws Exception {

        // arrange
        when(userService.getAllUsers())
                .thenReturn(
                        List.of(
                                User.builder()
                                        .id(1L)
                                        .firstName("Charles")
                                        .maxWithdrawalAmount(500.00D)
                                        .paymentMethods(
                                                List.of(
                                                        PaymentMethod.builder()
                                                                .id(1L)
                                                                .name("Bank account")
                                                                .build()))
                                        .build()));

        // act
        MvcResult resultActions = request(get("/users")).andExpect(status().isOk()).andReturn();
        List<UserResponse> userResponses =
                mapper.readValue(
                        resultActions.getResponse().getContentAsString(), new TypeReference<>() {});

        // assert
        assertThat(userResponses).hasSize(1);
        UserResponse userResponse = userResponses.getFirst();
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getPaymentMethods()).hasSize(1);
        assertThat(userResponse.getPaymentMethods().getFirst()).isNotNull();
        assertThat(userResponse.getPaymentMethods().getFirst().getId()).isNotNull();
        assertThat(userResponse.getPaymentMethods().getFirst().getName()).isNotNull();
    }

    @Test
    void shouldGetUserByIdNotFound() throws Exception {

        // arrange
        when(userService.getUserById(anyLong())).thenThrow(new NotFoundException("User not found"));

        // act
        MvcResult resultActions =
                request(get("/users/1")).andExpect(status().isNotFound()).andReturn();

        ProblemDetails problemDetails =
                mapper.readValue(
                        resultActions.getResponse().getContentAsString(), ProblemDetails.class);
        // assert
        assertThat(problemDetails).isNotNull();
        assertThat(problemDetails.getCode()).isNotBlank();
        assertThat(problemDetails.getMessage()).isNotBlank();
    }

    @Test
    void shouldGetUserById() throws Exception {

        // arrange
        when(userService.getUserById(anyLong()))
                .thenReturn(
                        User.builder()
                                .id(1L)
                                .firstName("Charles")
                                .maxWithdrawalAmount(500.00D)
                                .paymentMethods(
                                        List.of(
                                                PaymentMethod.builder()
                                                        .id(1L)
                                                        .name("Bank account")
                                                        .build()))
                                .build());

        // act
        MvcResult resultActions = request(get("/users/1")).andExpect(status().isOk()).andReturn();

        UserResponse userResponse =
                mapper.readValue(
                        resultActions.getResponse().getContentAsString(), UserResponse.class);
        // assert
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getPaymentMethods()).isNotNull().hasSize(1);
    }
}
