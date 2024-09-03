package com.shailkpatel.cravings.manager.menu;

import java.sql.SQLException;

import com.shailkpatel.cravings.db_connection.DBConnectorDish;
import com.shailkpatel.cravings.db_connection.DBConnectorRestaurant;
import com.shailkpatel.cravings.manager.Manager;
import com.shailkpatel.cravings.model.Dish;
import com.shailkpatel.cravings.model.Restaurant;
import com.shailkpatel.cravings.util.ArrayList;
import com.shailkpatel.cravings.util.InputValidator;

public class UpdateRestaurant {
    private final InputValidator iv = new InputValidator();
    private Manager manager;

    public void updateMenu(Manager manager) {
        this.manager = manager;

        while (true) {
            System.out.println();
            System.out.println("Restaurant Update Menu:");
            System.out.println("1. View Restaurant");
            System.out.println("2. Update Restaurant");
            System.out.println("3. Dish Menu");
            System.out.println("4. Return to Main Menu");

            int choice = iv.getIntInput("Enter your choice (1-4): ", 1, 4);

            switch (choice) {
                case 1:
                    viewRestaurant();
                    break;
                case 2:
                    updateRestaurant();
                    break;
                case 3:
                    dishMenu();
                    break;
                case 4:
                    System.out.println("Returning to the main menu...");
                    return;
            }
        }
    }

    private void viewRestaurant() {
        System.out.println();
        try {
            DBConnectorRestaurant dbConnector = new DBConnectorRestaurant();
            Restaurant restaurant = dbConnector.getRestaurantByID(manager.getRestaurantID());

            if (restaurant != null) {
                System.out.println("Restaurant ID: " + restaurant.getRestaurantId());
                System.out.println("Restaurant Name: " + restaurant.getRestaurantName());
                System.out.println("Restaurant Address: " + restaurant.getRestaurantAddress());
                System.out.println("Restaurant Code: " + restaurant.getrCode());
                System.out.println("Rating: " + restaurant.getRating());
                System.out.println("Theme: " + dbConnector.getThemeFromThemeID(restaurant.getThemeID()));
                System.out.println();
                dbConnector.printRestaurantMenu(restaurant.getRestaurantId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateRestaurant() {
        System.out.println();
        System.out.println("Update Restaurant:");
        System.out.println("1. Update Name");
        System.out.println("2. Update Address");
        System.out.println("3. Regenerate Restaurant Code");
        System.out.println("4. Change Theme");
        System.out.println("5. Start Accepting Orders");
        System.out.println("6. Stop Accepting Orders");
        System.out.println("7. Go Back");

        int choice = iv.getIntInput("Enter your choice (1-7): ", 1, 7);

        try {
            DBConnectorRestaurant dbConnector = new DBConnectorRestaurant();
            Restaurant restaurant = dbConnector.getRestaurantByID(manager.getRestaurantID());

            if (restaurant == null) {
                System.out.println("Restaurant not found.");
                return;
            }

            switch (choice) {
                case 1:
                    System.out.println();
                    String newName = iv.getStringInput("Enter new restaurant name: ", 30);
                    dbConnector.updateRestaurantName(manager.getRestaurantID(), newName);
                    System.out.println("Restaurant name updated successfully.");
                    break;
                case 2:
                    System.out.println();
                    String newAddress = iv.getStringInput("Enter new restaurant address: ", 50);
                    dbConnector.updateRestaurantAddress(manager.getRestaurantID(), newAddress);
                    System.out.println("Restaurant address updated successfully.");
                    break;
                case 3:
                    System.out.println();
                    String newCode = iv.generateRandomRCode();
                    dbConnector.updateRCode(manager.getRestaurantID(), newCode);
                    System.out.println("Restaurant code regenerated successfully.");
                    System.out.println("New RCode is: " + newCode);
                    break;
                case 4:
                    System.out.println();
                    int newThemeID = iv.getThemeID();
                    dbConnector.updateTheme(manager.getRestaurantID(), newThemeID);
                    System.out.println("Restaurant theme changed successfully.");
                    break;
                case 5:
                    System.out.println();
                    boolean startSuccess = dbConnector.startAcceptingOrders(manager.getRestaurantID());
                    if (startSuccess) {
                        System.out.println("Started accepting orders successfully.");
                    } else {
                        System.out.println("You cant start accepting orders, you have no dishes!");
                    }
                    break;
                case 6:
                    System.out.println();
                    boolean stopSuccess = dbConnector.stopAcceptingOrders(manager.getRestaurantID());
                    if (stopSuccess) {
                        System.out.println("Stopped accepting orders successfully.");
                    } else {
                        System.out.println("Failed to stop accepting orders.");
                    }
                    break;
                case 7:
                    System.out.println();
                    System.out.println("Returning to the restaurant update menu...");
                    return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dishMenu() {
        System.out.println();
        System.out.println("Dish Menu:");
        System.out.println("1. Add Dish");
        System.out.println("2. Delete Dish");
        System.out.println("3. Update Dish");
        System.out.println("4. Go Back");

        int choice = iv.getIntInput("Enter your choice (1-4): ", 1, 4);

        try {
            DBConnectorDish dbConnector = new DBConnectorDish();
            DBConnectorRestaurant db = new DBConnectorRestaurant();

            switch (choice) {
                case 1:
                    System.out.println();
                    String dishName = iv.getStringInput("Enter Dish Name: ", 32);
                    int courseID = iv.getCourseTags();
                    int dishPrice = iv.getIntInput("Enter Dish Price: ", 20, 10000);
                    int dishPreparationTime = iv.getIntInput("Enter Dish Preparation Time (in minutes): ", 1, 120);
                    ArrayList<String> dishCuisineTags = iv.getCuisineTags();
                    ArrayList<String> dishDietTags = iv.getDietTags();
                    System.out.println();

                    Dish newDish = new Dish(dishName, courseID, dishPrice, dishPreparationTime, dishCuisineTags,
                            dishDietTags);

                    int id = dbConnector.addDishToDB(newDish);
                    newDish.setId(id);
                    db.addDishToRestaurantDB(manager.getRestaurantID(), newDish);
                    System.out.println("Dish added successfully.");
                    break;

                case 2:
                    System.out.println();
                    int deleteDishId = iv.getDishRestaurantId(manager.getRestaurantID());
                    db.deleteDishFromRestaurantDB(manager.getRestaurantID(), deleteDishId);
                    System.out.println("Dish deleted successfully.");
                    break;

                case 3:
                    System.out.println();
                    int updateDishId = iv.getDishRestaurantId(manager.getRestaurantID());
                    if (updateDishId == -1) {
                        return;
                    }
                    updateDishMenu(updateDishId);
                    System.out.println("Dish updated successfully.");
                    break;

                case 4:
                    System.out.println();
                    System.out.println("Returning to the restaurant update menu...");
                    return;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDishMenu(int dishId) throws SQLException {
        DBConnectorDish dbConnector = new DBConnectorDish();
        boolean done = false;

        while (!done) {
            System.out.println("Update Dish Menu:");
            System.out.println("1. Update Name");
            System.out.println("2. Update Price");
            System.out.println("3. Update Preparation Time");
            System.out.println("4. Update Course ID");
            System.out.println("5. Update Cuisine Tags");
            System.out.println("6. Update Diet Tags");
            System.out.println("7. Go Back");

            int choice = iv.getIntInput("Enter your choice (1-7): ", 1, 7);

            switch (choice) {
                case 1:
                    String newName = iv.getStringInput("Enter new Dish Name: ", 32);
                    dbConnector.updateDishName(dishId, newName);
                    System.out.println("Dish name updated successfully.");
                    break;
                case 2:
                    int newPrice = iv.getIntInput("Enter new Dish Price: ", 20, 10000);
                    dbConnector.updateDishPrice(dishId, newPrice);
                    System.out.println("Dish price updated successfully.");
                    break;
                case 3:
                    int newPreparationTime = iv.getIntInput("Enter new Preparation Time (in minutes): ", 1, 120);
                    dbConnector.updateDishPreparationTime(dishId, newPreparationTime);
                    System.out.println("Dish preparation time updated successfully.");
                    break;
                case 4:
                    int newCourseId = iv.getCourseTags();
                    dbConnector.updateDishCourse(dishId, newCourseId);
                    System.out.println("Dish course ID updated successfully.");
                    break;
                case 5:
                    ArrayList<String> newCuisineTags = iv.getCuisineTags();
                    dbConnector.updateDishCuisineTagsInDB(dishId, newCuisineTags);
                    System.out.println("Dish cuisine tags updated successfully.");
                    break;
                case 6:
                    ArrayList<String> newDietTags = iv.getDietTags();
                    dbConnector.updateDishDietTagsInDB(dishId, newDietTags);
                    System.out.println("Dish diet tags updated successfully.");
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

}
