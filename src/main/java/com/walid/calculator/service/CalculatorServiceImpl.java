package com.walid.calculator.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Slf4j
@Getter
@Service(value = "service")
public class CalculatorServiceImpl implements CalculatorService, ApplicationContextAware {

    private BigDecimal firstOperand;
    private BigDecimal secondOperand;
    @Getter(AccessLevel.PRIVATE)
    private ApplicationContext context;

    @Autowired
    private DecimalFormat decimalFormat;

    private void setFirstOperand(String firstOperand) {
        try {
            this.firstOperand = new BigDecimal(firstOperand);
        } catch (NumberFormatException ex) {
            log.error("Invalid number {}...Exiting", firstOperand, ex);
            throw ex;
        }
    }

    private void setSecondOperand(String secondOperand) {
        try {
            this.secondOperand = new BigDecimal(secondOperand);
        } catch (NumberFormatException ex) {
            log.error("Invalid number {}...Exiting", firstOperand, ex);
            throw ex;
        }
    }

    @Override
    public String calculate(String operator, String operand) {
        // set operand before instantiating the operationBean
        setFirstOperand(operand);

        try {
            // operationBean is defined in calculatorAppContext.xml and processes firstOperand and secondOperand using SpEL
            return decimalFormat.format(((Operation) getContext().getBean(operator)).getResult());
        } catch (NoSuchBeanDefinitionException ex) {
            log.error("Operator {} has no corresponding bean. Please add one to application context file...Exiting", operator, ex);
            throw ex;
        }
    }

    @Override
    public String calculate(String operator, String firstOperand, String secondOperand) {
        // set secondOperand before delegating to the single operand override
        setSecondOperand(secondOperand);
        return calculate(operator, firstOperand);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
}
