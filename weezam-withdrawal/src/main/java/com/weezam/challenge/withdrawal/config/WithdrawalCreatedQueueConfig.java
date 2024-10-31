package com.weezam.challenge.withdrawal.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WithdrawalCreatedQueueConfig {

    public static final String NEW_WITHDRAWAL_QUEUE = "weezand.withdrawal.queue.created";
    public static final String NEW_WITHDRAWAL_EXCHANGE = "weezand.withdrawal.exchange.created";
    public static final String NEW_WITHDRAWAL_EXCHANGE_DELAYED = NEW_WITHDRAWAL_EXCHANGE + ".delayed";


    public static final String QUEUE_DLX = NEW_WITHDRAWAL_QUEUE + ".dlx";
    public static final String EXCHANGE_DLX = NEW_WITHDRAWAL_EXCHANGE + ".dlx";

    public static final String QUEUE_PARKING_LOT = NEW_WITHDRAWAL_QUEUE + ".parking-lot";
    public static final String EXCHANGE_PARKING_LOT = NEW_WITHDRAWAL_EXCHANGE + ".parking-lot";

    @Bean
    FanoutExchange newWithdrawalParkingLotExchange() {
        return new FanoutExchange(EXCHANGE_PARKING_LOT);
    }

    @Bean
    Queue newWithdrawalParkingLotQueue() {
        return QueueBuilder.durable(QUEUE_PARKING_LOT).build();
    }

    @Bean
    Binding newWithdrawalParkingLotBinding() {
        return BindingBuilder.bind(newWithdrawalParkingLotQueue()).to(newWithdrawalParkingLotExchange());
    }

    @Bean
    FanoutExchange newWithdrawalDeadLetterExchange() {
        return new FanoutExchange(EXCHANGE_DLX);
    }

    @Bean
    Queue newWithdrawalDeadLetterQueue() {
        return QueueBuilder.durable(QUEUE_DLX).build();
    }

    @Bean
    Binding newWithdrawalDeadLetterBinding() {
        return BindingBuilder.bind(newWithdrawalDeadLetterQueue()).to(newWithdrawalDeadLetterExchange());
    }

    @Bean
    Queue newWithdrawalMessagesQueue() {
        return QueueBuilder.durable(NEW_WITHDRAWAL_QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
                .build();
    }

    @Bean
    DirectExchange newWithdrawalExchange() {
        return new DirectExchange(NEW_WITHDRAWAL_EXCHANGE);
    }

    @Bean
    CustomExchange newWithdrawalExchangeDelayed() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(NEW_WITHDRAWAL_EXCHANGE_DELAYED, "x-delayed-message", true, false, args);

    }

    /**
     *Bind order delay plug-in queue to switch
     */
    @Bean
    public Binding newWithdrawalExchangeDelayedBinding() {
        return BindingBuilder
                .bind(newWithdrawalMessagesQueue())
                .to(newWithdrawalExchangeDelayed())
                .with(NEW_WITHDRAWAL_QUEUE)
                .noargs();
    }

    @Bean
    Binding bindingMessages() {
        return BindingBuilder.bind(newWithdrawalMessagesQueue()).to(newWithdrawalExchange()).with(NEW_WITHDRAWAL_QUEUE);
    }
}
