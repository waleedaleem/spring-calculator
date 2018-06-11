package com.walid.calculator.service;

import com.walid.calculator.repository.CalculatorRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

import static com.walid.calculator.CalculatorApp.printAndLog;
import static com.walid.calculator.repository.CalculatorRepository.EntryType;

/**
 * @author Walid Moustafa
 */

@Slf4j
@Getter
@Service(value = "service")
public class CalculatorServiceImpl implements CalculatorService, ApplicationContextAware {

    private BigDecimal firstOperand;
    private BigDecimal secondOperand;
    @Getter(AccessLevel.PRIVATE)
    private ApplicationContext context;

    @Autowired
    @Setter
    private CalculatorRepository repository;

    public CalculatorServiceImpl() {
        firstOperand = BigDecimal.valueOf(1);
        secondOperand = BigDecimal.valueOf(1);
    }

    boolean setOperand(int index, BigDecimal operand, String operator) {
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
            printAndLog(String.format("Invalid operand index %d passed to operator \"%s\"", index, operator),
                    this.getClass().getSimpleName(), true);
            return false;
        }
        return true;
    }

    @Override
    public void pushNumber(BigDecimal number) {
        pushNumber(number, EntryType.INPUT);
    }

    private void pushNumber(BigDecimal number, EntryType entryType) {
        repository.pushNumber(number, entryType);
    }

    @Override
    public BigDecimal popNumber() {
        return repository.popNumber();
    }

    @Override
    public boolean calculate(String operator) {
        if ("UNDO".equalsIgnoreCase(operator) && !readStack().isEmpty()) {
            repository.unDoEntry();
        } else if ("CLEAR".equalsIgnoreCase(operator)) {
            repository.clearAll();
        } else {
            try {
                int operandCount = getOperandCount(operator);
                if (operandCount == 1) {
                    if (readStack().isEmpty()) return false;
                    // set operand before instantiating the operationBean
                    if (!setOperand(1, popNumber(), operator)) return false;
                    // operation bean is defined in calculatorAppContext.xml and processes firstOperand and secondOperand using SpEL
                    pushNumber(getContext().getBean(operator, Operation.class).getResult(), EntryType.UNARY_RESULT);
                } else if (operandCount == 2) {
                    if (readStack().size() < 2) return false;
                    // set two operands before instantiating the operationBean
                    if (!setOperand(2, popNumber(), operator)) return false;
                    if (!setOperand(1, popNumber(), operator)) return false;
                    // operation bean is defined in calculatorAppContext.xml and processes firstOperand and secondOperand using SpEL
                    pushNumber(getContext().getBean(operator, Operation.class).getResult(), EntryType.BINARY_RESULT);
                } else {
                    log.error("Operator {} has {} operator count configured. Please correct operator count to either 1 or 2 in the application context file...Exiting", operator, operandCount);
                    throw new OperandException("Invalid operator count configuration...Check errors in log");
                }
            } catch (NoSuchBeanDefinitionException ex) {
                printAndLog(String.format("Operator %s has no corresponding bean. Please add one to application context file.", operator),
                        this.getClass().getSimpleName(), true);
                return false;
            }
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