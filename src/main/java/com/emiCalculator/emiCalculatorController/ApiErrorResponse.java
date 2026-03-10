package com.emiCalculator.emiCalculatorController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    private Map<String, String> fieldErrors;
}

