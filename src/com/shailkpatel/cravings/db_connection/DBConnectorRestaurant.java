package com.shailkpatel.cravings.db_connection;

import java.math.BigDecimal;
import java.sql.*;

import com.shailkpatel.cravings.model.Dish;
import com.shailkpatel.cravings.model.Restaurant;
import com.shailkpatel.cravings.util.ArrayList;
import com.shailkpatel.cravings.util.LinkedList;
import com.shailkpatel.cravings.util.HashSet;
import com.shailkpatel.cravings.util.HashMap;

public class DBConnectorRestaurant {
    private static final String URL = "jdbc:mysql://localhost:3306/cravings";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    public DBConnectorRestaurant() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public HashMap<Integer, String> getAllThemesMap() throws SQLException {
        HashMap<Integer, String> themeMap = new HashMap<>();
        String query = "SELECT id, name FROM Theme";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                themeMap.put(rs.getInt("id"), rs.getString("name"));
            }
        }
        return themeMap;
    }

    public void addThemeDB(String themeName) throws SQLException {
        String query = "{CALL InsertTheme(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, themeName);
            stmt.registerOutParameter(2, Types.BOOLEAN);
            stmt.executeUpdate();
        }
    }

    public void deleteThemeDB(int themeId) throws SQLException {
        String query = "DELETE FROM Theme WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, themeId);
            stmt.executeUpdate();
        }
    }

    public void updateThemeDB(int themeId, String newThemeName) throws SQLException {
        String query = "{CALL UpdateThemeName(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, themeId);
            stmt.setString(2, newThemeName);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.executeUpdate();
        }
    }

    public int saveRestaurantDB(Restaurant restaurant) throws SQLException {
        int restaurantID = -1;
        try {
            connection.setAutoCommit(false);

            // Insert restaurant using stored procedure
            String sql = "{CALL InsertRestaurant(?, ?, ?, ?, ?)}";
            CallableStatement pstmt = connection.prepareCall(sql);
            pstmt.setString(1, restaurant.getRestaurantName());
            pstmt.setString(2, restaurant.getRestaurantAddress());
            pstmt.setInt(3, restaurant.getThemeID());
            pstmt.setString(4, restaurant.getrCode());
            pstmt.registerOutParameter(5, Types.BOOLEAN);
            pstmt.executeUpdate();
            System.out.println("Restaurant registered successfully.");

            // Retrieve the generated restaurant ID
            restaurantID = getGeneratedId(pstmt);

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            connection.rollback();
            connection.setAutoCommit(true);
            e.printStackTrace();
        }

        return restaurantID;
    }

    public Restaurant getRestaurantByIdAndCode(int restaurantId, String code) throws SQLException {
        Restaurant restaurant = null;
        String query = "SELECT id, name, address, theme_id, numberOfStars, numberOfReviews, rCode, isAcceptingOrders " +
                "FROM Restaurant WHERE id = ? AND rCode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            stmt.setString(2, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    restaurant = new Restaurant();
                    restaurant.setId(rs.getInt("id"));
                    restaurant.setRestaurantName(rs.getString("name"));
                    restaurant.setRestaurantAddress(rs.getString("address"));
                    restaurant.setRestaurantThemeID(rs.getInt("theme_id"));
                    restaurant.setNumberOfStars(rs.getInt("numberOfStars"));
                    restaurant.setNumberOfReviews(rs.getInt("numberOfReviews"));
                    restaurant.setrCode(rs.getString("rCode"));
                    restaurant.setAcceptingOrders(rs.getBoolean("isAcceptingOrders"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    private int getGeneratedId(CallableStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public void updateRestaurantStars(int restaurantId, int stars) throws SQLException {
        String query = "{CALL AddRestaurantReview(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, restaurantId);
            stmt.setInt(2, stars);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.executeUpdate();
        }
    }

    public boolean startAcceptingOrders(int restaurantID) {
        String sql = "{CALL StartAcceptingOrders(?, ?)}";
        try (CallableStatement pstmt = connection.prepareCall(sql)) {
            pstmt.setInt(1, restaurantID);
            pstmt.registerOutParameter(2, Types.BOOLEAN);

            pstmt.executeUpdate();
            return pstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean stopAcceptingOrders(int restaurantID) {
        String sql = "{CALL StopAcceptingOrders(?, ?)}";
        try (CallableStatement pstmt = connection.prepareCall(sql)) {
            pstmt.setInt(1, restaurantID);
            pstmt.registerOutParameter(2, Types.BOOLEAN);

            pstmt.executeUpdate();
            return pstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addDishToRestaurantDB(int restaurantID, Dish dish) {
        try {
            int dishID = dish.getDishId();

            if (dishID != -1) {
                String insertSql = "{CALL InsertIntoRestaurantMenu(?, ?, ?)}";
                try (CallableStatement pstmt = connection.prepareCall(insertSql)) {
                    pstmt.setInt(1, restaurantID);
                    pstmt.setInt(2, dishID);
                    pstmt.registerOutParameter(3, Types.BOOLEAN);
                    pstmt.executeUpdate();
                }
            } else {
                System.out.println("Failed to retrieve dish ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDishFromRestaurantDB(int restaurantID, int dishID) {
        try {
            String sql = "{CALL DeleteFromRestaurantMenu(?, ?, ?)}";
            try (CallableStatement pstmt = connection.prepareCall(sql)) {
                pstmt.setInt(1, restaurantID);
                pstmt.setInt(2, dishID);
                pstmt.registerOutParameter(3, Types.BOOLEAN);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getThemeIdByNameDB(String themeName) throws SQLException {
        String query = "SELECT id FROM Theme WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, themeName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    public void updateRestaurantName(int restaurantID, String newName) {
        try {
            String sql = "{CALL UpdateRestaurantName(?, ?, ?)}";
            try (CallableStatement pstmt = connection.prepareCall(sql)) {
                pstmt.setInt(1, restaurantID);
                pstmt.setString(2, newName);
                pstmt.registerOutParameter(3, Types.BOOLEAN);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRCode(int restaurantID, String newCode) {
        String sql = "{CALL UpdateRestaurantCode(?, ?, ?)}";
        try (CallableStatement pstmt = connection.prepareCall(sql)) {
            pstmt.setInt(1, restaurantID);
            pstmt.setString(2, newCode);
            pstmt.registerOutParameter(3, Types.BOOLEAN);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTheme(int restaurantID, int newThemeID) {
        try {
            String sql = "{CALL UpdateRestaurantTheme(?, ?, ?)}";
            try (CallableStatement pstmt = connection.prepareCall(sql)) {
                pstmt.setInt(1, restaurantID);
                pstmt.setInt(2, newThemeID);
                pstmt.registerOutParameter(3, Types.BOOLEAN);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRestaurantIsActive(int restaurantID, Boolean active) {
        try {
            String sql = "{CALL UpdateRestaurantIsActive(?, ?, ?)}";
            try (CallableStatement pstmt = connection.prepareCall(sql)) {
                pstmt.setInt(1, restaurantID);
                pstmt.setBoolean(2, active);
                pstmt.registerOutParameter(3, Types.BOOLEAN);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRestaurantAddress(int restaurantID, String newAddress) {
        try {
            String sql = "{CALL UpdateRestaurantAddress(?, ?, ?)}";
            try (CallableStatement pstmt = connection.prepareCall(sql)) {
                pstmt.setInt(1, restaurantID);
                pstmt.setString(2, newAddress);
                pstmt.registerOutParameter(3, Types.BOOLEAN);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void softDeleteRestaurant(int restaurantID) {
        try {
            String sql = "UPDATE Restaurant SET isActive = false WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, restaurantID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDishToMenu(int restaurantID, int dishID) {
        try {
            String sql = "{CALL InsertIntoRestaurantMenu(?, ?, ?)}";
            try (CallableStatement pstmt = connection.prepareCall(sql)) {
                pstmt.setInt(1, restaurantID);
                pstmt.setInt(2, dishID);
                pstmt.registerOutParameter(3, Types.BOOLEAN);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDishFromMenu(int restaurantID, int dishID) {
        try {
            String sql = "{CALL DeleteFromRestaurantMenu(?, ?, ?)}";
            try (CallableStatement pstmt = connection.prepareCall(sql)) {
                pstmt.setInt(1, restaurantID);
                pstmt.setInt(2, dishID);
                pstmt.registerOutParameter(3, Types.BOOLEAN);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printRestaurantMenu(int restaurantID) {
        try {
            String sql = "SELECT dish_id FROM Restaurant_Menu WHERE restaurant_id = ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, restaurantID);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Menu for Restaurant ID: " + restaurantID);
            System.out.println("-----------------------------");

            DBConnectorDish dbConnectorDish = new DBConnectorDish();

            while (rs.next()) {
                int dishID = rs.getInt("dish_id");
                dbConnectorDish.printDishFromDB(dishID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printAllRestaurants() {
        try {
            String sql = """
                    SELECT r.id, r.name, r.address, t.name AS theme, r.numberOfStars, r.numberOfReviews
                    FROM Restaurant r
                    LEFT JOIN Theme t ON r.theme_id = t.id
                    """;

            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String theme = rs.getString("theme");
                int numberOfStars = rs.getInt("numberOfStars");
                int numberOfReviews = rs.getInt("numberOfReviews");

                int numberOfDishes = getNumberOfDishesForRestaurant(id);

                System.out.println("Restaurant ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Address: " + address);
                System.out.println("Theme: " + theme);
                System.out.println("Number of Dishes: " + numberOfDishes);

                if (numberOfReviews == 0) {
                    System.out.println("Rating: Unrated");
                } else {
                    double rating = (double) numberOfStars / numberOfReviews;
                    System.out.println("Rating: " + rating);
                }
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNumberOfDishesForRestaurant(int restaurantID) {
        int numberOfDishes = 0;
        try {
            String sql = "SELECT COUNT(*) AS numberOfDishes FROM Restaurant_Menu WHERE restaurant_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, restaurantID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                numberOfDishes = rs.getInt("numberOfDishes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfDishes;
    }

    public HashMap<Integer, String> getAllRestaurantIDsAndNames() {
        HashMap<Integer, String> restaurantMap = new HashMap<>();
        try {
            String sql = "SELECT id, name FROM Restaurant";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                restaurantMap.put(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantMap;
    }

    public Restaurant getRestaurantByID(int restaurantID) {
        Restaurant restaurant = null;
        try {
            String sql = """
                    SELECT r.id, r.name, r.address, r.theme_id, r.numberOfStars, r.numberOfReviews, r.rCode
                    FROM Restaurant r
                    WHERE r.id = ?
                    """;

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, restaurantID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                int themeID = rs.getInt("theme_id");
                int numberOfStars = rs.getInt("numberOfStars");
                int numberOfReviews = rs.getInt("numberOfReviews");
                String rCode = rs.getString("rCode");

                // Initialize the LinkedList for the restaurant menu
                LinkedList<Dish> restaurantMenu = new LinkedList<>();

                // Fetching the dishes for the restaurant
                String dishSQL = "SELECT dish_id FROM Restaurant_Menu WHERE restaurant_id = ?";
                PreparedStatement dishStmt = connection.prepareStatement(dishSQL);
                dishStmt.setInt(1, restaurantID);
                ResultSet dishRs = dishStmt.executeQuery();

                DBConnectorDish dbConnectorDish = new DBConnectorDish();

                while (dishRs.next()) {
                    int dishID = dishRs.getInt("dish_id");
                    Dish dish = dbConnectorDish.getDishById(dishID);
                    restaurantMenu.addLast(dish);
                }

                // Creating the Restaurant object with the constructor
                restaurant = new Restaurant(id, name, address, themeID, restaurantMenu, numberOfReviews, numberOfStars,
                        rCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    public HashSet<Integer> getAllRestaurantIdsDB() {
        HashSet<Integer> restaurantIds = new HashSet<>();
        String query = "SELECT id FROM Restaurant";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                restaurantIds.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantIds;
    }

    public String getThemeFromThemeID(int id) throws SQLException {
        String theme = null;
        String query = "SELECT name FROM Theme WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    theme = rs.getString("name");
                }
            }
        }
        return theme;
    }

    public int getRevenueFromID(int id) throws SQLException {
        int revenue = 0;
        String query = "SELECT revenue FROM Restaurant WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    revenue = rs.getInt("revenue");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }

    public void increaseRestaurantRevenue(int restaurantId, double amount) throws SQLException {
        String sql = "{CALL AddToRestaurantRevenue(?, ?, ?)}";

        try (CallableStatement cstmt = connection.prepareCall(sql)) {
            cstmt.setInt(1, restaurantId);
            cstmt.setBigDecimal(2, new BigDecimal(amount));
            cstmt.registerOutParameter(3, Types.BOOLEAN);
            cstmt.execute();
        }
    }
}
