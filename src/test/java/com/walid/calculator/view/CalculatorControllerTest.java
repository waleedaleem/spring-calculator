package com.walid.calculator.view;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class CalculatorControllerTest {

    @Test
    public void tokenizeInput() {
        String numbers = DoubleStream.generate(Math::random)
                .limit(10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));

        log.debug("randomly generated input {}", numbers);

        String tokens = String.join(" ",
                CalculatorController.tokenizeInput(numbers)
                        .stream()
                        .sequential()
                        .collect(Collectors.joining(" "))
        );

        assertThat(tokens, equalTo(numbers));
    }
}