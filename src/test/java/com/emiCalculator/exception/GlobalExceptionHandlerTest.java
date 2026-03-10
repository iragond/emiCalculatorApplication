package com.emiCalculator.exception;

import com.emiCalculator.model.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    void returnsBadRequestWithFieldErrorsAndValidationFailedMessageWhenValidationExceptionOccurs() throws Exception {
        MethodArgumentNotValidException ex = methodArgumentNotValidExceptionWithFieldErrors(
                new FieldError("emiRequest", "loanValue", null, false, null, null, "Loan value is required."),
                new FieldError("emiRequest", "yearlyInterestRate", -1, false, null, null, "Yearly interest rate must be greater than 0.")
        );

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("Loan value is required.", response.getBody().getFieldErrors().get("loanValue"));
        assertEquals("Yearly interest rate must be greater than 0.", response.getBody().getFieldErrors().get("yearlyInterestRate"));
    }

    @Test
    void keepsFirstValidationMessagePerFieldWhenMultipleErrorsExistForSameField() throws Exception {
        MethodArgumentNotValidException ex = methodArgumentNotValidExceptionWithFieldErrors(
                new FieldError("emiRequest", "loanValue", null, false, null, null, "Loan value is required."),
                new FieldError("emiRequest", "loanValue", -1, false, null, null, "Loan value must be a positive number.")
        );

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Loan value is required.", response.getBody().getFieldErrors().get("loanValue"));
        assertEquals(1, response.getBody().getFieldErrors().size());
    }

    @Test
    void returnsBadRequestWithEmptyFieldErrorsWhenNoFieldErrorsExist() throws Exception {
        MethodArgumentNotValidException ex = methodArgumentNotValidExceptionWithBindingResult(new BeanPropertyBindingResult(new Object(), "emiRequest"));

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertTrue(response.getBody().getFieldErrors().isEmpty());
    }

    @Test
    void returnsInternalServerErrorWithExceptionMessageWhenGenericExceptionOccurs() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<?> response = handler.handleGenericEx(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("boom", response.getBody());
    }

    @Test
    void returnsInternalServerErrorWithNullBodyWhenGenericExceptionMessageIsNull() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<?> response = handler.handleGenericEx(new RuntimeException((String) null));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    private static MethodArgumentNotValidException methodArgumentNotValidExceptionWithFieldErrors(FieldError... fieldErrors) throws Exception {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "emiRequest");
        for (FieldError fe : fieldErrors) {
            bindingResult.addError(fe);
        }
        return methodArgumentNotValidExceptionWithBindingResult(bindingResult);
    }

    private static MethodArgumentNotValidException methodArgumentNotValidExceptionWithBindingResult(BindingResult bindingResult) throws Exception {
        Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("dummyMethod", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        return new MethodArgumentNotValidException(parameter, bindingResult);
    }

    @SuppressWarnings("unused")
    private static void dummyMethod(String ignored) {
    }
}
