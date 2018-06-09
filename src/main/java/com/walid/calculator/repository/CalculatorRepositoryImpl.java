package com.walid.calculator.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

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
        return stack.removeLast();
    }

    @Override
    public Collection<BigDecimal> readStack() {
        return stack;
    }
}