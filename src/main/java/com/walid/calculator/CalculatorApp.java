package com.walid.calculator;

import com.walid.calculator.view.CalculatorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Scanner;

/**
 * @author Walid Moustafa
 */

@Slf4j
@Configuration
@ImportResource(locations = "file:calculatorAppContext.xml")
public class CalculatorApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(CalculatorApp.class);

        printAndLog("Welcome to Reverse Calculator. When finished, enter 'q' to quit.",
                CalculatorApp.class.getSimpleName(), false);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(">> ");
                String inputLine = scanner.nextLine();
                if ("q".equalsIgnoreCase(inputLine.trim())) {
                    log.info("User quit successfully.");
                    printAndLog("Bye!", CalculatorApp.class.getSimpleName(), false);
                    break;
                }
                context.getBean(CalculatorController.class).acceptInputLine(inputLine);
            }
        }
        context.close();
    }

    public static void printAndLog(String message, String sender, boolean isError) {
        message = String.format("%s: %s", sender, message);
        if (isError) {
            System.err.printf("ERROR: %s%n", message);
            log.error(message);
        } else {
            System.out.println(message);
            log.info(message);
        }
    }
}
