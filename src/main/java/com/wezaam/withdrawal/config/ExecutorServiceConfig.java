package com.wezaam.withdrawal.config;

import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceConfig {

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }
}
