package com.wezaam.withdrawal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.wezaam.withdrawal", "com.wezaam.common"})
@EntityScan({"com.wezaam.withdrawal", "com.wezaam.common"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
