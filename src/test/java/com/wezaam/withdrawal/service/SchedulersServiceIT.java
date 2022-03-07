package com.wezaam.withdrawal.service;


import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.wezaam.withdrawal.domain.entities.Withdraw;
import com.wezaam.withdrawal.domain.entities.WithdrawStatusEnum;
import com.wezaam.withdrawal.domain.repositories.WithdrawRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class SchedulersServiceIT {
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    @Autowired
    private WithdrawRepository withdrawRepository;
    @Autowired
    private SchedulerJobService schedulerJobService;
    @MockBean
    private AmazonSimpleEmailService amazonSimpleEmailService;
    private CountDownLatch lock = new CountDownLatch(1);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("provider.api.baseUrl", wireMockServer::baseUrl);
    }


    @Test
    @Sql(scripts = {"/scripts/withdrawals_to_notify.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testReNotifyWhitdrawProcessed() {
        // GIVEN (data initials)
        Assertions.assertTrue(withdrawRepository.findByStatus(WithdrawStatusEnum.PROCESSED, Pageable.unpaged()).getTotalElements() > 0);
        // WHEN
        schedulerJobService.withdrawalRetryNotifications();
        // THEN
        Assertions.assertEquals(withdrawRepository.findByStatus(WithdrawStatusEnum.PROCESSED, Pageable.unpaged()).getTotalElements(), 0);

    }


    @Test
    @Sql(scripts = {"/scripts/withdrawals_to_scheduled_execute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testExecuteScheduledWidrawalsToday() throws InterruptedException {


        // GIVEN (data initials)
        wireMockServer.stubFor(
                WireMock.post("/validation")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withFixedDelay(100)
                                .withBody("{\n" +
                                        "  \"transactionId\": \"d4c61123-38ba-44c3-b3a0-9e6a1c4a6d88\"\n" +
                                        "}"))
        );
        LocalDate date = LocalDate.now();
        List<Withdraw> withdarawls = withdrawRepository.findByStatus(WithdrawStatusEnum.SCHEDULED, Pageable.unpaged()).toList();
        withdarawls
                .forEach(w -> {
                    w.setExecutionDate(date);
                });
        withdrawRepository.saveAll(withdarawls);
        // WHEN
        schedulerJobService.withdrawalExecution();
        // THEN
        lock.await(1000, TimeUnit.MILLISECONDS);
        List<Withdraw> withdarawlsModified = withdrawRepository.findAllById(withdarawls.stream().map(Withdraw::getId).collect(Collectors.toList()));

        Assertions.assertTrue(withdarawlsModified.stream().allMatch(w -> w.getStatus().equals(WithdrawStatusEnum.NOTIFIED)));

    }

    @Test
    @Sql(scripts = {"/scripts/withdrawals_to_scheduled_execute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testExecuteScheduledWidrawalsTomorrow() throws InterruptedException {


        // GIVEN (data initials)
        wireMockServer.stubFor(
                WireMock.post("/validation")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withFixedDelay(100)
                                .withBody("{\n" +
                                        "  \"transactionId\": \"d4c61123-38ba-44c3-b3a0-9e6a1c4a6d88\"\n" +
                                        "}"))
        );
        LocalDate date = LocalDate.now().plus(1, ChronoUnit.DAYS);
        List<Withdraw> withdarawls = withdrawRepository.findByStatus(WithdrawStatusEnum.SCHEDULED, Pageable.unpaged()).toList();
        withdarawls
                .forEach(w -> {
                    w.setExecutionDate(date);
                });
        withdrawRepository.saveAll(withdarawls);
        // WHEN
        schedulerJobService.withdrawalExecution();
        // THEN
        lock.await(1000, TimeUnit.MILLISECONDS);
        List<Withdraw> withdarawlsModified = withdrawRepository.findAllById(withdarawls.stream().map(Withdraw::getId).collect(Collectors.toList()));

        Assertions.assertTrue(withdarawlsModified.stream().allMatch(w -> w.getStatus().equals(WithdrawStatusEnum.SCHEDULED)));

    }

}
