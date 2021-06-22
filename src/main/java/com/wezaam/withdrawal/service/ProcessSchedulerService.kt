package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
@Slf4j
class ProcessSchedulerService(private val withdrawalService:WithdrawalService) {

    private val logger = LoggerFactory.getLogger(javaClass)
    @Scheduled(fixedDelay = 5000)
    fun run() {
        logger.info("**** Running ProcessSchedulerService ****");
        withdrawalService.factory.findAllPendingWithdrawal()
                .forEach(Consumer { withdrawal: WithdrawalInformationDTO ->
                    withdrawalService.process(withdrawal) })
    }

}