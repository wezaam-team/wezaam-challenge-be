package com.wezaam.withdrawal.resource;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class ApplicationToResourceExceptionMapper {

    @Pointcut("execution(public * *(..)) "
            + "&& within(com.wezaam.withdrawal.resource.*Resource) "
            + "&& @within(org.springframework.web.bind.annotation.RestController)")
    private void resourceOperation() {}

    @AfterThrowing(pointcut = "resourceOperation()", throwing = "exception")
    private void mapException(IllegalArgumentException exception) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @AfterThrowing(pointcut = "resourceOperation()", throwing = "exception")
    public void mapException(IllegalStateException exception) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage());
    }
}
