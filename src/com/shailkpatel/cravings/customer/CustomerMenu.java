package com.shailkpatel.cravings.customer;

import com.shailkpatel.cravings.util.InputValidator;
import com.shailkpatel.cravings.customer.menu.Profile;
import com.shailkpatel.cravings.customer.menu.RestaurantsNearMe;
import com.shailkpatel.cravings.db_connection.DBConnectorCustomer;

import java.sql.SQLException;

public class CustomerMenu {
    private static final InputValidator iv = new InputValidator();
    private Customer customer;

    public void menu(Customer customer) {
        this.customer = customer;
        int choice;

        while (true) {
            System.out.println();
            System.out.println("Customer Menu:");
            System.out.println("1. View Restaurants");
            System.out.println("2. View Order History");
            System.out.println("3. View Cart");
            System.out.println("4. View Profile");
            System.out.println("5. Logout");

            choice = iv.getIntInput("Enter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    RestaurantsNearMe rn = new RestaurantsNearMe();
                    rn.printAllRestaurant(customer);
                    break;
                case 2:
                    viewOrderHistory();
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    Profile profile = new Profile();
                    profile.profileMenu(customer);
                    break;
                case 5:
                    logout();
                    return;
            }
        }
    }

    private void viewOrderHistory() {
        try {
            DBConnectorCustomer db = new DBConnectorCustomer();
            System.out.println("Orders Currently Being Cooked:");
            db.viewCookingOrders(customer.getCustomerId());

            System.out.println("\nCooked Orders:");
            db.viewCookedOrders(customer.getCustomerId());

        } catch (SQLException e) {
            System.err.println("Error retrieving order history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewCart() {
        try {
            DBConnectorCustomer db = new DBConnectorCustomer();
            System.out.println("Viewing Cart:");
            db.viewCart(customer.getCustomerId());
        } catch (SQLException e) {
            System.err.println("Error retrieving cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void logout() {
        System.out.println();
        System.out.println("Logging out");
        DBConnectorCustomer dbConnectorCustomer = new DBConnectorCustomer();
        try {
            boolean success = dbConnectorCustomer.logoutCustomer(customer.getCustomerId());
            if (success) {
                System.out.println("Logout successful.");
            } else {
                System.out.println("Logout failed.");
            }
        } catch (SQLException e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
