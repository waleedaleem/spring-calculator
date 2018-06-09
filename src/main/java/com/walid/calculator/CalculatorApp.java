package com.walid.calculator;

import com.walid.calculator.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

@Slf4j
public class CalculatorApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new FileSystemXmlApplicationContext(
                "calculatorAppContext.xml");

        CalculatorService service = (CalculatorService) context.getBean("service");
        log.info("----------> Result= {}", service.calculate("+", "5", "4"));
        log.info("----------> Result= {}", service.calculate("+", "500", "400"));
        log.info("----------> Result= {}", service.calculate("-", "5", "4"));
        log.info("----------> Result= {}", service.calculate("-", "500", "400"));

        context.close();
    }
}
