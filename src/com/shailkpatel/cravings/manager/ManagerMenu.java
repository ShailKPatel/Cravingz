package com.shailkpatel.cravings.manager;

import java.sql.SQLException;

import com.shailkpatel.cravings.db_connection.DBConnectorManager;
import com.shailkpatel.cravings.manager.menu.OrdersCooking;
import com.shailkpatel.cravings.manager.menu.Profile;
import com.shailkpatel.cravings.manager.menu.Revenue;
import com.shailkpatel.cravings.manager.menu.UpdateRestaurant;
import com.shailkpatel.cravings.util.InputValidator;

public class ManagerMenu {
    private static final InputValidator iv = new InputValidator();

    public void menu(Manager manager) {

        int choice;

        while (true) {
            System.out.println();
            System.out.println("Manager Menu:");
            System.out.println("1. View Orders in Cooking");
            System.out.println("2. Update Restaurant Information");
            System.out.println("3. View Revenue");
            System.out.println("4. View Profile");
            System.out.println("5. Logout");

            choice = iv.getIntInput("Enter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    OrdersCooking oc = new OrdersCooking();
                    oc.printOrdersCooking(manager);
                    break;
                case 2:
                    UpdateRestaurant ur = new UpdateRestaurant();
                    ur.updateMenu(manager);
                    break;
                case 3:
                    Revenue revenue = new Revenue();
                    revenue.menu(manager);
                    break;
                case 4:
                    Profile profile = new Profile();
                    profile.profileMenu(manager);
                    break;
                case 5:
                    logout(manager);
                    return;
            }
        }
    }

    public void logout(Manager manager) {
        System.out.println();
        System.out.println("Logging out");
        DBConnectorManager dbConnectorManager = new DBConnectorManager();
        try {
            dbConnectorManager.updateManagerOnlineStatus(manager.getManagerId(), false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
