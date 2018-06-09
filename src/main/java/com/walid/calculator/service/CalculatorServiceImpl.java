package com.walid.calculator.service;

import com.walid.calculator.repository.CalculatorRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

@Slf4j
@Getter
@Service(value = "service")
public class CalculatorServiceImpl implements CalculatorService, ApplicationContextAware {

    private BigDecimal firstOperand;
    private BigDecimal secondOperand;
    @Getter(AccessLevel.PRIVATE)
    private ApplicationContext context;

    @Autowired
    private CalculatorRepository repository;

    public CalculatorServiceImpl() {
        firstOperand = BigDecimal.valueOf(1);
        secondOperand = BigDecimal.valueOf(1);
    }

    private void setOperand(int index, BigDecimal operand, String operator) {
        try {
            if (index == 1) {
                this.firstOperand = operand;
            } else if (index == 2) {
                this.secondOperand = operand;
            } else {
                log.error("Invalid operand index {} passed to operator {}...Exiting", index, operator);
                throw new OperandException(
                        String.format("Invalid operand index %d passed to operator %s...Check errors in log", index, operator)
                );
            }
        } catch (NumberFormatException ex) {
            log.error("Invalid operand numeric value {} passed to operator {}...Exiting", operand, operator, ex);
            throw ex;
        }
    }

    @Override
    public void pushNumber(BigDecimal number) {
        repository.push(number);
    }

    @Override
    public BigDecimal popNumber() {
        return repository.pop();
    }

    @Override
    public void calculate(String operator) {
        try {
            int operandCount = getContext().getBean(operator, Operation.class).getOperandCount();
            if (operandCount == 1) {
                // set operand before instantiating the operationBean
                setOperand(1, popNumber(), operator);
            } else if (operandCount == 2) {
                // set two operands before instantiating the operationBean
                setOperand(2, popNumber(), operator);
                setOperand(1, popNumber(), operator);
            } else {
                log.error("Operator {} has {} operator count configured. Please correct operator count to either 1 or 2 in the application context file...Exiting", operator, operandCount);
                throw new OperandException("Invalid operator count configuration...Check errors in log");
            }
            // operation bean is defined in calculatorAppContext.xml and processes firstOperand and secondOperand using SpEL
            pushNumber(getContext().getBean(operator, Operation.class).getResult());
        } catch (NoSuchBeanDefinitionException ex) {
            log.error("Operator {} has no corresponding bean. Please add one to application context file...Exiting", operator, ex);
            throw ex;
        }
    }

    @Override
    public Collection<BigDecimal> readStack() {
        return repository.readStack();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    private static class OperandException extends IllegalArgumentException {

        public OperandException(String message) {
            super(message);
        }
    }
}