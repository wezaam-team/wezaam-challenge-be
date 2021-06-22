package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO;
import com.wezaam.withdrawal.model.WithdrawalEntity;
import com.wezaam.withdrawal.model.WithdrawalScheduled;
import org.springframework.stereotype.Service;

@Service
public class EventsService {

    public void send(WithdrawalInformationDTO dto) {
        // send an event in message queue
    }

}
