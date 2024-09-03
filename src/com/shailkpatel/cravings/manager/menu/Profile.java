package com.shailkpatel.cravings.manager.menu;

import java.sql.SQLException;

import com.shailkpatel.cravings.db_connection.DBConnectorManager;
import com.shailkpatel.cravings.db_connection.DBConnectorRestaurant;
import com.shailkpatel.cravings.manager.LoginManager;
import com.shailkpatel.cravings.manager.Manager;
import com.shailkpatel.cravings.manager.ManagerMenu;
import com.shailkpatel.cravings.model.Restaurant;
import com.shailkpatel.cravings.util.InputValidator;

public class Profile {
    private final InputValidator iv = new InputValidator();
    private Manager manager;

    public void profileMenu(Manager manager) {
        this.manager = manager;

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
        System.out.println("Name: " + manager.getName());
        System.out.println("Phone Number: " + manager.getPhoneNumber());
        System.out.println("Email: " + manager.getEmail());
        System.out.println();
        System.out.println("Restaurant: ");
        DBConnectorRestaurant dbConnector;
        try {
            dbConnector = new DBConnectorRestaurant();
            Restaurant restaurant = dbConnector.getRestaurantByID(manager.getRestaurantID());
            System.out.println("Restaurant ID: " + restaurant.getRestaurantId());
            System.out.println("Restaurant Name: " + restaurant.getRestaurantName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        DBConnectorManager dbConnector = new DBConnectorManager();
        while (true) {
            System.out.println();
            System.out.println("Update Profile:");
            System.out.println("1. Update Name");
            System.out.println("2. Update Phone Number");
            System.out.println("3. Update Email");
            System.out.println("4. Update Password");
            System.out.println("5. Change Restaurant");
            System.out.println("6. Delete Account");
            System.out.println("7. Update Favorite Food");
            System.out.println("8. Back to Profile Menu");

            int choice = iv.getIntInput("Enter your choice (1-8): ", 1, 8);

            try {
                switch (choice) {
                    case 1:
                        System.out.println();
                        String newName = iv.getStringInput("Enter your new name: ", 32);
                        manager.setName(newName);
                        dbConnector.updateManagerName(manager.getManagerId(), newName);
                        System.out.println("Name updated successfully.");
                        break;
                    case 2:
                        System.out.println();
                        String newPhoneNumber = iv.setValidPhoneNumber();
                        while (!iv.isUniqueManagerPhoneNumber(newPhoneNumber)) {
                            System.out.println("This Phone Number is already in use.");
                            String response = iv.getStringInput("Would you like to login instead? (yes/no)", 3);
                            if (response.equalsIgnoreCase("yes")) {
                                LoginManager loginManager = new LoginManager();
                                loginManager.login();
                                return;
                            }
                            newPhoneNumber = iv.setValidPhoneNumber();
                        }
                        manager.setPhoneNumber(newPhoneNumber);
                        dbConnector.updateManagerPhone(manager.getManagerId(), newPhoneNumber);
                        System.out.println("Phone number updated successfully.");
                        break;
                    case 3:
                        System.out.println();
                        String newEmail = iv.setValidEmail();
                        while (!iv.isUniqueManagerEmail(newEmail)) {
                            System.out.println("This email is already in use.");
                            String response = iv.getStringInput("Would you like to login instead? (yes/no)", 3);
                            if (response.equalsIgnoreCase("yes")) {
                                LoginManager loginManager = new LoginManager();
                                loginManager.login();
                                return;
                            }
                            newEmail = iv.setValidEmail();
                        }
                        manager.setEmail(newEmail);
                        dbConnector.updateManagerEmail(manager.getManagerId(), newEmail);
                        System.out.println("Email updated successfully.");
                        break;
                    case 4:
                        System.out.println();
                        String newPassword = iv.setValidPassword();
                        String encryptedPassword = iv.encryptPassword(newPassword);
                        manager.setPassword(encryptedPassword);
                        dbConnector.updateManagerPassword(manager.getManagerId(), encryptedPassword);
                        System.out.println("Password updated successfully.");
                        break;
                    case 5:
                        DBConnectorRestaurant db = new DBConnectorRestaurant();

                        System.out.println();
                        System.out.println(" Please choose an option: ");
                        System.out.println("1. Connect with an existing Restaurant");
                        System.out.println("2. Register a new Restaurant");
                        int newChoice = iv.getIntInput("Enter your choice: ", 1, 2);
                        if (newChoice == 1) {
                            System.out.println();
                            int newRestaurantID = iv.getRestaurantID();
                            String rCode = iv.getStringInput("Enter Restaurant code: ", 8);

                            Restaurant newRestaurant = db.getRestaurantByIdAndCode(newRestaurantID, rCode);
                            if (!newRestaurant.getAcceptingOrders()) {
                                System.out.println("This restaurant was set to delete soon");
                                System.out.println("Since you connected to it the deletion has been cancelled!");
                                db.updateRestaurantIsActive(newRestaurantID, true);
                            }
                            if (newRestaurant != null) {
                                manager.setRestaurantID(newRestaurantID);
                                dbConnector.updateManagerRestaurantId(manager.getManagerId(), newRestaurantID);
                                System.out.println("Restaurant changed to: " + newRestaurant.getRestaurantName());
                            } else {
                                System.out.println("Invalid restaurant ID or Code");
                            }
                        } else {
                            String restaurantName = iv.getStringInput("Enter Restaurant Name: ", 32);
                            String restaurantAddress = iv.getStringInput("Enter Restaurant Address: ", 64);
                            int themeID = iv.getThemeID();
                            String restaurantCode = iv.generateRandomRCode();

                            // Create Restaurant object
                            Restaurant restaurant = new Restaurant();
                            restaurant.setRestaurantName(restaurantName);
                            restaurant.setRestaurantAddress(restaurantAddress);
                            restaurant.setRestaurantThemeID(themeID);
                            restaurant.setrCode(restaurantCode);

                            // Save restaurant to database
                            int restaurantId = db.saveRestaurantDB(restaurant);
                            manager.setRestaurantID(restaurantId);
                            dbConnector.updateManagerRestaurantId(manager.getManagerId(), restaurantId);
                            System.out.println();
                            System.out.println("Restaurant registration successful!");
                            System.out.println("Your Restaurant Code is: " + restaurantCode);

                        }

                        break;
                    case 6:
                        System.out.println();
                        dbConnector.updateManagerActiveStatus(manager.getManagerId(), false);
                        System.out.println();
                        String response = iv.getStringInput("Are you sure you want to delete Account? (yes/no)", 3);
                        if (response.equalsIgnoreCase("yes")) {
                            System.out.println("Your Account will be deleted in 30 Days!");
                            System.out.println("If you want to cancel deletion you can sign in within 30 days");
                            System.out.println();
                            ManagerMenu mm = new ManagerMenu();
                            mm.logout(manager);
                            System.out.println("Closing App!");
                            System.exit(0);
                        }
                        System.out.println("Operation Cancelled!");

                        break;
                    case 7:
                        System.out.println();
                        String newFavoriteFood = iv.getStringInput("Enter your favorite food: ", 50).toLowerCase();
                        manager.setFavoriteFood(newFavoriteFood);
                        dbConnector.updateManagerFavoriteFood(manager.getManagerId(), newFavoriteFood);
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
