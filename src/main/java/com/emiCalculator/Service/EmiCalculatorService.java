package com.emiCalculator.Service;

import org.springframework.stereotype.Service;

@Service
public class EmiCalculatorService {
    public double calculateEmi(double principal, double yearlyRate, int years) {
        double monthlyRate = yearlyRate / 12 / 100;
        int months = years * 12;
        if (monthlyRate == 0) {
            return principal / months;
        }
        double emi = principal * monthlyRate * Math.pow(1 + monthlyRate, months)
                / (Math.pow(1 + monthlyRate, months) - 1);
        return emi;
    }
}