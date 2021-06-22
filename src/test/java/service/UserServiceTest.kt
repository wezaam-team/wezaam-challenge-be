package service

import com.wezaam.withdrawal.model.PaymentMethod
import com.wezaam.withdrawal.model.User
import com.wezaam.withdrawal.repository.UserRepository
import com.wezaam.withdrawal.service.UserService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
internal class UserServiceTest{

    private val userRepo:UserRepository = Mockito.mock(UserRepository::class.java)
    private val userService = UserService(userRepo)
    private val paymentMethods: List<PaymentMethod> = listOf(PaymentMethod(1, "My bank account"))
    private val user1 = User(1, "Peter", paymentMethods, 5000.0)
    private val user2 = User(8, "Paul", paymentMethods, 800.0)
    private val userEntityList = listOf(user1, user2)


    @Test
    fun testThatServiceReturnUserListFromRepository(){
        Mockito.`when`(userRepo. findAll()).thenReturn(userEntityList)
        val result = userService.getUsersList()
        assert(result.size == 2)
        assert(result[1].name == "Paul")
    }

    @Test
    fun testThatServiceReturnUserFromRepository(){
        Mockito.`when`(userRepo. findById(8)).thenReturn(Optional.of(user2))
        val result = userService.getUserById(8)
        assert(result != null)
        assert(result.maxWithdrawalAmount == 800.0)
    }
}