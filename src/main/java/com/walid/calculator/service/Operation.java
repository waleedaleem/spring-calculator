package com.walid.calculator.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Walid Moustafa
 */

@Getter
@Setter
public class Operation {
    int operandCount;
    BigDecimal result;
}
