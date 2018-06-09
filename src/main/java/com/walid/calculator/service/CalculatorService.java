package com.walid.calculator.service;

public interface CalculatorService {
    String calculate(String operator, String operand);

    String calculate(String operator, String firstOperand, String secondOperand);
}
