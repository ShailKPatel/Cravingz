package com.shailkpatel.cravings.customer;

import com.shailkpatel.cravings.db_connection.DBConnectorCustomer;
import com.shailkpatel.cravings.util.InputValidator;

import java.sql.SQLException;

public class RegisterCustomer {

    private static final InputValidator inputValidator = new InputValidator();

    public void register() {
        String name = inputValidator.getStringInput("Enter your name: ", 32);

        String phoneNumber = inputValidator.setValidPhoneNumber();
        while (!inputValidator.isUniqueCustomerPhoneNumber(phoneNumber)) {
            System.out.println("This Phone Number is already in use.");
            System.out.println("Would you like to login instead?(yes/no): ");
            String response = inputValidator.getStringInput("", 3);
            if (response.equalsIgnoreCase("yes")) {
                LoginCustomer loginCustomer = new LoginCustomer();
                loginCustomer.login();
                return;
            }
            phoneNumber = inputValidator.setValidPhoneNumber();
        }

        String email = inputValidator.setValidEmail();
        while (!inputValidator.isUniqueCustomerEmail(email)) {
            System.out.println("This email is already in use.");
            String response = inputValidator.getStringInput("Would you like to login instead?(yes/no): ", 3);
            if (response.equalsIgnoreCase("yes")) {
                LoginCustomer loginCustomer = new LoginCustomer();
                loginCustomer.login();
                return;
            }
            email = inputValidator.setValidEmail();
        }

        String password = inputValidator.setValidPassword();
        String[] addresses = inputValidator.getAddresses();
        System.out.println();
        String favoriteFood = inputValidator.getStringInput("Enter your favorite food: ", 50).toLowerCase();

        String encryptedPassword = inputValidator.encryptPassword(password);

        // Creating Customer object
        Customer customer = new Customer(name, phoneNumber, email, encryptedPassword, addresses, favoriteFood);

        try {
            DBConnectorCustomer dbConnector = new DBConnectorCustomer();
            dbConnector.saveCustomerDB(customer);
            // System.out.println("Customer registration successful!");
            CustomerMenu cm = new CustomerMenu();
            cm.menu(customer);
        } catch (SQLException e) {
            System.out.println("Error while registering customer");
            System.out.println("Please try again later!");
        }
    }
}
