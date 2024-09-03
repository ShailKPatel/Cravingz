package com.shailkpatel.cravings.util;

import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Scanner;
import java.util.Random;
import java.util.regex.Pattern;

import com.shailkpatel.cravings.db_connection.DBConnectorCustomer;
import com.shailkpatel.cravings.db_connection.DBConnectorDish;
import com.shailkpatel.cravings.db_connection.DBConnectorManager;
import com.shailkpatel.cravings.db_connection.DBConnectorRestaurant;

public class InputValidator {

    private static Scanner scanner = new Scanner(System.in);

    public int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String userInput = scanner.nextLine();
                return Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                System.out.println();
            }
        }
    }

    public String getStringInput(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String userInput = scanner.nextLine();

            if (userInput.length() <= maxLength) {
                return userInput;
            } else {
                System.out.println("Invalid input. Please enter a string of " + maxLength + " characters or shorter.");
                System.out.println();
            }
        }
    }

    public String setValidPhoneNumber() {
        String regex = "^[789]\\d{9}$"; // Indian phone number regex
        while (true) {
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();
            if (Pattern.matches(regex, phone)) {
                return phone;
            } else {
                System.out.println("Invalid phone number. Please enter a valid Indian phone number.");
                System.out.println();
            }
        }
    }

    public boolean isUniqueManagerPhoneNumber(String phoneNumber) {
        DBConnectorManager dbConnector = new DBConnectorManager();

        try {
            HashMap<Integer, String> phoneNumberMap = dbConnector.getAllManagerPhoneNumbers();

            if (!phoneNumberMap.containsValue(phoneNumber)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking Phone Number. Please try again later.");
            e.printStackTrace();
        }

        return false;
    }

    public boolean isUniqueCustomerPhoneNumber(String phoneNumber) {

        try {
            DBConnectorCustomer dbConnector = new DBConnectorCustomer();
            HashMap<Integer, String> phoneNumberMap = dbConnector.getAllCustomerPhoneNumbers();

            if (!phoneNumberMap.containsValue(phoneNumber)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking Phone Number. Please try again later.");
            e.printStackTrace();
        }

        return false;
    }

    public boolean isValidPhoneNumber(String phone) {
        String regex = "^[789]\\d{9}$"; // Indian phone number regex
        return Pattern.matches(regex, phone);
    }

    public String setValidEmail() {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        while (true) {
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            if (Pattern.matches(regex, email)) {
                return email;
            } else {
                System.out.println("Invalid email. Please enter a valid email address.");
                System.out.println();
            }
        }
    }

    public boolean isUniqueManagerEmail(String email) {
        DBConnectorManager dbConnector = new DBConnectorManager();

        try {
            HashMap<Integer, String> emailMap = dbConnector.getAllManagerEmails();

            if (!emailMap.containsValue(email)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking email. Please try again later.");
            e.printStackTrace();
        }

        return false;
    }

    public boolean isUniqueCustomerEmail(String email) {

        try {
            DBConnectorCustomer dbConnector = new DBConnectorCustomer();
            HashMap<Integer, String> emailMap = dbConnector.getAllCustomerEmails();

            if (!emailMap.containsValue(email)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking email. Please try again later.");
            e.printStackTrace();
        }

        return false;
    }

    public String setValidPassword() {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        // Password must contain at least one digit, one lowercase, one uppercase
        // letter, and be at least 8 characters long
        while (true) {
            String password = getStringInput("Enter password: ", 30);
            if (Pattern.matches(regex, password)) {
                return password;
            } else {
                System.out.println(
                        "Invalid password. Password must be at least 8 characters long, contain at least one digit, one lowercase letter, and one uppercase letter.");
                System.out.println();
            }
        }
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        // Password must contain at least one digit, one
        // lowercase, one uppercase letter, and be at least 8
        // characters long
        return Pattern.matches(regex, password);
    }

    public String getStringInput32Char(String prompt) {
        while (true) {
            System.out.print(prompt);
            String userInput = scanner.nextLine();

            if (userInput.length() <= 32) {
                return userInput;
            } else {
                System.out.println("Invalid input. Please enter a string of 32 characters or shorter.");
                System.out.println();
            }
        }
    }

    public String getStringInput64Char(String prompt) {
        while (true) {
            System.out.print(prompt);
            String userInput = scanner.nextLine();

            if (userInput.length() <= 64) {
                return userInput;
            } else {
                System.out.println("Invalid input. Please enter a string of 64 characters or shorter.");
                System.out.println();
            }
        }
    }

    public int getIntInput(String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.println("Input must be between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }

    public int getRestaurantID() {
        try {
            DBConnectorRestaurant dbRestaurant = new DBConnectorRestaurant();
            HashSet<Integer> restaurantIds = dbRestaurant.getAllRestaurantIdsDB();

            while (true) {
                int inputId = getIntInput("Enter Restaurant ID: ");

                if (restaurantIds.contains(inputId)) {
                    return inputId;
                } else {
                    System.out.println("Invalid Restaurant ID. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getThemeID() {
        DBConnectorRestaurant db;
        try {
            db = new DBConnectorRestaurant();
            HashMap<Integer, String> themeMap = db.getAllThemesMap();
            System.out.println("Available Restaurant Themes:");
            themeMap.printHashMap();
            while (true) {
                int inputId = getIntInput("Enter Restaurant Theme ID: ", 1, Integer.MAX_VALUE);

                if (themeMap.containsKey(inputId)) {
                    return inputId;
                } else {
                    System.out.println("Invalid Theme ID. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String encryptPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hash computation
            byte[] encodedHash = digest.digest(password.getBytes());

            // Convert byte array into a hex string
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateRandomRCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder rCode = new StringBuilder(8);
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            rCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        return rCode.toString();
    }

    public String[] getAddresses() {
        String[] addresses = new String[5];
        int count = 0;

        do {
            addresses[count] = getStringInput("Enter address: ", 100);
            count++;

            if (count >= 5) {
                break;
            }
            System.out.println("You have added " + count + " addresses.");
            System.out.print("Do you want to stop? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                break;
            }

        } while (count < 5);

        // Fill any remaining slots with "Not Entered"
        for (int i = count; i < addresses.length; i++) {
            addresses[i] = "Not Entered";
        }

        return addresses;
    }

    public ArrayList<String> getCuisineTags() {
        ArrayList<String> cuisineTags = new ArrayList<>();
        try {
            DBConnectorDish dbConnector = new DBConnectorDish();
            HashMap<Integer, String> cuisineMap = dbConnector.getAllCuisinesMapDB();
            System.out.println("Available Cuisines:");
            cuisineMap.printHashMap();

            while (cuisineTags.size() < 3) {
                int inputId = getIntInput("Enter Cuisine ID: ", 1, Integer.MAX_VALUE);

                if (cuisineMap.containsKey(inputId)) {
                    String cuisineName = cuisineMap.get(inputId);
                    cuisineTags.add(cuisineName);
                    System.out.println("Added: " + cuisineName);
                } else {
                    System.out.println("Invalid Cuisine ID. Please try again.");
                }

                if (cuisineTags.size() >= 3) {
                    break;
                }

                String response = getStringInput("Do you want to add another cuisine tag? (yes/no): ", 3).trim()
                        .toLowerCase();

                if (response.equals("no")) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuisineTags;
    }

    public ArrayList<String> getDietTags() {
        ArrayList<String> dietTags = new ArrayList<>();
        try {
            DBConnectorDish dbConnector = new DBConnectorDish();
            HashMap<Integer, String> dietMap = dbConnector.getAllDietsMapDB();
            System.out.println("Available Diets:");
            dietMap.printHashMap();

            while (dietTags.size() < 5) {
                int inputId = getIntInput("Enter Diet ID: ", 1, Integer.MAX_VALUE);

                if (dietMap.containsKey(inputId)) {
                    String dietName = dietMap.get(inputId);
                    dietTags.add(dietName);
                    System.out.println("Added: " + dietName);
                } else {
                    System.out.println("Invalid Diet ID. Please try again.");
                }

                if (dietTags.size() >= 5) {
                    break;
                }

                String response = getStringInput("Do you want to add another diet tag? (yes/no): ", 3).trim()
                        .toLowerCase();

                if (response.equals("no")) {
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dietTags;
    }

    public int getCourseTags() {
        try {
            DBConnectorDish dbConnector = new DBConnectorDish();
            HashMap<Integer, String> courseMap = dbConnector.getAllCoursesMapDB();
            System.out.println("Available Courses:");
            courseMap.printHashMap();

            while (true) {
                int inputId = getIntInput("Enter Course ID: ", 1, Integer.MAX_VALUE);

                if (courseMap.containsKey(inputId)) {
                    return inputId;
                } else {
                    System.out.println("Invalid Course ID. Please try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int getDishId() {
        try {
            DBConnectorDish dbConnector = new DBConnectorDish();
            HashMap<Integer, String> dishMap = dbConnector.getAllDishMapDB();
            System.out.println("Available Dishes:");
            dishMap.printHashMap();

            while (true) {
                int inputId = getIntInput("Enter Dish ID: ", 1, Integer.MAX_VALUE);

                if (dishMap.containsKey(inputId)) {
                    return inputId;
                } else {
                    System.out.println("Invalid Dish ID. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int getDishRestaurantId(int id) {
        try {
            DBConnectorDish dbConnector = new DBConnectorDish();
            HashMap<Integer, String> dishMap = dbConnector.getAllDishMapRestaurant(id);
            System.out.println("Available Dishes:");
            if (dishMap.size() == 0) {
                System.out.println("No dishes available!");
                return -1;
            }
            dishMap.printHashMap();

            while (true) {
                int inputId = getIntInput("Enter Dish ID: ", 1, Integer.MAX_VALUE);

                if (dishMap.containsKey(inputId)) {
                    return inputId;
                } else {
                    System.out.println("Invalid Dish ID. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}