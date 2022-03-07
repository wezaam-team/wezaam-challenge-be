package com.wezaam.withdrawal.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.wezaam.withdrawal.domain.entities.Withdraw;
import com.wezaam.withdrawal.domain.entities.WithdrawStatusEnum;
import com.wezaam.withdrawal.domain.repositories.WithdrawRepository;
import com.wezaam.withdrawal.exception.BusinessException;
import com.wezaam.withdrawal.rest.dto.ErrorResponse;
import com.wezaam.withdrawal.rest.dto.RestApiResponse;
import com.wezaam.withdrawal.rest.dto.WithdrawalRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WithdrawOperationsIT {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private WithdrawRepository withdrawRepository;
    @MockBean
    private AmazonSimpleEmailService amazonSimpleEmailService;

    private CountDownLatch lock = new CountDownLatch(1);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("provider.api.baseUrl", wireMockServer::baseUrl);
    }

    @BeforeEach
    public void setUp() {
        webTestClient = webTestClient.mutate()
                .responseTimeout(Duration.ofMillis(300000))
                .build();
    }

    @Test
    public void withdrawSuccesToNotified() throws InterruptedException {

        // GIVEN
        wireMockServer.stubFor(
                WireMock.post("/validation")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\n" +
                                        "  \"transactionId\": \"d4c61123-38ba-44c3-b3a0-9e6a1c4a6d88\"\n" +
                                        "}"))
        );

        WithdrawalRequest request = WithdrawalRequest.builder()
                .currency("EUR")
                .employeeId("XY123456")
                .executionDate(LocalDate.now())
                .paymentMethod("BBVA")
                .withdrawalAmount(BigDecimal.valueOf(100)).build();

        RestApiResponse response = this.webTestClient
                .post()
                .uri("/withdrawal")
                .body(Mono.just(request), WithdrawalRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(RestApiResponse.class).returnResult().getResponseBody();

        lock.await(1000, TimeUnit.MILLISECONDS);

        Optional<Withdraw> withdrawalEntity = withdrawRepository.findById(Long.parseLong(response.getId()));
        Assertions.assertTrue(withdrawalEntity.isPresent());
        Assertions.assertEquals(WithdrawStatusEnum.NOTIFIED, withdrawalEntity.get().getStatus());

    }

    @Test
    public void withdrawErrorNotFoundEmployee() {

        WithdrawalRequest request = WithdrawalRequest.builder()
                .currency("EUR")
                .employeeId("XXXXX")
                .executionDate(LocalDate.now())
                .paymentMethod("BBVA")
                .withdrawalAmount(BigDecimal.valueOf(100)).build();
        ErrorResponse responseError = this.webTestClient
                .post()
                .uri("/withdrawal")
                .body(Mono.just(request), WithdrawalRequest.class)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals(responseError.getCode(), BusinessException.ERROR_TYPE.EMPLOYEE_NOT_FOUND.getCode());

    }

    @Test
    public void withdrawErrorAmountNotEnough() {

        WithdrawalRequest request = WithdrawalRequest.builder()
                .currency("EUR")
                .employeeId("XY123456")
                .executionDate(LocalDate.now())
                .paymentMethod("BBVA")
                .withdrawalAmount(BigDecimal.valueOf(999999.99)).build();
        ErrorResponse responseError = this.webTestClient
                .post()
                .uri("/withdrawal")
                .body(Mono.just(request), WithdrawalRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals(responseError.getCode(), BusinessException.ERROR_TYPE.NOT_ENOUGH_BALANCE.getCode());

    }

    @Test
    public void withdrawErrorPaymentMethodNotFound() {

        WithdrawalRequest request = WithdrawalRequest.builder()
                .currency("EUR")
                .employeeId("XY123456")
                .executionDate(LocalDate.now())
                .paymentMethod("TESTEST")
                .withdrawalAmount(BigDecimal.valueOf(100.01)).build();
        ErrorResponse responseError = this.webTestClient
                .post()
                .uri("/withdrawal")
                .body(Mono.just(request), WithdrawalRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals(responseError.getCode(), BusinessException.ERROR_TYPE.PAYMENT_METHOD_NOT_FOUND.getCode());

    }

    @Test
    public void withdrawErrorWithdrawalAlreadyExist() {

        WithdrawalRequest request = WithdrawalRequest.builder()
                .currency("EUR")
                .employeeId("MA987654")
                .executionDate(LocalDate.now())
                .paymentMethod("BBVA-1")
                .withdrawalAmount(BigDecimal.valueOf(100.01)).build();
        ErrorResponse responseError = this.webTestClient
                .post()
                .uri("/withdrawal")
                .body(Mono.just(request), WithdrawalRequest.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ErrorResponse.class).returnResult().getResponseBody();

        Assertions.assertEquals(responseError.getCode(), BusinessException.ERROR_TYPE.ALREADY_OPERATION_RUNNING.getCode());

    }

    @Test
    public void withdrawOKScheduled() {

        WithdrawalRequest request = WithdrawalRequest.builder()
                .currency("EUR")
                .employeeId("XY123456")
                .executionDate(LocalDate.now().plus(5, ChronoUnit.DAYS))
                .paymentMethod("BBVA")
                .withdrawalAmount(BigDecimal.valueOf(100.01)).build();
        RestApiResponse response = this.webTestClient
                .post()
                .uri("/withdrawal")
                .body(Mono.just(request), WithdrawalRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(RestApiResponse.class).returnResult().getResponseBody();


        Optional<Withdraw> withdrawalEntity = withdrawRepository.findById(Long.parseLong(response.getId()));
        Assertions.assertTrue(withdrawalEntity.isPresent());
        Assertions.assertEquals(WithdrawStatusEnum.SCHEDULED, withdrawalEntity.get().getStatus());

    }


    @Test
    public void withdrawSuccessAndFailedNotification() throws InterruptedException {
        // GIVEN
        WithdrawalRequest request = WithdrawalRequest.builder()
                .currency("EUR")
                .employeeId("XY123456")
                .executionDate(LocalDate.now())
                .paymentMethod("BBVA")
                .withdrawalAmount(BigDecimal.valueOf(100.01)).build();
        // WHEN
        wireMockServer.stubFor(
                WireMock.post("/validation")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\n" +
                                        "  \"transactionId\": \"d4c61123-38ba-44c3-b3a0-9e6a1c4a6d88\"\n" +
                                        "}"))
        );

        Mockito.when(amazonSimpleEmailService.sendEmail(Mockito.any()))
                .thenThrow(new RuntimeException());

        // THEN
        RestApiResponse response = this.webTestClient
                .post()
                .uri("/withdrawal")
                .body(Mono.just(request), WithdrawalRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(RestApiResponse.class).returnResult().getResponseBody();

        lock.await(1000, TimeUnit.MILLISECONDS);


        Optional<Withdraw> withdrawalEntity = withdrawRepository.findById(Long.parseLong(response.getId()));
        Assertions.assertTrue(withdrawalEntity.isPresent());
        Assertions.assertEquals(WithdrawStatusEnum.PROCESSED, withdrawalEntity.get().getStatus());

    }


    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }
}
