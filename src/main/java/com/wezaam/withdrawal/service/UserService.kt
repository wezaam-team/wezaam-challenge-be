package com.wezaam.withdrawal.service

import com.wezaam.withdrawal.model.User
import com.wezaam.withdrawal.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun findAll(): List<User?>? = userRepository.findAll()
    fun findById(id: Long) = userRepository.findById(id)
}