package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.rest.dto.ProviderRequest;
import com.wezaam.withdrawal.rest.dto.ProviderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

@ApiIgnore
@RestController
@Slf4j
public class ProviderExternalApiController {

    @PostMapping("/validation")
    public ResponseEntity<ProviderResponse> createTransaction(@RequestBody ProviderRequest providerRequest) {
        log.info("transaction received {}", providerRequest);
        return ResponseEntity.ok(ProviderResponse.builder().transactionId(String.valueOf(UUID.randomUUID())).build());

    }
}

