package service

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import com.wezaam.withdrawal.model.PaymentMethod
import com.wezaam.withdrawal.model.WithdrawType
import com.wezaam.withdrawal.model.WithdrawalEntity
import com.wezaam.withdrawal.model.WithdrawalStatus
import com.wezaam.withdrawal.repository.WithdrawalRepository
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import com.wezaam.withdrawal.service.*
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito
import org.mockito.Mockito.times
import java.time.Instant

class RetryServiceTest() {
    private val withdrawalRepository = Mockito.mock(WithdrawalRepository::class.java)
    private val withdrawalScheduleRepository = Mockito.mock(WithdrawalScheduledRepository::class.java)
    private val withdrawalFactory = WithdrawalFactory(withdrawalRepository, withdrawalScheduleRepository)
    private val processService = WithdrawalProcessingService()
    private val withdrawalService = WithdrawalService(withdrawalFactory, processService, EventsService())
    private val retry = RetryProcess(withdrawalService)
    private val withdrawalInformationDTO = WithdrawalInformationDTO(
            23,
            0,
            500.0,
            Instant.now(),
            Instant.now(),
            3,
            PaymentMethod(1,"test account"),
            WithdrawalStatus.PENDING,
            WithdrawType.ASAP)
    private val failedEntity = WithdrawalEntity(
            System.nanoTime(),
            withdrawalInformationDTO.transactionId,
            withdrawalInformationDTO.amount,
            Instant.now(),
            withdrawalInformationDTO.userId,
            withdrawalInformationDTO.paymentMethod,
            WithdrawalStatus.FAILED)
    private val updatedEntity = WithdrawalEntity(
            System.nanoTime(),
            withdrawalInformationDTO.transactionId,
            withdrawalInformationDTO.amount,
            Instant.now(),
            withdrawalInformationDTO.userId,
            withdrawalInformationDTO.paymentMethod,
            WithdrawalStatus.PROCESSING)
    private val failedWithdrawalList:MutableList<WithdrawalEntity> = mutableListOf()

    @Before
    fun setUp() {
        failedWithdrawalList.add(failedEntity)
    }

    @Test
    fun run() {
        Mockito.`when`(withdrawalRepository.findAllByFailedStatus()).thenReturn(failedWithdrawalList)
        Mockito.`when`(withdrawalRepository.save(any(WithdrawalEntity::class.java))).thenReturn(updatedEntity)
        retry.run()
        Mockito.verify(withdrawalRepository, times(1)).save(any(WithdrawalEntity::class.java))
    }

    private fun <T> any(type : Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}