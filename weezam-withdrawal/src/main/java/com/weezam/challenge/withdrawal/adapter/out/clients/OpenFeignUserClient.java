package com.weezam.challenge.withdrawal.adapter.out.clients;

import com.weezam.challenge.withdrawal.adapter.out.clients.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "WEEZAM-USER/users")
public interface OpenFeignUserClient {

    @GetMapping("/{id}")
    public UserDto findOne(@PathVariable("id") Long id);
}
