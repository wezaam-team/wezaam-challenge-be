package com.wezaam.withdrawal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class RuntimeConfig {


    @Bean
    public Clock systemDefaultClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }
}
