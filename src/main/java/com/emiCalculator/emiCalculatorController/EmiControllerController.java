package com.emiCalculator.emiCalculatorController;

import com.emiCalculator.Service.EmiCalculatorService;
import com.emiCalculator.model.EmiRequest;
import com.emiCalculator.model.EmiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.emiCalculator.utils.SshCommonConstants.EMI_CALCULATED_SUCCESSFULLY;

@RestController
@RequestMapping("/api/v1/emi")
public class EmiControllerController {

    private final EmiCalculatorService emiCalculatorService;

    public EmiControllerController(EmiCalculatorService emiCalculatorService) {
        this.emiCalculatorService = emiCalculatorService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<EmiResponse> calculateEmi(@Valid @RequestBody EmiRequest request) {
        double emi = emiCalculatorService.calculateEmi(
                request.getLoanValue(),
                request.getYearlyInterestRate(),
                request.getLoanTermYears()
        );
        return ResponseEntity.ok(new EmiResponse(emi, EMI_CALCULATED_SUCCESSFULLY));
    }
}
