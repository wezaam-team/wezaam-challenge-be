package com.weezam.challenge.withdrawal.adapter.out.clients;

import com.weezam.challenge.withdrawal.adapter.out.clients.dto.PaymentMethodDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "WEEZAM-USER/payments")
public interface OpenFeignPaymentMethodClient {

    @GetMapping("/{id}")
    public PaymentMethodDto findOne(@PathVariable("id") Long id);
}
