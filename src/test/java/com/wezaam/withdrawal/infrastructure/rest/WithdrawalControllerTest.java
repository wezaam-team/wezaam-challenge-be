package com.wezaam.withdrawal.infrastructure.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MvcResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.fasterxml.jackson.core.type.TypeReference;

import com.wezaam.withdrawal.application.WithdrawalService;
import com.wezaam.withdrawal.domain.command.CreateWithdrawalCommand;
import com.wezaam.withdrawal.domain.entity.Withdrawal;
import com.wezaam.withdrawal.domain.entity.WithdrawalScheduled;
import com.wezaam.withdrawal.domain.exception.NotFoundException;
import com.wezaam.withdrawal.domain.exception.WithdrawalDomainException;
import com.wezaam.withdrawal.domain.valueobject.WithdrawalStatus;
import com.wezaam.withdrawal.infrastructure.rest.adapter.WithdrawalController;
import com.wezaam.withdrawal.infrastructure.rest.dto.create.CreateWithdrawalRequest;
import com.wezaam.withdrawal.infrastructure.rest.dto.response.WithdrawalResponse;
import com.wezaam.withdrawal.infrastructure.rest.handler.ProblemDetails;
import com.wezaam.withdrawal.infrastructure.rest.mapper.WithdrawalMapper;

@WebMvcTest(WithdrawalController.class)
@Import({WithdrawalMapper.class})
public class WithdrawalControllerTest extends WithdrawalFakeData {

    @MockBean WithdrawalService withdrawalService;

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalUserNotFound(int source) throws Exception {

        // arrange
        when(withdrawalService.create(any(CreateWithdrawalCommand.class)))
                .thenThrow(new NotFoundException("User was not found"));

        CreateWithdrawalRequest request =
                source == 1 ? getWithdrawalRequestAsap() : getWithdrawalRequestScheduled();

        final String body = mapper.writeValueAsString(request);

        // act
        MvcResult mvcResult =
                requestBody(post("/withdrawals"), body)
                        .andExpect(status().isNotFound())
                        .andReturn();

        ProblemDetails problemDetails =
                mapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ProblemDetails.class);

        // assert
        assertThat(problemDetails).isNotNull();
        assertThat(problemDetails.getCode()).isNotNull();
        assertThat(problemDetails.getMessage()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalPaymentMethodNotFound(int source) throws Exception {

        // arrange
        when(withdrawalService.create(any(CreateWithdrawalCommand.class)))
                .thenThrow(new NotFoundException("Payment method was not found"));

        CreateWithdrawalRequest request =
                source == 1 ? getWithdrawalRequestAsap() : getWithdrawalRequestScheduled();

        final String body = mapper.writeValueAsString(request);

        // act
        MvcResult mvcResult =
                requestBody(post("/withdrawals"), body)
                        .andExpect(status().isNotFound())
                        .andReturn();

        ProblemDetails problemDetails =
                mapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ProblemDetails.class);

        // assert
        assertThat(problemDetails).isNotNull();
        assertThat(problemDetails.getCode()).isNotNull();
        assertThat(problemDetails.getMessage()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalMissingFields(int source) throws Exception {

        // arrange
        when(withdrawalService.create(any(CreateWithdrawalCommand.class)))
                .thenThrow(new NotFoundException("Payment method was not found"));

        CreateWithdrawalRequest request = new CreateWithdrawalRequest();
        final String body = mapper.writeValueAsString(request);

        // act
        MvcResult mvcResult =
                requestBody(post("/withdrawals"), body)
                        .andExpect(status().isBadRequest())
                        .andReturn();

        ProblemDetails problemDetails =
                mapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ProblemDetails.class);

        // assert
        assertThat(problemDetails).isNotNull();
        assertThat(problemDetails.getCode()).isNotNull();
        assertThat(problemDetails.getMessage()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawalWithdrawalDomainException(int source) throws Exception {

        // arrange
        double amount = 450.90D;
        when(withdrawalService.create(any(CreateWithdrawalCommand.class)))
                .thenThrow(
                        new WithdrawalDomainException(
                                "Withdrawal amount exceed the maximum amount allowed " + amount));

        CreateWithdrawalRequest request =
                source == 1
                        ? CreateWithdrawalRequest.builder()
                                .amount(BigDecimal.valueOf(100, 0))
                                .executeAt("ASAP")
                                .paymentMethodId(1L)
                                .userId(1L)
                                .build()
                        : CreateWithdrawalRequest.builder()
                                .amount(BigDecimal.valueOf(100, 0))
                                .executeAt("2024-11-30T18:35:24.00Z")
                                .paymentMethodId(1L)
                                .userId(1L)
                                .build();

        final String body = mapper.writeValueAsString(request);

        // act
        MvcResult mvcResult =
                requestBody(post("/withdrawals"), body)
                        .andExpect(status().isBadRequest())
                        .andReturn();

        ProblemDetails problemDetails =
                mapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ProblemDetails.class);

        // assert
        assertThat(problemDetails).isNotNull();
        assertThat(problemDetails.getCode()).isNotNull();
        assertThat(problemDetails.getMessage())
                .isNotNull()
                .isEqualTo("Withdrawal amount exceed the maximum amount allowed 450.9");
    }

    @ParameterizedTest
    @CsvSource({"1", "2"})
    void shouldCreateWithdrawal(int source) throws Exception {

        // arrange
        CreateWithdrawalRequest request =
                source == 1 ? getWithdrawalRequestAsap() : getWithdrawalRequestScheduled();
        when(withdrawalService.create(any(CreateWithdrawalCommand.class)))
                .thenReturn(
                        Withdrawal.builder()
                                .id((long) source)
                                .userId(request.getUserId())
                                .paymentMethodId(request.getPaymentMethodId())
                                .status(WithdrawalStatus.PENDING)
                                .build());

        final String body = mapper.writeValueAsString(request);

        // act
        MvcResult mvcResult =
                requestBody(post("/withdrawals"), body).andExpect(status().isCreated()).andReturn();

        WithdrawalResponse withdrawalResponse =
                mapper.readValue(
                        mvcResult.getResponse().getContentAsString(), WithdrawalResponse.class);

        // assert
        assertThat(withdrawalResponse).isNotNull();
        assertThat(withdrawalResponse.getId()).isNotNull();
    }

    @Test
    void shouldGetAllWithdrawalsEmpty() throws Exception {

        // arrange
        when(withdrawalService.getAllWithdrawal()).thenReturn(List.of());
        // act
        MvcResult mvcResult = request(get("/withdrawals")).andExpect(status().isOk()).andReturn();

        List<WithdrawalResponse> withdrawalResponses =
                mapper.readValue(
                        mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        // assert
        assertThat(withdrawalResponses).isEmpty();
    }

    @Test
    void shouldGetAllWithdrawals() throws Exception {

        // arrange
        when(withdrawalService.getAllWithdrawal())
                .thenReturn(
                        List.of(
                                Withdrawal.builder()
                                        .id(1L)
                                        .amount(1_000D)
                                        .userId(1L)
                                        .paymentMethodId(1L)
                                        .build(),
                                WithdrawalScheduled.builder()
                                        .id(2L)
                                        .amount(5_000D)
                                        .userId(2L)
                                        .paymentMethodId(2L)
                                        .build()));
        // act
        MvcResult mvcResult = request(get("/withdrawals")).andExpect(status().isOk()).andReturn();

        List<WithdrawalResponse> withdrawalResponses =
                mapper.readValue(
                        mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        // assert
        assertThat(withdrawalResponses).hasSize(2);
        WithdrawalResponse withdrawalResponse = withdrawalResponses.getFirst();

        assertThat(withdrawalResponse).isNotNull();
        assertThat(withdrawalResponse.getId()).isNotNull();
        assertThat(withdrawalResponse.getPaymentMethodId()).isNotNull();
        assertThat(withdrawalResponse.getUserId()).isNotNull();
    }
}
