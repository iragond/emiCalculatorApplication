package com.emiCalculator.Service;

import com.emiCalculator.exception.EmiCalculatorRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static com.emiCalculator.utils.SshCommonConstants.ERROR_CALCULATIING_EMI;
import static org.junit.jupiter.api.Assertions.*;

class EmiCalculatorServiceTest {

    private final EmiCalculatorService service = new EmiCalculatorService();

    @Test
    void calculatesEmiForValidInputs() {
        double emi = service.calculateEmi(100000, 10, 10);

        assertTrue(emi > 0);
        assertEquals(1321.51, emi, 0.05);
    }



    @Test
    void throwsBadRequestWhenLoanTermYearsOverflowDuringMonthCalculation() {
        EmiCalculatorRuntimeException ex = assertThrows(
                EmiCalculatorRuntimeException.class,
                () -> service.calculateEmi(100000, 10, Integer.MAX_VALUE)
        );

        assertEquals(ERROR_CALCULATIING_EMI, ex.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertNotNull(ex.getCause());
        assertInstanceOf(ArithmeticException.class, ex.getCause());
    }

    @Test
    void wrapsUnexpectedExceptionsAsInternalServerError() {
        EmiCalculatorService throwingService = new EmiCalculatorService() {
            @Override
            public double calculateEmi(double principal, double yearlyRate, int loanTermYears) {
                try {
                    throw new IllegalStateException("boom");
                } catch (ArithmeticException ex) {
                    throw new EmiCalculatorRuntimeException(ERROR_CALCULATIING_EMI, HttpStatus.BAD_REQUEST, ex);
                } catch (Exception ex) {
                    throw new EmiCalculatorRuntimeException(ERROR_CALCULATIING_EMI, HttpStatus.INTERNAL_SERVER_ERROR, ex);
                }
            }
        };

        EmiCalculatorRuntimeException ex = assertThrows(
                EmiCalculatorRuntimeException.class,
                () -> throwingService.calculateEmi(100000, 10, 10)
        );

        assertEquals(ERROR_CALCULATIING_EMI, ex.getErrorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
        assertNotNull(ex.getCause());
        assertInstanceOf(IllegalStateException.class, ex.getCause());
    }
}