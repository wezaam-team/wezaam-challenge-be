package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.dto.UserDTO
import com.wezaam.withdrawal.exception.UserNotFoundException
import com.wezaam.withdrawal.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class UserService(private val repository: UserRepository) {
    /**
     * This method call User repository and map to Dto Object
     */
    fun getUsersList():List<UserDTO> {
        return repository.findAll().stream()
                .map { item ->  UserDTO(
                        item.id,
                        item.firstName,
                        item.paymentMethods,
                        item.maxWithdrawalAmount)}
                .collect(Collectors.toList())
    }

    /**
     * This method call User repository and map to Dto Object by Id
     */
    fun getUserById(id:Long):UserDTO {
        val userEntity = repository.findById(id)
        if (userEntity.isPresent){
            return UserDTO(
                    userEntity.get().id,
                    userEntity.get().firstName,
                    userEntity.get().paymentMethods,
                    userEntity.get().maxWithdrawalAmount)
        } else {
            throw UserNotFoundException()
        }
    }
}