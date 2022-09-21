package com.wezaam.withdrawal.service.user

import com.wezaam.withdrawal.model.User
import com.wezaam.withdrawal.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserCrudService(private val userRepository: UserRepository) :UserService {
    override fun findAll(): List<User?> = userRepository.findAll()
    override fun findById(id: Long) = userRepository.findById(id)
}