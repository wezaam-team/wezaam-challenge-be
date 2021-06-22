package service

import com.wezaam.withdrawal.dto.WithdrawalInformationDTO
import com.wezaam.withdrawal.model.*
import com.wezaam.withdrawal.repository.WithdrawalRepository
import com.wezaam.withdrawal.repository.WithdrawalScheduledRepository
import com.wezaam.withdrawal.service.WithdrawalFactory
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito
import java.time.Instant

class WithdrawalFactoryTest {

    private val withdrawalRepository = Mockito.mock(WithdrawalRepository::class.java)
    private val withdrawalScheduleRepository = Mockito.mock(WithdrawalScheduledRepository::class.java)
    private val withdrawalFactory = WithdrawalFactory(withdrawalRepository, withdrawalScheduleRepository)
    private val withdrawalScheduledInformationDTO = WithdrawalInformationDTO(
            23,
            0,
            500.0,
            Instant.now(),
            Instant.now(),
            3,
            PaymentMethod(1,"test account"),
            WithdrawalStatus.PENDING,
            WithdrawType.SCHEDULE)

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
            23,
            0,
            500.0,
            Instant.now(),
            3,
            PaymentMethod(1,"test account"),
            WithdrawalStatus.PENDING)
    private val scheduleWithdrawalEntity = WithdrawalScheduled(
            23,
            0,
            500.0,
            Instant.now(),
            Instant.now(),
            3,
            PaymentMethod(1,"test account"),
            WithdrawalStatus.PENDING)

    private val asapWithdrawalList:MutableList<WithdrawalInformationDTO> = mutableListOf()
    private val scheduleWithdrawalList:MutableList<WithdrawalInformationDTO> = mutableListOf()
    private val entityWithdrawalList:MutableList<WithdrawalEntity> = mutableListOf()
    private val entityScheduleWithdrawalList:MutableList<WithdrawalScheduled> = mutableListOf()

    @Before
    fun setUp() {
        asapWithdrawalList.add(withdrawalInformationDTO)
        scheduleWithdrawalList.add(withdrawalScheduledInformationDTO)
        entityWithdrawalList.add(withdrawalEntity)
        entityScheduleWithdrawalList.add(scheduleWithdrawalEntity)
    }

    @Test
    fun testThatFactoryCreateAsapWithdrawal(){
        val result = withdrawalFactory.create(withdrawalInformationDTO)
        assertNotNull(result)
        assertTrue(result is AsapWithdrawal)
    }

    @Test
    fun testThatFactoryCreateScheduleWithdrawal(){
        val result = withdrawalFactory.create(withdrawalScheduledInformationDTO)
        assertNotNull(result)
        assertTrue(result is ScheduleWithdrawal)
    }

    @Test
    fun findAllWithdrawal() {

        Mockito.`when`(withdrawalRepository.findAll()).thenReturn(entityWithdrawalList)
        Mockito.`when`(withdrawalScheduleRepository.findAll()).thenReturn(entityScheduleWithdrawalList)
        val result = withdrawalFactory.findAllWithdrawal()
        assertNotNull(result)
        assertTrue(result.size == 2)
    }

    @Test
    fun findAllPendingWithdrawal() {
        Mockito.`when`(withdrawalScheduleRepository.findAllByExecuteAtBefore(any(Instant::class.java)))
                .thenReturn(entityScheduleWithdrawalList)
        val result = withdrawalFactory.findAllPendingWithdrawal()
        assertNotNull(result)
        assertTrue(result.size == 1)
    }
    private fun <T> any(type : Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}