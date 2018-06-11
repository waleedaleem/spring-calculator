package com.walid.calculator.service;

import com.walid.calculator.repository.CalculatorRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static java.math.MathContext.DECIMAL64;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * @author Walid Moustafa
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:calculatorTestContext.xml"})
public class CalculatorServiceImplTest {

    private static BigDecimal firstOperand;
    private static BigDecimal secondOperand;
    @Autowired
    CalculatorServiceImpl service;
    @Autowired
    CalculatorRepository repository;

    @BeforeClass
    public static void setUp() {
        firstOperand = BigDecimal.TEN;
        secondOperand = BigDecimal.valueOf(17);
    }

    @Test
    public void setOperandToNull() {
        assertFalse(service.setOperand(1, null, "power"));
    }

    @Test
    public void setOperands() {
        assertTrue(service.setOperand(1, BigDecimal.TEN, "+"));
        assertTrue(service.setOperand(2, BigDecimal.ONE, "+"));
    }

    @Test
    public void setOperandWrongIndex() {
        assertFalse(service.setOperand(0, BigDecimal.TEN, "+"));
    }

    @Test
    public void add() {

        service.pushNumber(firstOperand);
        service.pushNumber(secondOperand);

        assertTrue(service.calculate("+"));

        assertThat(firstOperand.add(secondOperand), equalTo(service.popNumber()));
    }

    @Test
    public void divide() {

        service.pushNumber(firstOperand);
        service.pushNumber(secondOperand);

        assertTrue(service.calculate("/"));

        assertThat(firstOperand.divide(secondOperand, DECIMAL64), equalTo(service.popNumber()));
    }

    @Test
    public void power() {

        service.pushNumber(firstOperand);
        service.pushNumber(secondOperand);

        assertTrue(service.calculate("^"));

        assertThat(firstOperand.pow(secondOperand.intValue(), DECIMAL64), equalTo(service.popNumber()));
    }

    @Test
    public void sqrt() {

        service.pushNumber(new BigDecimal(144));

        assertTrue(service.calculate("sqrt"));

        assertThat(12, equalTo(service.popNumber().intValue()));
    }
}