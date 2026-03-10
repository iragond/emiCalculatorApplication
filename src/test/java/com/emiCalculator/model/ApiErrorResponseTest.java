package com.emiCalculator.model;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


class ApiErrorResponseTest {

    @Test
    void exposesMessageAndFieldErrorsWhenBuiltWithValues() {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message("Validation failed")
                .fieldErrors(Map.of("loanValue", "Loan value is required."))
                .build();

        assertEquals("Validation failed", response.getMessage());
        assertEquals(Map.of("loanValue", "Loan value is required."), response.getFieldErrors());
    }

    @Test
    void allowsEmptyFieldErrors() {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message("Validation failed")
                .fieldErrors(Map.of())
                .build();

        assertEquals("Validation failed", response.getMessage());
        assertNotNull(response.getFieldErrors());
        assertTrue(response.getFieldErrors().isEmpty());
    }

    @Test
    void allowsNullFieldErrors() {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message("Validation failed")
                .fieldErrors(null)
                .build();

        assertEquals("Validation failed", response.getMessage());
        assertNull(response.getFieldErrors());
    }

    @Test
    void allowsNullMessage() {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message(null)
                .fieldErrors(Map.of())
                .build();

        assertNull(response.getMessage());
        assertNotNull(response.getFieldErrors());
    }
}
