package com.wezaam.withdrawal.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestApiResponse {

    @ApiModelProperty(value = "Transaction identifier", example = "0981988")
    @JsonProperty("identifier")
    private String id;
}
