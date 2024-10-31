package com.wezaam.withdrawal.infrastructure.rest.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wezaam.withdrawal.domain.exception.NotFoundException;
import com.wezaam.withdrawal.domain.exception.WithdrawalDomainException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetails> handlerException(NotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(
                        ProblemDetails.builder()
                                .code(NOT_FOUND.getReasonPhrase())
                                .message(exception.getMessage())
                                .build());
    }

    @ExceptionHandler(WithdrawalDomainException.class)
    public ResponseEntity<ProblemDetails> handlerException(WithdrawalDomainException exception) {
        return ResponseEntity.status(BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(
                        ProblemDetails.builder()
                                .code(BAD_REQUEST.getReasonPhrase())
                                .message(exception.getMessage())
                                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleException(
            MethodArgumentNotValidException methodArgumentNotValidException) {

        String exceptionMessage =
                methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining("--"));

        log.error(exceptionMessage, methodArgumentNotValidException);
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(
                        ProblemDetails.builder()
                                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .message(exceptionMessage)
                                .build());
    }
}
