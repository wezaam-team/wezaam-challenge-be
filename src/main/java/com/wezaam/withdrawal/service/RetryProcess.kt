package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.function.Consumer

@Service
class RetryProcess(private val withdrawalService: WithdrawalService){

    private val logger = LoggerFactory.getLogger(javaClass)
    @Scheduled(fixedDelay = 10000)
    fun run() {
        logger.info("**** Retrying Service ****");
        withdrawalService.factory.findAllFailedWithdrawal()
                .forEach(Consumer { withdrawal: WithdrawalInformationDTO ->
                    withdrawalService.process(withdrawal) })
    }
}