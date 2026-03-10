package com.emiCalculator.emiCalculatorController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmiValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
}

