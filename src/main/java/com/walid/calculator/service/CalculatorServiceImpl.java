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

import static com.walid.calculator.CalculatorApp.printAndLog;

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

    private boolean setOperand(int index, BigDecimal operand, String operator) {
        if (operand == null) {
            printAndLog("Processing of input line stopped. Null operand retrieved from repository",
                    this.getClass().getSimpleName(), true);
            return false;
        }
        if (index == 1) {
            this.firstOperand = operand;
        } else if (index == 2) {
            this.secondOperand = operand;
        } else {


            log.error("Invalid operand index {} passed to operator {}...Exiting", index, operator);
            System.err.printf("Invalid operand index %d passed to operator %s...Check errors in log", index, operator);
            return false;
        }
        return true;
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
    public boolean calculate(String operator) {
        try {
            int operandCount = getOperandCount(operator);
            if (operandCount == 1) {
                // set operand before instantiating the operationBean
                if (!setOperand(1, popNumber(), operator)) return false;
            } else if (operandCount == 2) {
                // set two operands before instantiating the operationBean
                if (!setOperand(2, popNumber(), operator)) return false;
                if (!setOperand(1, popNumber(), operator)) return false;
            } else {
                log.error("Operator {} has {} operator count configured. Please correct operator count to either 1 or 2 in the application context file...Exiting", operator, operandCount);
                throw new OperandException("Invalid operator count configuration...Check errors in log");
            }
            // operation bean is defined in calculatorAppContext.xml and processes firstOperand and secondOperand using SpEL
            pushNumber(getContext().getBean(operator, Operation.class).getResult());
        } catch (NoSuchBeanDefinitionException ex) {
            log.error("Operator {} has no corresponding bean. Please add one to application context file...Exiting", operator, ex);
            return false;
        }
        return true;
    }

    private int getOperandCount(String operator) {
        return getContext().getBean(operator, Operation.class).getOperandCount();
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