package com.shailkpatel.cravings.manager.menu;

import java.sql.SQLException;
import com.shailkpatel.cravings.db_connection.DBConnectorManager;
import com.shailkpatel.cravings.db_connection.DBConnectorRestaurant;
import com.shailkpatel.cravings.manager.Manager;
import com.shailkpatel.cravings.util.InputValidator;

public class Revenue {

    private final InputValidator iv = new InputValidator();

    public void menu(Manager manager) {
        int choice;

        do {
            System.out.println();
            System.out.println("Revenue Menu:");
            System.out.println("1. View Total Earnings");
            System.out.println("2. View Orders Cooked");
            System.out.println("3. Exit");

            choice = iv.getIntInput("Enter from 1 to 3: ", 1, 3);

            switch (choice) {
                case 1:
                    viewTotalEarnings(manager);
                    break;
                case 2:
                    viewOrdersCooked(manager);
                    break;
                case 3:
                    System.out.println("Exiting Revenue Menu.");
                    break;
            }
        } while (choice != 3); // Updated to match menu options correctly
    }

    private void viewTotalEarnings(Manager manager) {
        try {DBConnectorRestaurant db = new DBConnectorRestaurant();
            int revenue = db.getRevenueFromID(manager.getRestaurantID());
            System.out.println("Total Revenue of restaurant: " + revenue);
        } catch (SQLException e) {
            System.err.println("Error retrieving total earnings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewOrdersCooked(Manager manager) {
        try {DBConnectorManager db = new DBConnectorManager();
            db.viewOrders(manager);
        } catch (SQLException e) {
            System.err.println("Error retrieving orders cooked: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
