package com.wezaam.withdrawal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezaam.withdrawal.domain.model.WithdrawalStatus;
import com.wezaam.withdrawal.resource.CreateWithdrawalRequest;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIT {

    @Autowired
    private ObjectMapper objectMapper;

    private CreateWithdrawalRequest createImmediateWithdrawalRequest;
    private CreateWithdrawalRequest createScheduledWithdrawalRequest;

    private static final long IMMEDIATE_WITHDRAWAL_USER_ID = 1L;
    private static final long IMMEDIATE_WITHDRAWAL_PAYMENT_METHOD_ID = 2L;
    private static final double IMMEDIATE_WITHDRAWAL_AMOUNT = 9.99d;

    private static final long SCHEDULED_WITHDRAWAL_USER_ID = 2L;
    private static final long SCHEDULED_WITHDRAWAL_PAYMENT_METHOD_ID = 3L;
    private static final double SCHEDULED_WITHDRAWAL_AMOUNT = 20.05d;
    private static final String SCHEDULED_WITHDRAWAL_EXECUTE_AT = "2022-03-01T00:00:00Z";

    private static final String WITHDRAWAL_RESOURCE_URL = "/withdrawals";

    @BeforeEach
    void setUp() {
        this.createImmediateWithdrawalRequest = new CreateWithdrawalRequest(
                IMMEDIATE_WITHDRAWAL_USER_ID,
                IMMEDIATE_WITHDRAWAL_PAYMENT_METHOD_ID,
                IMMEDIATE_WITHDRAWAL_AMOUNT,
                "ASAP");

        this.createScheduledWithdrawalRequest = new CreateWithdrawalRequest(
                SCHEDULED_WITHDRAWAL_USER_ID,
                SCHEDULED_WITHDRAWAL_PAYMENT_METHOD_ID,
                SCHEDULED_WITHDRAWAL_AMOUNT,
                SCHEDULED_WITHDRAWAL_EXECUTE_AT);
    }

    @Test
    void whenAnImmediateWithdrawalIsCreatedThenItIsCreatedAndImmediatelyProcessed(@Autowired MockMvc mvc)
            throws Exception {
        // When
        var instantBeforeCreation = Instant.now().toString();

        var resultActions = mvc.perform(
                post(WITHDRAWAL_RESOURCE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.createImmediateWithdrawalRequest)));

        var instantAfterCreation = Instant.now().toString();

        // Then
        resultActions
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.userId").value(IMMEDIATE_WITHDRAWAL_USER_ID),
                        jsonPath("$.paymentMethodId").value(IMMEDIATE_WITHDRAWAL_PAYMENT_METHOD_ID),
                        jsonPath("$.amount").value(IMMEDIATE_WITHDRAWAL_AMOUNT),
                        jsonPath("$.createdAt")
                                .value(allOf(greaterThan(instantBeforeCreation), lessThan(instantAfterCreation))),
                        jsonPath("$.executeAt").doesNotExist(),
                        jsonPath("$.transactionId").isNumber(),
                        jsonPath("$.status").value(WithdrawalStatus.PROCESSING.toString()));
    }

    @Test
    void whenAScheduledWithdrawalIsCreatedThenItIsCreatedAndScheduledForProcessingLaterOn(@Autowired MockMvc mvc)
            throws Exception {
        // When
        var instantBeforeCreation = Instant.now().toString();

        var resultActions = mvc.perform(
                post(WITHDRAWAL_RESOURCE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.createScheduledWithdrawalRequest)));

        var instantAfterCreation = Instant.now().toString();

        // Then
        resultActions
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.userId").value(SCHEDULED_WITHDRAWAL_USER_ID),
                        jsonPath("$.paymentMethodId").value(SCHEDULED_WITHDRAWAL_PAYMENT_METHOD_ID),
                        jsonPath("$.amount").value(SCHEDULED_WITHDRAWAL_AMOUNT),
                        jsonPath("$.createdAt")
                                .value(allOf(greaterThan(instantBeforeCreation), lessThan(instantAfterCreation))),
                        jsonPath("$.executeAt").value(SCHEDULED_WITHDRAWAL_EXECUTE_AT),
                        jsonPath("$.transactionId").doesNotExist(),
                        jsonPath("$.status").value(WithdrawalStatus.PENDING.toString()));
    }

    @Test
    void givenAScheduledWithdrawalWhenItIsTimeForItToBeProcessedThenItIsProcessed(@Autowired MockMvc mvc)
            throws Exception {
        // Given
        var mvcResult = mvc
                .perform(
                        post(WITHDRAWAL_RESOURCE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(this.createScheduledWithdrawalRequest)))
                .andReturn();

        var scheduledWithdrawalId =
                this.objectMapper.readTree(mvcResult.getResponse().getContentAsString()).at("/id").asLong();

        // When
        Thread.sleep(6_000);

        // Then
        mvc
                .perform(get(WITHDRAWAL_RESOURCE_URL + '/' + scheduledWithdrawalId))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(scheduledWithdrawalId),
                        jsonPath("$.userId").value(SCHEDULED_WITHDRAWAL_USER_ID),
                        jsonPath("$.paymentMethodId").value(SCHEDULED_WITHDRAWAL_PAYMENT_METHOD_ID),
                        jsonPath("$.amount").value(SCHEDULED_WITHDRAWAL_AMOUNT),
                        jsonPath("$.createdAt").isString(),
                        jsonPath("$.executeAt").value(SCHEDULED_WITHDRAWAL_EXECUTE_AT),
                        jsonPath("$.transactionId").isNumber(),
                        jsonPath("$.status").value(WithdrawalStatus.PROCESSING.toString()));
    }
}
