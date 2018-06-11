package com.walid.calculator.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * @author Walid Moustafa
 */

@Slf4j
public class CalculatorRepositoryImplTest {

    CalculatorRepository repository;
    List<BigDecimal> pushList;

    @Before
    public void setUp() {
        repository = new CalculatorRepositoryImpl();

        // A random list of 10 numbers freshly initialised for every test run
        pushList = DoubleStream.generate(Math::random)
                .limit(10)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList());

        log.debug("pushing {}", pushList);

        pushList.forEach(num -> repository.pushNumber(num, CalculatorRepository.EntryType.INPUT));
    }

    /**
     * Verifies the LIFO nature of the Stack
     */
    @Test
    public void pushThenPopNumbersLIFO() {

        List<BigDecimal> popList = Stream.generate(repository::popNumber)
                .limit(pushList.size())
                .collect(Collectors.toList());
        Collections.reverse(popList);

        log.debug("popping {}", popList);

        assertThat(pushList, equalTo(popList));
    }

    /**
     * Do a list of random numbers then undo them all. repository stack should be empty by end of test.
     */
    @Test
    public void unDoEntries() {
        IntStream.range(0, pushList.size())
                .forEach((n) -> repository.unDoEntry());

        assertThat(repository.readStack(), empty());
    }

    @Test
    public void clearAll() {
        repository.clearAll();

        assertThat(repository.readStack(), empty());
    }
}