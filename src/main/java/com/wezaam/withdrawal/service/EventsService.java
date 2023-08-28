package com.wezaam.withdrawal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezaam.withdrawal.model.WithdrawalInstant;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import com.wezaam.withdrawal.model.dto.TransactionRequestDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventsService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    final static String withdrawalInstantQueue = "withdrawalInstantQueue";
    final static String withdrawalScheduledQueue = "withdrawalScheduledQueue";
    final static String transactionRequestQueue = "transactionRequestQueue";

    @Async
    public void send(WithdrawalInstant withdrawalInstant) {
        try {
            rabbitTemplate.convertAndSend(withdrawalInstantQueue, objectMapper.writeValueAsString(withdrawalInstant));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void send(WithdrawalScheduled withdrawalScheduled) {
        try {
            rabbitTemplate.convertAndSend(withdrawalScheduledQueue, objectMapper.writeValueAsString(withdrawalScheduled));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void send(TransactionRequestDto transactionRequest) throws JsonProcessingException {
        rabbitTemplate.convertAndSend(transactionRequestQueue, objectMapper.writeValueAsString(transactionRequest));
    }
}
