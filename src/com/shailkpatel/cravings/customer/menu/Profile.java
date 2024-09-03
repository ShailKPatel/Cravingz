package com.shailkpatel.cravings.customer.menu;

import java.sql.SQLException;

import com.shailkpatel.cravings.db_connection.DBConnectorCustomer;
import com.shailkpatel.cravings.customer.Customer;
import com.shailkpatel.cravings.customer.LoginCustomer;
import com.shailkpatel.cravings.util.InputValidator;

public class Profile {
    private final InputValidator iv = new InputValidator();
    private Customer customer;

    public void profileMenu(Customer customer) {
        this.customer = customer;

        int choice;

        while (true) {
            System.out.println();
            System.out.println("Profile Menu:");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Back to Main Menu");

            choice = iv.getIntInput("Enter your choice (1-3): ", 1, 3);

            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    updateProfile();
                    break;
                case 3:
                    System.out.println("Returning to the main menu...");
                    return;
            }
        }
    }

    private void viewProfile() {
        System.out.println();
        System.out.println("Profile");
        System.out.println("Name: " + customer.getName());
        System.out.println("Phone Number: " + customer.getPhoneNumber());
        System.out.println("Email: " + customer.getEmail());
        System.out.println();
    }

    private void updateProfile() {
        DBConnectorCustomer dbConnector = new DBConnectorCustomer();
        while (true) {
            System.out.println();
            System.out.println("Update Profile:");
            System.out.println("1. Update Name");
            System.out.println("2. Update Phone Number");
            System.out.println("3. Update Email");
            System.out.println("4. Update Password");
            System.out.println("5. Add Address");
            System.out.println("6. Delete Account");
            System.out.println("7. Update Favorite Food");
            System.out.println("8. Back to Profile Menu");

            int choice = iv.getIntInput("Enter your choice (1-8): ", 1, 8);

            try {
                switch (choice) {
                    case 1:
                        System.out.println();
                        String newName = iv.getStringInput("Enter your new name: ", 30);
                        customer.setName(newName);
                        dbConnector.updateCustomerName(customer.getCustomerId(), newName);
                        System.out.println("Name updated successfully.");
                        break;
                    case 2:
                        System.out.println();
                        String newPhoneNumber = iv.setValidPhoneNumber();
                        while (!iv.isUniqueCustomerPhoneNumber(newPhoneNumber)) {
                            System.out.println("This Phone Number is already in use.");
                            String response = iv.getStringInput("Would you like to login instead? (yes/no)", 3);
                            if (response.equalsIgnoreCase("yes")) {
                                LoginCustomer loginCustomer = new LoginCustomer();
                                loginCustomer.login();
                                return;
                            }
                            newPhoneNumber = iv.setValidPhoneNumber();
                        }
                        customer.setPhoneNumber(newPhoneNumber);
                        dbConnector.updateCustomerPhoneNumber(customer.getCustomerId(), newPhoneNumber);
                        System.out.println("Phone number updated successfully.");
                        break;
                    case 3:
                        System.out.println();
                        String newEmail = iv.setValidEmail();
                        while (!iv.isUniqueCustomerEmail(newEmail)) {
                            System.out.println("This email is already in use.");
                            String response = iv.getStringInput("Would you like to login instead? (yes/no)", 3);
                            if (response.equalsIgnoreCase("yes")) {
                                LoginCustomer loginCustomer = new LoginCustomer();
                                loginCustomer.login();
                                return;
                            }
                            newEmail = iv.setValidEmail();
                        }
                        customer.setEmail(newEmail);
                        dbConnector.updateCustomerEmail(customer.getCustomerId(), newEmail);
                        System.out.println("Email updated successfully.");
                        break;
                    case 4:
                        System.out.println();
                        String newPassword = iv.setValidPassword();
                        String encryptedPassword = iv.encryptPassword(newPassword);
                        customer.setPassword(encryptedPassword);
                        dbConnector.updateCustomerPassword(customer.getCustomerId(), encryptedPassword);
                        System.out.println("Password updated successfully.");
                        break;
                    case 5:
                        System.out.println();
                        System.out.println("Change all Addresses!");
                        String[] newAddress = iv.getAddresses();
                        dbConnector.updateCustomerAddresses(customer.getCustomerId(), newAddress);
                        System.out.println("Address added successfully.");
                        break;
                    case 6:
                        System.out.println();
                        dbConnector.updateCustomerActiveStatus(customer.getCustomerId(), false);
                        System.out.println();
                        String response = iv.getStringInput("Are you sure you want to delete your account? (yes/no)",
                                3);
                        if (response.equalsIgnoreCase("yes")) {
                            System.out.println("Your account will be deleted in 30 days!");
                            System.out.println("If you want to cancel the deletion, you can sign in within 30 days.");
                            System.exit(0);
                        }
                        System.out.println("Operation cancelled!");
                        break;
                    case 7:
                        System.out.println();
                        String newFavoriteFood = iv.getStringInput("Enter your favorite food: ", 50).toLowerCase();
                        customer.setFavoriteFood(newFavoriteFood);
                        dbConnector.updateCustomerFavoriteFood(customer.getCustomerId(), newFavoriteFood);
                        System.out.println("Favorite food updated successfully.");
                        break;
                    case 8:
                        return; // Back to the Profile Menu
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
