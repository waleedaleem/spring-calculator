package com.walid.calculator.model;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

import static com.walid.calculator.repository.CalculatorRepositoryImpl.EntryType;

/**
 * @author Walid Moustafa
 */

@Data
public class NumericEntry {

    @NonNull
    private BigDecimal value;

    @NonNull
    private EntryType type;
}