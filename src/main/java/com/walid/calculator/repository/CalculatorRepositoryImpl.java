package com.walid.calculator.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.NoSuchElementException;

import static com.walid.calculator.CalculatorApp.printAndLog;

@Slf4j
@Repository
public class CalculatorRepositoryImpl implements CalculatorRepository {

    private Deque<BigDecimal> inputs = new ArrayDeque<>();
    private Deque<BigDecimal> stack = new ArrayDeque<>();

    @Override
    public void push(BigDecimal number) {
        inputs.addLast(number);
        stack.addLast(number);
    }

    @Override
    public BigDecimal pop() {
        try {
            return stack.removeLast();
        } catch (NoSuchElementException ex) {
            printAndLog("Stack underflow (empty stack)", this.getClass().getSimpleName(), true);
            return null;
        }
    }

    @Override
    public Collection<BigDecimal> readStack() {
        return stack;
    }
}