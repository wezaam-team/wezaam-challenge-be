package com.wezaam.withdrawal.rest;


import com.wezaam.withdrawal.rest.dto.ErrorResponse;
import com.wezaam.withdrawal.rest.dto.RestApiResponse;
import com.wezaam.withdrawal.rest.dto.WithdrawalRequest;
import com.wezaam.withdrawal.service.WithdrawalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api
@RestController
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;


    @ApiOperation(value = "Create Withdrawal transaction")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Response Ok", response = RestApiResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Employer not found", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Syntax error in request body", response = ErrorResponse.class),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Internal server error", response = ErrorResponse.class)})
    @PostMapping("/withdrawal")
    public ResponseEntity<RestApiResponse> createWithdrawal(@Valid @RequestBody WithdrawalRequest withdrawalRequest) throws Exception {
        final RestApiResponse response = withdrawalService
                .createWithdrawalProcess(withdrawalRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
