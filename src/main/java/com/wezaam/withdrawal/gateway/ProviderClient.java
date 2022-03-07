package com.wezaam.withdrawal.gateway;

import com.wezaam.withdrawal.domain.entities.Withdraw;
import com.wezaam.withdrawal.rest.dto.ProviderRequest;
import com.wezaam.withdrawal.rest.dto.ProviderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProviderClient {

    @Value("${provider.api.path}")
    private String path;

    @Autowired
    private WebClient client;


    public Mono<ProviderResponse> callProvider(Withdraw withdrawDataResult) {
        ProviderRequest providerRequest = ProviderRequest.builder().amount(withdrawDataResult.getAmount())
                .currency(withdrawDataResult.getCurrency())
                .identifier(withdrawDataResult.getEmployee().getEmployeeId()).build();

        return client.post()
                .uri(path)
                .body(Mono.just(providerRequest), ProviderRequest.class)
                .retrieve()
                .bodyToMono(ProviderResponse.class);

    }
}
