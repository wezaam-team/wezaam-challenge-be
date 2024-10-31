package com.weezam.challenge.notifications.config;

import com.weezam.challenge.notifications.domain.NotificationAggregate;
import com.weezam.challenge.notifications.domain.aggregate.DefaultNotificationAggregate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationAggregateConfig {

    @Bean
    public NotificationAggregate userAggregate() {
        return new DefaultNotificationAggregate();
    }
}
