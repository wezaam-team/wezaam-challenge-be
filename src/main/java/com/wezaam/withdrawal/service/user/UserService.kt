package com.wezaam.withdrawal.service.user

import com.wezaam.withdrawal.model.User
import java.util.*

interface UserService {
    fun findAll(): List<User?>
    fun findById(id: Long): Optional<User>
}