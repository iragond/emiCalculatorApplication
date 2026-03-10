package com.emiCalculator.Service;

import com.emiCalculator.exception.EmiCalculatorRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.emiCalculator.utils.SshCommonConstants.ERROR_CALCULATIING_EMI;
import static com.emiCalculator.utils.SshCommonConstants.INVALID_INPUTS;

@Slf4j
@Service
public class EmiCalculatorService {
    public double calculateEmi(double principal, double yearlyRate, int loanTermYears) {
        log.info("EMI calculation process started...");

        try {
            double monthlyRate = yearlyRate / 12 / 100;
            int months = Math.multiplyExact(loanTermYears, 12);

            double denominator = (Math.pow(1 + monthlyRate, months) - 1);
            if (denominator == 0) {
                throw new EmiCalculatorRuntimeException(INVALID_INPUTS, HttpStatus.BAD_REQUEST);
            }

            return principal * monthlyRate * Math.pow(1 + monthlyRate, months) / denominator;
        } catch (ArithmeticException ex) {
            throw new EmiCalculatorRuntimeException(ERROR_CALCULATIING_EMI, HttpStatus.BAD_REQUEST, ex);
        } catch (Exception ex) {
            throw new EmiCalculatorRuntimeException(ERROR_CALCULATIING_EMI, HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }
}