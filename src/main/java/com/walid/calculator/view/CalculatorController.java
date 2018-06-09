package com.walid.calculator.view;

import com.walid.calculator.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Controller("controller")
public class CalculatorController {

    @Autowired
    private CalculatorService service;

    @Autowired
    private DecimalFormat decimalFormat;

    static List<String> tokenizeInput(String inputLine) {
        if (inputLine != null) {
            return Arrays.asList(inputLine.split("\\s+"));
        }
        return new ArrayList<>(0);
    }

    private static boolean isNumber(String number) {
        return BigDecimalValidator.getInstance().isValid(number);
    }

    private void processToken(String token) {
        if (isNumber(token)) {
            service.pushNumber(new BigDecimal(token));
        } else {
            service.calculate(token);
        }
    }

    public void acceptInputLine(String inputLine) {
        List<String> tokens = tokenizeInput(inputLine);
        if (! tokens.isEmpty()) {
            tokens.stream()
                    .sequential()
                    .filter(((Predicate<String>) String::isEmpty).negate())
                    .peek(token -> log.debug("received token {}", token))
                    .forEach(this::processToken);

            printStack();
        }
    }

    private void printStack() {
        System.out.printf("stack: %s%n", service.readStack().stream()
                .sequential()
                .map(decimalFormat::format)
                .collect(Collectors.joining(" "))
        );
    }
}