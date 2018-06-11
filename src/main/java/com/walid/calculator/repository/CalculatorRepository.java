package com.walid.calculator.repository;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author Walid Moustafa
 */

public interface CalculatorRepository {

    void pushNumber(BigDecimal number, CalculatorRepositoryImpl.EntryType entryType);

    BigDecimal popNumber();

    void unDoEntry();

    Collection<BigDecimal> readStack();

    void clearAll();

    // enum representing the type of the stack entry.
    // if it is an input number, then UNDO operator will just pop it out.
    // if it is a unary result (from a single operand operation), then it is popped out and the single operand is pushed in
    // if it is a binary result (from a two-operand operation), then it is popped out and both operands are pushed in
    enum EntryType {
        INPUT,
        UNARY_RESULT,
        BINARY_RESULT
    }
}