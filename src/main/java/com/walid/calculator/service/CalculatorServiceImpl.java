package com.walid.calculator.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Getter
@Service(value = "service")
public class CalculatorServiceImpl implements CalculatorService, ApplicationContextAware {

    private BigDecimal firstOperand;
    private BigDecimal secondOperand;
    @Getter(AccessLevel.PRIVATE)
    private ApplicationContext context;

    private void setFirstOperand(String firstOperand) {
        this.firstOperand = new BigDecimal(firstOperand);
    }

    private void setSecondOperand(String secondOperand) {
        this.secondOperand = new BigDecimal(secondOperand);
    }

    @Override
    public BigDecimal calculate(String operator, String firstOperand, String secondOperand) {
        // set firstOperand and secondOperand before instantiating the operationBean
        setFirstOperand(firstOperand);
        setSecondOperand(secondOperand);

        // operationBean is defined in calculatorAppContext.xml and processes firstOperand and secondOperand using SpEL
        Operation operationBean = (Operation) getContext().getBean(operator);

        if (operationBean != null) {
            return operationBean.getResult();
        } else {
            log.error("Operator {} has no corresponding bean. Please add one to application context file...Exiting", operator, new RuntimeException("Check log for errors"));
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
}
