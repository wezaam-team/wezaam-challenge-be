package com.wezaam.withdrawal.rest

import com.wezaam.withdrawal.dto.CreateWithdrawalRequest
import com.wezaam.withdrawal.exception.BadRequestException
import com.wezaam.withdrawal.exception.NotFoundException
import com.wezaam.withdrawal.service.WithdrawalManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WithdrawalController(private val withdrawalManager: WithdrawalManager) {

    @PostMapping("/create-withdrawals")
    fun create(@RequestBody createWithdrawalRequest: CreateWithdrawalRequest): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(withdrawalManager.create(createWithdrawalRequest))
        } catch (e: BadRequestException) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }


    @GetMapping("/find-all-withdrawals")
    fun findAll() = ResponseEntity.ok(withdrawalManager.findAllWithdrawals())

}