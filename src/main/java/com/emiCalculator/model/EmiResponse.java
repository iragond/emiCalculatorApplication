package com.emiCalculator.model;

@lombok.AllArgsConstructor
@lombok.Data
public class EmiResponse {
    private Double emiAmount;
    private String message;
}