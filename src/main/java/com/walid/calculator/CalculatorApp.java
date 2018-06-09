package com.walid.calculator;

import com.walid.calculator.view.CalculatorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Scanner;

@Slf4j
@Configuration
@ImportResource(locations = "file:calculatorAppContext.xml")
public class CalculatorApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(CalculatorApp.class);

        printAndLog("Welcome to Reverse Calculator. When finished, enter 'q' to quit.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(">> ");
                String inputLine = scanner.nextLine();
                if ("q".equalsIgnoreCase(inputLine.trim())) {
                    log.info("User quit successfully.");
                    printAndLog("Bye!");
                    break;
                }
                context.getBean(CalculatorController.class).acceptInputLine(inputLine);
            }
        }
        context.close();
    }

    public static void printAndLog(String message) {
        System.out.println(message);
        log.info(message);
    }
}
