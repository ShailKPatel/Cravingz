package com.shailkpatel.cravings.customer.menu;

import com.shailkpatel.cravings.customer.Customer;
import com.shailkpatel.cravings.db_connection.DBConnectorCustomer;
import com.shailkpatel.cravings.db_connection.DBConnectorDish;
import com.shailkpatel.cravings.db_connection.DBConnectorRestaurant;
import com.shailkpatel.cravings.model.Dish;
import com.shailkpatel.cravings.util.InputValidator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class RestaurantsNearMe {
    InputValidator iv = new InputValidator();
    DBConnectorRestaurant dbConnectorRestaurant;

    public void printAllRestaurant(Customer customer) {
        System.out.println();
        try {
            dbConnectorRestaurant = new DBConnectorRestaurant();
            dbConnectorRestaurant.printAllRestaurants();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int choice;

        while (true) {
            System.out.println();
            System.out.println("Please choose an option:");
            System.out.println("1. Pick a restaurant to order from");
            System.out.println("2. Return to main menu");

            choice = iv.getIntInput("Enter 1 or 2: ", 1, 2);

            if (choice == 1) {
                System.out.println();
                int restaurantID = iv.getRestaurantID();
                printMenu(customer, restaurantID);
                break;
            } else {
                System.out.println("Returning to main menu");
                return;
            }
        }
    }

    private void printMenu(Customer customer, int restaurantID) {
        try {
            dbConnectorRestaurant.printRestaurantMenu(restaurantID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.println();
            System.out.println("Please choose an option:");
            System.out.println("1. Order a dish");
            System.out.println("2. Add a dish to cart");
            System.out.println("3. View other restaurants");

            int choice = iv.getIntInput("Enter 1, 2, or 3: ", 1, 3);

            switch (choice) {
                case 1:
                    int dishID = iv.getDishRestaurantId(restaurantID);
                    orderDish(customer, dishID, restaurantID);
                    return;
                case 2:
                    dishID = iv.getDishRestaurantId(restaurantID);
                    addToCart(customer, dishID);
                    break;
                case 3:
                    System.out.println("Returning to restaurant selection");
                    return;
            }
        }
    }

    public void orderDish(Customer customer, int dishID, int restaurantID) {
        System.out.println("Where would you like to get the food delivered?");

        String[] addresses = customer.getAddresses();
        int selectedAddressIndex = -1;

        for (int i = 0; i < addresses.length; i++) {
            if (!addresses[i].equalsIgnoreCase("Not Entered")) {
                System.out.println((i + 1) + ". " + addresses[i]);
            }
        }

        while (selectedAddressIndex == -1) {
            int choice = iv.getIntInput("Enter the number of the address where you want the food delivered: ", 1,
                    addresses.length);
            if (!addresses[choice - 1].equalsIgnoreCase("Not Entered")) {
                selectedAddressIndex = choice - 1;
            } else {
                System.out.println("Invalid selection. Please choose a valid address.");
            }
        }

        try {
            DBConnectorCustomer db = new DBConnectorCustomer();
            Connection conn = DBConnectorCustomer.getConnection();
            int addressId = selectedAddressIndex + 1; // Address index is zero-based, SQL expects one-based
            int quantity = 1; // Assuming quantity is 1

            // Calling the InsertIntoOrdersCooking procedure
            String insertOrderProcedure = "{CALL InsertIntoOrdersCooking(?, ?, ?, ?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(insertOrderProcedure)) {
                stmt.setInt(1, customer.getCustomerId());
                stmt.setInt(2, restaurantID);
                stmt.setInt(3, addressId);
                stmt.setInt(4, dishID);
                stmt.setInt(5, quantity);
                stmt.registerOutParameter(6, Types.BOOLEAN);
                stmt.registerOutParameter(7, Types.INTEGER);

                stmt.execute();

                boolean success = stmt.getBoolean(6);
                int orderId = stmt.getInt(7);

                if (success) {
                    System.out.println("Your order has been placed successfully with Order ID: " + orderId);
                } else {
                    System.out.println("Failed to place order.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error placing order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addToCart(Customer customer, int dishID) {
        DBConnectorCustomer db = new DBConnectorCustomer();
        try {
            db.addToCart(customer.getCustomerId(), dishID);
            System.out.println("Dish added to cart successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
