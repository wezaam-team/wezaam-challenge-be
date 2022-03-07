package com.wezaam.withdrawal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private ERROR_TYPE error;

    @Getter
    public enum ERROR_TYPE {
        EMPLOYEE_NOT_FOUND("000", "The employee does not exist", HttpStatus.NOT_FOUND),
        NOT_ENOUGH_BALANCE("001", "The balance does not enough", HttpStatus.BAD_REQUEST),
        PAYMENT_METHOD_NOT_FOUND("002", "The payment method does not exist", HttpStatus.BAD_REQUEST),
        ALREADY_OPERATION_RUNNING("003", "There is already an operation running", HttpStatus.CONFLICT);

        private String code;
        private String description;
        private HttpStatus status;

        ERROR_TYPE(String code, String description, HttpStatus status) {
            this.code = code;
            this.description = description;
            this.status = status;
        }
    }
}
