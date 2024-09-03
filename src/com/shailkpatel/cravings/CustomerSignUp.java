package com.shailkpatel.cravings;

import com.shailkpatel.cravings.customer.LoginCustomer;
import com.shailkpatel.cravings.customer.RegisterCustomer;
import com.shailkpatel.cravings.util.InputValidator;

public class CustomerSignUp {

    private static final InputValidator iv = new InputValidator();

    public static void main(String[] args) {
        int choice;

        while (true) {
            System.out.println();
            System.out.println("Welcome! Please choose an option:");
            System.out.println("1. Register as a new customer");
            System.out.println("2. Login as an existing customer");
            System.out.println("3. Exit");

            choice = iv.getIntInput("Enter 1, 2, or 3: ", 1, 3);
            System.out.println();

            if (choice == 1) {
                RegisterCustomer registerCustomer = new RegisterCustomer();
                registerCustomer.register();
                return;
            } else if (choice == 2) {
                LoginCustomer loginCustomer = new LoginCustomer();
                loginCustomer.login();
                return;
            } else if (choice == 3) {
                System.out.println("Exiting the Customer Sign-Up menu.");
                return; // returns to main page
            }
        }
    }
}
