package com.emiCalculator.emiCalculatorController;


import com.emiCalculator.Service.EmiCalculatorService;
import com.emiCalculator.model.EmiRequest;
import com.emiCalculator.model.EmiResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/emi")
public class EmiControllerController {

    private final EmiCalculatorService emiCalculatorService;

    public EmiControllerController(EmiCalculatorService emiCalculatorService) {
        this.emiCalculatorService = emiCalculatorService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<EmiResponse> calculateEmi(@Valid @RequestBody EmiRequest request) {
        try {
            double emi = emiCalculatorService.calculateEmi(
                    request.getLoanValue(),
                    request.getYearlyInterestRate(),
                    request.getLoanTermYears()
            );
            return ResponseEntity.ok(new EmiResponse(emi, "EMI calculated successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new EmiResponse(null, "Error calculating EMI: " + e.getMessage()));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        java.util.Map<String, String> errors = new java.util.LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            // keep first message per field (avoids overwriting if multiple constraints fail)
            errors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }

        ApiErrorResponse body = ApiErrorResponse.builder()
                .message("Validation failed")
                .fieldErrors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
