package com.wezaam.withdrawal.exception;

import com.wezaam.withdrawal.helpers.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        Map<String, List<String>> result = new HashMap<>();
        result.put("Required params are missing", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                null,
                httpStatus,
                DateHelper.toZonedDateTime(LocalDateTime.now())
        );

        log.error("handleNotFoundException: {}", exception.getMessage());

        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException exception) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                null,
                httpStatus,
                DateHelper.toZonedDateTime(LocalDateTime.now())
        );

        log.error("handleForbiddenException: {}", exception.getMessage());

        return new ResponseEntity<>(response, httpStatus);
    }
}
