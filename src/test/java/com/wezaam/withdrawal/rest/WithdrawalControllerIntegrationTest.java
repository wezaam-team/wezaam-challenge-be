package com.wezaam.withdrawal.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import com.wezaam.withdrawal.rest.adapter.WithdrawalAdapter;
import com.wezaam.withdrawal.rest.api.withdrawal.CreateWithdrawalDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WithdrawalController.class)
class WithdrawalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserController userController;

    @MockBean
    PaymentMethodRepository paymentMethodRepository;

    @MockBean
    WithdrawalAdapter withdrawalAdapter;

    @Test
    void shouldReturn400WhenInvalidInput() throws Exception {
        CreateWithdrawalDto withdrawalDto = new CreateWithdrawalDto(null, 1L, 10d, "ASAP", null);

        mockMvc.perform(post("/withdrawals")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(withdrawalDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        CreateWithdrawalDto withdrawalDto = new CreateWithdrawalDto(1L, 1L, 10d, "ASAP", null);
        when(userController.findById(1L)).thenThrow();

        mockMvc.perform(post("/withdrawals")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(withdrawalDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("User not found"));
    }

    @Test
    void shouldReturn404WhenPaymentMethodNotFound() throws Exception {
        CreateWithdrawalDto withdrawalDto = new CreateWithdrawalDto(1L, 1L, 10d, "ASAP", null);
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/withdrawals")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(withdrawalDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Payment method not found"));
    }

    @Test
    void shouldReturnNoContentWhenWithdrawalIsSuccessful() throws Exception {
        CreateWithdrawalDto withdrawalDto = new CreateWithdrawalDto(1L, 1L, 10d, "ASAP", null);
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(new PaymentMethod()));

        mockMvc.perform(post("/withdrawals")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(withdrawalDto)))
                .andExpect(status().isNoContent());
    }
}
