package com.comp2042.util;

/**
 * Simple logging utility to replace System.err.println throughout the application.
 * Provides consistent error logging with the option to enhance later with proper logging framework.
 */
public final class Logger {
    
    private Logger() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Logs an error message.
     * @param message The error message to log
     */
    public static void error(String message) {
        System.err.println("[ERROR] " + message);
    }
    
    /**
     * Logs an error message with an exception.
     * @param message The error message to log
     * @param throwable The exception that occurred
     */
    public static void error(String message, Throwable throwable) {
        System.err.println("[ERROR] " + message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }
    
    /**
     * Logs a warning message.
     * @param message The warning message to log
     */
    public static void warn(String message) {
        System.err.println("[WARN] " + message);
    }
    
    /**
     * Logs an info message.
     * @param message The info message to log
     */
    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }
}


