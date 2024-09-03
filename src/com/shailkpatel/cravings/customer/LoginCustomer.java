package com.shailkpatel.cravings.customer;

import com.shailkpatel.cravings.db_connection.DBConnectorCustomer;
import com.shailkpatel.cravings.util.InputValidator;

import java.sql.SQLException;

public class LoginCustomer {

    private static final InputValidator iv = new InputValidator();
    private static final int MAX_ATTEMPTS = 3;
    private int attempts = 0;

    public void login() {
        int phoneAttempt = 0;

        String phoneNumber = iv.setValidPhoneNumber();
        while (iv.isUniqueCustomerPhoneNumber(phoneNumber)) {
            phoneAttempt++;
            System.out.println("This phone number is not registered!");

            if (phoneAttempt == 1) {
                String response = iv.getStringInput("Did you want to login with your email instead (yes/no)?", 3);
                if (response.equalsIgnoreCase("yes")) {
                    try {
                        forgotPhoneNumber();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            if (phoneAttempt == 2 && handleRegistrationPrompt()) {
                return;
            }
            if (phoneAttempt > 5) {
                System.out.println("You have made too many attempts.");
                return;
            }

            phoneNumber = iv.setValidPhoneNumber();
        }

        while (attempts < MAX_ATTEMPTS) {
            String password = iv.getStringInput("Enter your password: ", 30);

            if (!iv.isValidPassword(password)) {
                System.out.println("Incorrect password.");
                attempts++;
            } else {
                String encryptedPassword = iv.encryptPassword(password);

                try {
                    DBConnectorCustomer dbConnector = new DBConnectorCustomer();
                    boolean isValid = dbConnector.validateCustomer(phoneNumber, encryptedPassword);

                    if (isValid) {
                        processSuccessfulLogin(phoneNumber);
                        return;
                    } else {
                        System.out.println("Incorrect password.");
                        attempts++;
                    }
                } catch (SQLException e) {
                    System.out.println("Error while logging in: " + e.getMessage());
                    return;
                }
            }

            if (handleForgotPasswordPrompt(phoneNumber)) {
                return;
            }
        }

        handleMaxAttemptsExceeded();
    }

    private boolean handleRegistrationPrompt() {
        String response = iv.getStringInput("Would you like to Register instead? (yes/no)", 3);
        if (response.equalsIgnoreCase("yes")) {
            new RegisterCustomer().register();
            return true;
        }
        return false;
    }

    private boolean handleForgotPasswordPrompt(String phoneNumber) {
        String forgotResponse = iv.getStringInput("Did you forget your password? (yes/no): ", 3);
        if ("yes".equalsIgnoreCase(forgotResponse)) {
            forgotPassword(phoneNumber);
            return true;
        }
        return false;
    }

    private void processSuccessfulLogin(String phoneNumber) throws SQLException {
        System.out.println("Login successful!");
        DBConnectorCustomer dbConnector = new DBConnectorCustomer();

        Customer customer = dbConnector.getCustomerByPhoneNumber(phoneNumber);
        CustomerMenu cm = new CustomerMenu();

        if (dbConnector.getCustomerOnlineStatus(customer.getCustomerId())) {
            System.out.println("There was an error in your last session!");
            System.out.println("Any unsaved work might have been deleted!");
        } else {
            dbConnector.loginCustomer(customer.getCustomerId());
        }

        if (!dbConnector.getCustomerActiveStatus(customer.getCustomerId())) {
            System.out.println("Your account was set to be deleted soon!");
            System.out.println("Since you logged in, deletion has been cancelled!");
            dbConnector.updateCustomerActiveStatus(customer.getCustomerId(), true);
        }

        cm.menu(customer);
    }

    private void handleMaxAttemptsExceeded() {
        System.out.println("Too many failed attempts.");
        System.out.println("This device is blocked for 3 hours.");
        try {
            Thread.sleep(3 * 60 * 60 * 1000); // Sleep for 3 hours
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void forgotPhoneNumber() throws SQLException {
        int emailAttempt = 0;

        String email = iv.setValidEmail();
        while (iv.isUniqueCustomerEmail(email)) {
            emailAttempt++;
            System.out.println("This email is not registered!");

            if (emailAttempt == 1) {
                String response = iv.getStringInput("Did you want to login with your phone number instead (yes/no)?",
                        3);
                if (response.equalsIgnoreCase("yes")) {
                    login();
                    return;
                }
            }
            if (emailAttempt == 2 && handleRegistrationPrompt()) {
                return;
            }
            if (emailAttempt > 5) {
                System.out.println("You have made too many attempts.");
                return;
            }

            email = iv.setValidEmail();
        }
        DBConnectorCustomer dbConnector = new DBConnectorCustomer();
        String phoneNumber = dbConnector.getPhoneFromEmail(email);

        while (attempts < MAX_ATTEMPTS) {
            String password = iv.getStringInput("Enter your password: ", 30);

            if (!iv.isValidPassword(password)) {
                System.out.println("Invalid password.");
                attempts++;
            } else {
                String encryptedPassword = iv.encryptPassword(password);

                try {
                    boolean found = dbConnector.validateCustomer(phoneNumber, encryptedPassword);

                    if (found) {
                        System.out.println("Your phone number is: " + phoneNumber);
                        processSuccessfulLogin(phoneNumber);
                        return;
                    } else {
                        System.out.println("Incorrect password.");
                        attempts++;
                    }
                } catch (SQLException e) {
                    System.out.println("Error while recovering phone number: " + e.getMessage());
                    return;
                }
            }
            if (handleForgotPasswordPrompt(phoneNumber)) {
                return;
            }
        }

        handleMaxAttemptsExceeded();
    }

    private void forgotPassword(String phoneNumber) {
        String favoriteFood = iv.getStringInput("What is your favorite food? ", 50);

        try {
            DBConnectorCustomer dbConnector = new DBConnectorCustomer();
            boolean isValid = dbConnector.validateCustomerFood(phoneNumber, favoriteFood);

            if (isValid) {
                System.out.println("Password recovery successful.");
                System.out.println("Change your password as soon as possible!");
                processSuccessfulLogin(phoneNumber);
            } else {
                System.out.println("Attempt failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error while recovering password: " + e.getMessage());
        }
    }
}
