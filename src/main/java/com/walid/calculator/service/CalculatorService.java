package com.walid.calculator.service;

import java.math.BigDecimal;

public interface CalculatorService {
    BigDecimal calculate(String operator, String firstOperand, String secondOperand);
}
