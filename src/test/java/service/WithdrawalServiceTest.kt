package service

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import com.wezaam.withdrawal.exception.TransactionException
import com.wezaam.withdrawal.model.PaymentMethod
import com.wezaam.withdrawal.model.WithdrawType
import com.wezaam.withdrawal.model.WithdrawalEntity
import com.wezaam.withdrawal.model.WithdrawalStatus
import com.wezaam.withdrawal.repository.WithdrawalRepository
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import com.wezaam.withdrawal.service.EventsService
import com.wezaam.withdrawal.service.WithdrawalFactory
import com.wezaam.withdrawal.service.WithdrawalProcessingService
import com.wezaam.withdrawal.service.WithdrawalService
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.Mockito.times
import java.time.Instant

class WithdrawalServiceTest {

    private val withdrawalRepository = Mockito.mock(WithdrawalRepository::class.java)
    private val withdrawalScheduleRepository = Mockito.mock(WithdrawalScheduledRepository::class.java)
    private val withdrawalFactory = WithdrawalFactory(withdrawalRepository, withdrawalScheduleRepository)
    private val notifierService = Mockito.mock(EventsService::class.java)
    private val processService = WithdrawalProcessingService()
    private val withdrawalService = WithdrawalService (withdrawalFactory, processService, notifierService)
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
    private val withdrawalEntity = WithdrawalEntity(
            withdrawalInformationDTO.id,
            withdrawalInformationDTO.transactionId,
            withdrawalInformationDTO.amount,
            Instant.now(),
            withdrawalInformationDTO.userId,
            withdrawalInformationDTO.paymentMethod,
            WithdrawalStatus.PENDING)
    private val updatedEntity = WithdrawalEntity(
            System.nanoTime(),
            withdrawalInformationDTO.transactionId,
            withdrawalInformationDTO.amount,
            Instant.now(),
            withdrawalInformationDTO.userId,
            withdrawalInformationDTO.paymentMethod,
            WithdrawalStatus.PROCESSING)

    @Before
    fun setUp() {
    }

    @Test
    fun create() {
        Mockito.`when`(withdrawalRepository.save(any(WithdrawalEntity::class.java))).thenReturn(withdrawalEntity)
        val result = withdrawalService.create(withdrawalInformationDTO)
        assertNotNull(result)
        Mockito.verify(withdrawalRepository, times(1)).save(any(WithdrawalEntity::class.java))
    }

    @Test
    fun process() {
        Mockito.`when`(withdrawalRepository.save(any(WithdrawalEntity::class.java))).thenReturn(updatedEntity)
        val result = withdrawalService.process(withdrawalInformationDTO)
        assertNotNull(result)
        assertNotNull(result.transactionId)
        assertTrue(result.status == WithdrawalStatus.PROCESSING)
        Mockito.verify(withdrawalRepository, times(1)).save(any(WithdrawalEntity::class.java))
        Mockito.verify(notifierService, times(1)).send(any(WithdrawalInformationDTO::class.java))
    }

    @Test(expected = TransactionException::class)
    fun testThatProcessThrowsException(){
        Mockito.`when`(withdrawalRepository.save(any(WithdrawalEntity::class.java)))
                .thenThrow(TransactionException("Test message"))
        val result = withdrawalService.process(withdrawalInformationDTO)
        assertNotNull(result)
        assertNotNull(result.transactionId)
        assertTrue(result.status == WithdrawalStatus.FAILED)
    }

    private fun <T> any(type : Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}