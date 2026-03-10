package com.emiCalculator.emiCalculatorController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.emiCalculator.utils.SshCommonConstants.EMI_CALCULATED_SUCCESSFULLY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmiControllerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsSuccessResponseWhenRequestIsValid() throws Exception {
        String body = "{" +
                "\"loanValue\":100000," +
                "\"yearlyInterestRate\":10.0," +
                "\"loanTermYears\":10" +
                "}";

        mockMvc.perform(post("/api/v1/emi/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(EMI_CALCULATED_SUCCESSFULLY))
                .andExpect(jsonPath("$.emiAmount").isNumber());
    }

    @Test
    void returnsConstraintMessageWhenLoanValueIsMissing() throws Exception {
        String body = "{" +
                "\"yearlyInterestRate\":10.0," +
                "\"loanTermYears\":10" +
                "}";

        mockMvc.perform(post("/api/v1/emi/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.loanValue").value("Loan value is required."));
    }

    @Test
    void returnsConstraintMessageWhenLoanValueIsNegative() throws Exception {
        String body = "{" +
                "\"loanValue\":-1," +
                "\"yearlyInterestRate\":10.0," +
                "\"loanTermYears\":10" +
                "}";

        mockMvc.perform(post("/api/v1/emi/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.loanValue").value("Loan value must be a positive number."));
    }

    @Test
    void returnsConstraintMessageWhenYearlyInterestRateIsZero() throws Exception {
        String body = "{" +
                "\"loanValue\":100000," +
                "\"yearlyInterestRate\":0.0," +
                "\"loanTermYears\":10" +
                "}";

        mockMvc.perform(post("/api/v1/emi/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.yearlyInterestRate").value("Yearly interest rate must be greater than 0."));
    }

    @Test
    void returnsConstraintMessageWhenYearlyInterestRateIsAboveMax() throws Exception {
        String body = "{" +
                "\"loanValue\":100000," +
                "\"yearlyInterestRate\":100.01," +
                "\"loanTermYears\":10" +
                "}";

        mockMvc.perform(post("/api/v1/emi/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.yearlyInterestRate").value("Yearly interest rate must be less than or equal to 100."));
    }

    @Test
    void returnsConstraintMessageWhenLoanTermYearsIsAboveMax() throws Exception {
        String body = "{" +
                "\"loanValue\":100000," +
                "\"yearlyInterestRate\":10.0," +
                "\"loanTermYears\":31" +
                "}";

        mockMvc.perform(post("/api/v1/emi/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.loanTermYears").value("Loan term must be less than or equal to 30 years."));
    }
}