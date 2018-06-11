package com.walid.calculator.repository;

import com.walid.calculator.model.NumericEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.stream.Collectors;

import static com.walid.calculator.CalculatorApp.printAndLog;

/**
 * @author Walid Moustafa
 */

@Slf4j
@Repository("repository")
public class CalculatorRepositoryImpl implements CalculatorRepository {

    // unlike stack, the journal does NOT delete any entries except when UNDOing (helps with the UNDO)
    private Deque<NumericEntry> journal = new ArrayDeque<>();
    private Deque<NumericEntry> stack = new ArrayDeque<>();

    @Override
    public void pushNumber(BigDecimal number, EntryType entryType) {
        NumericEntry numericEntry = new NumericEntry(number, entryType);
        journal.addLast(numericEntry);
        stack.addLast(numericEntry);
    }

    @Override
    public BigDecimal popNumber() {
        NumericEntry numericEntry = popNumericEntry();
        if (numericEntry != null) {
            return numericEntry.getValue();
        }
        return null;
    }

    @Override
    public void unDoEntry() {
        NumericEntry numericEntry = popNumericEntry();
        if (numericEntry != null) {
            EntryType entryType = numericEntry.getType();
            switch (entryType) {
                case INPUT:
                    journal.removeLast();
                    break;
                case UNARY_RESULT:
                    journal.removeLast();
                    stack.addLast(journal.peekLast());
                    break;
                case BINARY_RESULT:
                    journal.removeLast();
                    NumericEntry secondOperand = journal.removeLast();
                    stack.addLast(journal.peekLast());
                    journal.addLast(secondOperand);
                    stack.addLast(journal.peekLast());
            }
        }
    }

    private NumericEntry popNumericEntry() {
        if (stack.isEmpty()) {
            printAndLog("Stack underflow (empty stack)", this.getClass().getSimpleName(), true);
            return null;
        }
        return stack.removeLast();
    }

    @Override
    public Collection<BigDecimal> readStack() {
        return stack.stream()
                .map(NumericEntry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public void clearAll() {
        stack.clear();
        journal.clear();
    }
}