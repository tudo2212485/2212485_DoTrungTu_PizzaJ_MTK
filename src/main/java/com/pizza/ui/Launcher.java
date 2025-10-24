package com.pizza.ui;

/**
 * Launcher class to run JavaFX application from executable JAR
 * This class doesn't extend Application to avoid JavaFX runtime check
 */
public class Launcher {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}

