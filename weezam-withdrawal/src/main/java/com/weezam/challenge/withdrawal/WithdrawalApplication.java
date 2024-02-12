package com.weezam.challenge.withdrawal;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableEurekaClient
@EnableWebMvc
@EnableFeignClients
@EnableRabbit
public class WithdrawalApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithdrawalApplication.class, args);
	}

}
