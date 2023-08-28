package com.wezaam.withdrawal.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

	@Bean
	public Queue withdrawalInstantQueue() {
		// durable is false so that the queue and any messages on it will be removed when RabbitMQ is stopped
		return new Queue("withdrawalInstantQueue", false);
	}

	@Bean
	public Queue withdrawalScheduledQueue() {
		return new Queue("withdrawalScheduledQueue", false);
	}

	@Bean
	public Queue transactionRequestQueue() {return new Queue("transactionRequestQueue", false); }
}
