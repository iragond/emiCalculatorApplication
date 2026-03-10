package com.emiCalculator.model;

@lombok.AllArgsConstructor
@lombok.Builder
@lombok.Getter
@lombok.Setter
public class EmiResponse {
    private Double emiAmount;
    private String message;
}