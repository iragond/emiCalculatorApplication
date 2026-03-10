package com.emiCalculator.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class EmiRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDownValidator() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Test
    void acceptsRequestWhenAllFieldsAreValid() {
        EmiRequest request = new EmiRequest(100000d, 10d, 10);

        Set<ConstraintViolation<EmiRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void reportsViolationWhenLoanValueIsNull() {
        EmiRequest request = new EmiRequest(null, 10d, 10);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Loan value is required.", errors.get("loanValue"));
    }

    @Test
    void reportsViolationWhenLoanValueIsZero() {
        EmiRequest request = new EmiRequest(0d, 10d, 10);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Loan value must be a positive number.", errors.get("loanValue"));
    }

    @Test
    void reportsViolationWhenLoanValueIsNegative() {
        EmiRequest request = new EmiRequest(-1d, 10d, 10);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Loan value must be a positive number.", errors.get("loanValue"));
    }

    @Test
    void reportsViolationWhenYearlyInterestRateIsNull() {
        EmiRequest request = new EmiRequest(100000d, null, 10);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Yearly interest rate is required.", errors.get("yearlyInterestRate"));
    }

    @Test
    void reportsViolationWhenYearlyInterestRateIsZero() {
        EmiRequest request = new EmiRequest(100000d, 0d, 10);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Yearly interest rate must be greater than 0.", errors.get("yearlyInterestRate"));
    }

    @Test
    void reportsViolationWhenYearlyInterestRateIsNegative() {
        EmiRequest request = new EmiRequest(100000d, -1d, 10);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Yearly interest rate must be greater than 0.", errors.get("yearlyInterestRate"));
    }

    @Test
    void acceptsRequestWhenYearlyInterestRateIsAtMaxBoundary() {
        EmiRequest request = new EmiRequest(100000d, 100d, 10);

        Set<ConstraintViolation<EmiRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void reportsViolationWhenYearlyInterestRateIsAboveMaxBoundary() {
        EmiRequest request = new EmiRequest(100000d, 100.0001d, 10);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Yearly interest rate must be less than or equal to 100.", errors.get("yearlyInterestRate"));
    }

    @Test
    void reportsViolationWhenLoanTermYearsIsNull() {
        EmiRequest request = new EmiRequest(100000d, 10d, null);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Loan term years is required.", errors.get("loanTermYears"));
    }

    @Test
    void reportsViolationWhenLoanTermYearsIsZero() {
        EmiRequest request = new EmiRequest(100000d, 10d, 0);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Loan term must be a positive number.", errors.get("loanTermYears"));
    }

    @Test
    void reportsViolationWhenLoanTermYearsIsNegative() {
        EmiRequest request = new EmiRequest(100000d, 10d, -1);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Loan term must be a positive number.", errors.get("loanTermYears"));
    }

    @Test
    void acceptsRequestWhenLoanTermYearsIsAtMaxBoundary() {
        EmiRequest request = new EmiRequest(100000d, 10d, 30);

        Set<ConstraintViolation<EmiRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void reportsViolationWhenLoanTermYearsIsAboveMaxBoundary() {
        EmiRequest request = new EmiRequest(100000d, 10d, 31);

        Map<String, String> errors = errorsByField(request);

        assertEquals("Loan term must be less than or equal to 30 years.", errors.get("loanTermYears"));
    }

    private static Map<String, String> errorsByField(EmiRequest request) {
        return validator.validate(request)
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (first, ignored) -> first
                ));
    }
}
