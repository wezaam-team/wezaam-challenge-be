package com.wezaam.withdrawal.rest

import com.wezaam.withdrawal.model.User
import com.wezaam.withdrawal.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/find-all-users")
    fun findAll(): List<User?>? = userService.findAll()

    @GetMapping("/find-user-by-id/{id}")
    fun findById(@PathVariable id: Long): User? = userService.findById(id).get()

}