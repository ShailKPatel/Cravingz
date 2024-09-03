package com.shailkpatel.cravings.manager;

import com.shailkpatel.cravings.db_connection.DBConnectorManager;
import com.shailkpatel.cravings.db_connection.DBConnectorRestaurant;
import com.shailkpatel.cravings.model.Restaurant;
import com.shailkpatel.cravings.util.InputValidator;

import java.sql.SQLException;

public class RegisterManager {

    private static final InputValidator inputValidator = new InputValidator();

    public void register() {
        System.out.println();
        String name = inputValidator.getStringInput("Enter your name: ", 32);

        String phoneNumber = inputValidator.setValidPhoneNumber();
        while (!inputValidator.isUniqueManagerPhoneNumber(phoneNumber)) {
            System.out.println("This Phone Number is alreaddy in use.");
            String response = inputValidator.getStringInput("Would you like to login instead? (yes/no): ", 3);
            if (response.equalsIgnoreCase("yes")) {
                LoginManager loginManager = new LoginManager();
                loginManager.login();
                return;
            }
            phoneNumber = inputValidator.setValidPhoneNumber();
        }

        String email = inputValidator.setValidEmail();
        while (!inputValidator.isUniqueManagerEmail(email)) {
            System.out.println("This email is already in use.");
            String response = inputValidator.getStringInput("Would you like to login instead? (yes/no)", 3);
            if (response.equalsIgnoreCase("yes")) {
                LoginManager loginManager = new LoginManager();
                loginManager.login();
                return;
            }
            email = inputValidator.setValidEmail();
        }
        String password = inputValidator.setValidPassword();
        int restaurantID = setRestaurantID();
        if (restaurantID == -1) {
            return;
        }
        String favoriteFood = inputValidator.getStringInput("Enter your favorite food: ", 50);

        String encryptedPassword = inputValidator.encryptPassword(password);

        Manager manager = new Manager(name, phoneNumber, email, encryptedPassword, restaurantID, favoriteFood);

        try {
            DBConnectorManager dbConnector = new DBConnectorManager();
            dbConnector.saveRestaurantManagerDB(manager);
            System.out.println("Manager registration successful!");
            ManagerMenu mn = new ManagerMenu();
            mn.menu(manager);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while registering manager");
            System.out.println("Please try again later!");
        }
    }

    private int setRestaurantID() {
        System.out.println();
        System.out.println(" Please choose an option: ");
        System.out.println("1. Connect with an existing Restaurant");
        System.out.println("2. Register a new Restaurant");
        System.out.println();
        int choice = inputValidator.getIntInput("Enter your choice: ", 1, 2);
        if (choice == 1) {
            return connectRestaurant();
        } else {
            return registerRestaurant();
        }
    }

    private int registerRestaurant() {
        String restaurantName = inputValidator.getStringInput("Enter Restaurant Name: ", 32);
        String restaurantAddress = inputValidator.getStringInput("Enter Restaurant Address: ", 64);
        int themeID = inputValidator.getThemeID();
        String restaurantCode = inputValidator.generateRandomRCode();

        // Create Restaurant object
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName(restaurantName);
        restaurant.setRestaurantAddress(restaurantAddress);
        restaurant.setRestaurantThemeID(themeID);
        restaurant.setrCode(restaurantCode);

        try {
            // Save restaurant to database
            DBConnectorRestaurant dbConnector = new DBConnectorRestaurant();
            int restaurantId = dbConnector.saveRestaurantDB(restaurant);
            System.out.println("Restaurant registration successful!");
            System.out.println("Your Restaurant Code is: " + restaurantCode);
            return restaurantId;
        } catch (SQLException e) {
            System.out.println("Error while registering restaurant: " + e.getMessage());
            return -1;
        }
    }

    private int connectRestaurant() {
        try {
            while (true) {
                DBConnectorRestaurant db = new DBConnectorRestaurant();
                int restaurantID = inputValidator.getRestaurantID();
                String restaurantCode = inputValidator.getStringInput("Enter Restaurant Code: ", 8);
                Restaurant restaurant = db.getRestaurantByIdAndCode(restaurantID, restaurantCode);
                if (restaurant != null) {
                    return restaurant.getRestaurantId();
                }
                System.out.println();
                System.out.println("Invalid Restaurant ID or Code");
                System.out.println();
                String response = inputValidator
                        .getStringInput("Do you want to register a new restaurant instead? (yes/no)", 3);
                System.out.println();
                if (response.equalsIgnoreCase("yes")) {
                    return registerRestaurant();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to restaurant: " + e.getMessage());
            return -1;
        }
    }
}
