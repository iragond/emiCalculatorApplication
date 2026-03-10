package com.emiCalculator.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import java.io.Serial;


@Data
public class EmiCalculatorRuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String errorMessage;
    private final HttpStatus statusCode;

    public EmiCalculatorRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.errorMessage = message;
        this.statusCode = httpStatus;
    }

    public EmiCalculatorRuntimeException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
        this.statusCode = httpStatus;
    }

}

