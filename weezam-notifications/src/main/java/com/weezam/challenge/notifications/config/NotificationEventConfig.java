package com.weezam.challenge.notifications.config;

import com.weezam.challenge.notifications.adapter.in.amqp.AmqpNotificationListener;
import com.weezam.challenge.notifications.domain.NotificationAggregate;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationEventConfig {

    public static final String NOTIFICATION_QUEUE = "weezand.withdrawal.queue.notifications";
    public static final String NOTIFICATION_EXCHANGE = "weezand.withdrawal.exchange.notifications";

    public static final String QUEUE_DLX = NOTIFICATION_QUEUE + ".dlx";
    public static final String EXCHANGE_DLX = NOTIFICATION_EXCHANGE + ".dlx";

    public static final String QUEUE_PARKING_LOT = NOTIFICATION_QUEUE + ".parking-lot";
    public static final String EXCHANGE_PARKING_LOT = NOTIFICATION_EXCHANGE + ".parking-lot";

    @Bean
    FanoutExchange notificationParkingLotExchange() {
        return new FanoutExchange(EXCHANGE_PARKING_LOT);
    }

    @Bean
    Queue notificationParkingLotQueue() {
        return QueueBuilder.durable(QUEUE_PARKING_LOT).build();
    }

    @Bean
    Binding notificationParkingLotBinding() {
        return BindingBuilder.bind(notificationParkingLotQueue()).to(notificationParkingLotExchange());
    }

    @Bean
    FanoutExchange notificationDeadLetterExchange() {
        return new FanoutExchange(EXCHANGE_DLX);
    }

    @Bean
    Queue notificationDeadLetterQueue() {
        return QueueBuilder.durable(QUEUE_DLX).build();
    }

    @Bean
    Binding notificationDeadLetterBinding() {
        return BindingBuilder.bind(notificationDeadLetterQueue()).to(notificationDeadLetterExchange());
    }

    @Bean
    Queue notificationMessagesQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
                .build();
    }

    @Bean
    DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    Binding notificationBindingMessages() {
        return BindingBuilder.bind(notificationMessagesQueue()).to(notificationExchange()).with(NOTIFICATION_QUEUE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
