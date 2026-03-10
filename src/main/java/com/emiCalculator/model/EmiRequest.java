package com.emiCalculator.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@lombok.AllArgsConstructor
@Data
public class EmiRequest {

    @NotNull(message = "Loan value is required.")
    @Positive(message = "Loan value must be a positive number.")
    private Double loanValue;

    @NotNull(message = "Yearly interest rate is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Yearly interest rate must be greater than 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "Yearly interest rate must be less than or equal to 100.")
    private Double yearlyInterestRate;

    @NotNull(message = "Loan term years is required.")
    @Positive(message = "Loan term must be a positive number.")
    @Max(value = 30, message = "Loan term must be less than or equal to 30 years.")
    private Integer loanTermYears;

}
