package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.exception.BusinessException;
import com.wezaam.withdrawal.rest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class WithdrawControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handler(BusinessException ex) {

        return new ResponseEntity<>(ErrorResponse
                .builder()
                .code(ex.getError().getCode())
                .message(ex.getError().getDescription())
                .build(), ex.getError().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException ex) {


        return new ResponseEntity<>(ErrorResponse
                .builder()
                .code("-1")
                .message(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
