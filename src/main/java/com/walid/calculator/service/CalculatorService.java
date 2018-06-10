package com.walid.calculator.service;

import java.math.BigDecimal;
import java.util.Collection;

public interface CalculatorService {
    void pushNumber(BigDecimal number);

    BigDecimal popNumber();

    boolean calculate(String operator);

    Collection<BigDecimal> readStack();
}