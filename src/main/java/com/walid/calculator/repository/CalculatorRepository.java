package com.walid.calculator.repository;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author Walid Moustafa
 */
public interface CalculatorRepository {
    void push(BigDecimal number);

    BigDecimal pop();

    Collection<BigDecimal> readStack();
}