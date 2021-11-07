package com.wezaam.withdrawal.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"withdrawal.process.delay=10000", "withdrawal.notify.delay=10000"})
public class WithdrawalControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void createAsapWithdrawal() throws Exception {
        mvc.perform(post("/create-withdrawals")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "3")
                .param("paymentMethodId", "5")
                .param("amount", "650.00")
                .param("executeAt", "ASAP"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createScheduledWithdrawal() throws Exception {
        mvc.perform(post("/create-withdrawals")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "3")
                .param("paymentMethodId", "5")
                .param("amount", "650.00")
                .param("executeAt", "2021-11-07T19:55:00Z"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createWithdrawalNonExistentUser() throws Exception {
        mvc.perform(post("/create-withdrawals")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "9")
                .param("paymentMethodId", "5")
                .param("amount", "650.00")
                .param("executeAt", "ASAP"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User not found"));
    }

    @Test
    void createWithdrawalNonExistentPaymentMethod() throws Exception {
        mvc.perform(post("/create-withdrawals")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "2")
                .param("paymentMethodId", "11")
                .param("amount", "650.00")
                .param("executeAt", "ASAP"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Payment method not found"));
    }

    @Test
    void createWithdrawalInvalidPaymentMethod() throws Exception {
        mvc.perform(post("/create-withdrawals")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", "2")
                .param("paymentMethodId", "1")
                .param("amount", "650.00")
                .param("executeAt", "ASAP"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Invalid payment method"));
    }
}
