package com.pizza.ui.controllers;

import com.pizza.ui.MainApp;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller for the Home view.
 */
public class HomeController {
    
    @FXML
    private void handleMenuButton() {
        try {
            MainApp.showMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCheckoutButton() {
        try {
            MainApp.showCart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}






