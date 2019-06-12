package com.automation.logger;

/**
 * @author Shrikrushna Atkalikar (atkalikar@optymyze.com)
 */
public class ConsoleLogger {

    private static boolean isLoggingEnabled = false;

    public static void log(String message) {
        if (isLoggingEnabled) {
            System.out.println(message);
        }
    }

    public void enableLogging() {
        isLoggingEnabled = true;
    }
}