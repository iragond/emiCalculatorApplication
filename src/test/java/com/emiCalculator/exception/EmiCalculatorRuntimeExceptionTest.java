package com.emiCalculator.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

class EmiCalculatorRuntimeExceptionTest {

    @Test
    void setsMessageAndStatusWhenCreatedWithMessageAndStatus() {
        EmiCalculatorRuntimeException ex = new EmiCalculatorRuntimeException("boom", HttpStatus.BAD_REQUEST);

        assertEquals("boom", ex.getMessage());
        assertEquals("boom", ex.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertNull(ex.getCause());
    }

    @Test
    void setsMessageStatusAndCauseWhenCreatedWithMessageStatusAndCause() {
        RuntimeException cause = new RuntimeException("root");

        EmiCalculatorRuntimeException ex = new EmiCalculatorRuntimeException("boom", HttpStatus.INTERNAL_SERVER_ERROR, cause);

        assertEquals("boom", ex.getMessage());
        assertEquals("boom", ex.getErrorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
        assertSame(cause, ex.getCause());
    }

    @Test
    void supportsNullMessage() {
        EmiCalculatorRuntimeException ex = new EmiCalculatorRuntimeException(null, HttpStatus.BAD_REQUEST);

        assertNull(ex.getMessage());
        assertNull(ex.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void supportsNullCause() {
        EmiCalculatorRuntimeException ex = new EmiCalculatorRuntimeException("boom", HttpStatus.BAD_REQUEST, null);

        assertEquals("boom", ex.getMessage());
        assertEquals("boom", ex.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertNull(ex.getCause());
    }

    @Test
    void supportsNullStatusCode() {
        EmiCalculatorRuntimeException ex = new EmiCalculatorRuntimeException("boom", null);

        assertEquals("boom", ex.getMessage());
        assertEquals("boom", ex.getErrorMessage());
        assertNull(ex.getStatusCode());
    }
}

