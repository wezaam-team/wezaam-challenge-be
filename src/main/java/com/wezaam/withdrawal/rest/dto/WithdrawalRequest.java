package com.wezaam.withdrawal.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class WithdrawalRequest {


    @NotBlank
    @ApiModelProperty(position = 0, required = true, value = "Employer_id", example = "XY123456")
    private String employeeId;

    @NotBlank
    @ApiModelProperty(position = 1, required = true, value = "Payment_method", example = "BBVA")
    private String paymentMethod;

    @NotNull
    @ApiModelProperty(position = 2, required = true, value = "withdrawal_amount", example = "109.80")
    private BigDecimal withdrawalAmount;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(position = 3, required = true, value = "execution_date", example = "2022-03-05")
    private LocalDate executionDate;

    @NotBlank
    @ApiModelProperty(position = 4, required = true, value = "currency", example = "EUR")
    private String currency;
}
