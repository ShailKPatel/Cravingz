package com.shailkpatel.cravings.db_connection;

import com.shailkpatel.cravings.manager.Manager;
import com.shailkpatel.cravings.util.HashMap;
import com.shailkpatel.cravings.util.InputValidator;

import java.sql.*;

public class DBConnectorManager {
    private static final String URL = "jdbc:mysql://localhost:3306/cravings";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public Connection connection;

    private static final InputValidator iv = new InputValidator();

    public DBConnectorManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveRestaurantManagerDB(Manager manager) throws SQLException {
        String procedureCall = "{CALL registerManager(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {

            stmt.setString(1, manager.getName());
            stmt.setString(2, manager.getPhoneNumber());
            stmt.setString(3, manager.getEmail());
            stmt.setString(4, manager.getPassword());
            stmt.setString(5, manager.getFavoriteFood());
            stmt.setInt(6, manager.getRestaurantID());

            stmt.registerOutParameter(7, Types.INTEGER);

            stmt.execute();

            int managerId = stmt.getInt(7);
            manager.setManagerId(managerId);
        }
    }

    // public void updateManager(Manager manager) throws SQLException {
    // // Update manager details in the Manager table
    // String updateSQL = "UPDATE Manager SET name = ?, phone_number = ?, email = ?,
    // password = ?, restaurant_id = ?, favorite_food = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setString(1, manager.getName());
    // stmt.setString(2, manager.getPhoneNumber());
    // stmt.setString(3, manager.getEmail());
    // stmt.setString(4, manager.getPassword());
    // stmt.setInt(5, manager.getRestaurantID());
    // stmt.setString(6, manager.getFavoriteFood());
    // stmt.setInt(7, manager.getManagerId());
    // stmt.executeUpdate();
    // }
    // }

    public void updateManagerName(int managerId, String newName) throws SQLException {
        String procedureCall = "{CALL UpdateManagerName(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            stmt.setInt(1, managerId);
            stmt.setString(2, newName);

            stmt.registerOutParameter(3, Types.BOOLEAN);

            stmt.execute();

            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager name.");
            }
        }
    }

    // public void updateManagerPhone(int managerId, String newPhone) throws
    // SQLException {
    // String updateSQL = "UPDATE Manager SET phone_number = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setString(1, newPhone);
    // stmt.setInt(2, managerId);
    // stmt.executeUpdate();
    // }
    // }

    public void viewOrders(Manager manager) throws SQLException {
        String query = "SELECT order_id, address_id, customer_id, order_time FROM Customer_Order WHERE restaurant_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, manager.getRestaurantID());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int addressId = rs.getInt("address_id");
                    int customerId = rs.getInt("customer_id");
                    Timestamp orderTime = rs.getTimestamp("order_time");

                    System.out.println("Order ID: " + orderId);
                    System.out.println("Address ID: " + addressId);
                    System.out.println("Customer ID: " + customerId);
                    System.out.println("Order Time: " + orderTime);
                    System.out.println("-----------------------------");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateManagerPhone(int managerId, String newPhone) throws SQLException {
        // Call the UpdateManagerPhoneNumber stored procedure
        String procedureCall = "{CALL UpdateManagerPhoneNumber(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            // Set input parameters
            stmt.setInt(1, managerId);
            stmt.setString(2, newPhone);

            // Register output parameter for success status
            stmt.registerOutParameter(3, Types.BOOLEAN);

            // Execute the stored procedure
            stmt.execute();

            // Check the success status
            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager phone number.");
            }
        }
    }

    // public void updateManagerEmail(int managerId, String newEmail) throws
    // SQLException {
    // String updateSQL = "UPDATE Manager SET email = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setString(1, newEmail);
    // stmt.setInt(2, managerId);
    // stmt.executeUpdate();
    // }
    // }

    public void updateManagerEmail(int managerId, String newEmail) throws SQLException {
        // Call the UpdateManagerEmail stored procedure
        String procedureCall = "{CALL UpdateManagerEmail(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            // Set input parameters
            stmt.setInt(1, managerId);
            stmt.setString(2, newEmail);

            // Register output parameter for success status
            stmt.registerOutParameter(3, Types.BOOLEAN);

            // Execute the stored procedure
            stmt.execute();

            // Check the success status
            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager email.");
            }
        }
    }

    // public void updateManagerPassword(int managerId, String newPassword) throws
    // SQLException {
    // String updateSQL = "UPDATE Manager SET password = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setString(1, newPassword);
    // stmt.setInt(2, managerId);
    // stmt.executeUpdate();
    // }
    // }

    public void updateManagerPassword(int managerId, String newPassword) throws SQLException {
        // Call the UpdateManagerPassword stored procedure
        String procedureCall = "{CALL UpdateManagerPassword(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            // Set input parameters
            stmt.setInt(1, managerId);
            stmt.setString(2, newPassword);

            // Register output parameter for success status
            stmt.registerOutParameter(3, Types.BOOLEAN);

            // Execute the stored procedure
            stmt.execute();

            // Check the success status
            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager password.");
            }
        }
    }

    // public void updateManagerRestaurantId(int managerId, int newRestaurantId)
    // throws SQLException {
    // String updateSQL = "UPDATE Manager SET restaurant_id = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setInt(1, newRestaurantId);
    // stmt.setInt(2, managerId);
    // stmt.executeUpdate();
    // }
    // }

    public void updateManagerRestaurantId(int managerId, int newRestaurantId) throws SQLException {
        // Call the UpdateManagerRestaurant stored procedure
        String procedureCall = "{CALL UpdateManagerRestaurant(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            // Set input parameters
            stmt.setInt(1, managerId);
            stmt.setInt(2, newRestaurantId);

            // Register output parameter for success status
            stmt.registerOutParameter(3, Types.BOOLEAN);

            // Execute the stored procedure
            stmt.execute();

            // Check the success status
            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager's restaurant ID.");
            }
        }
    }

    // public void updateManagerActiveStatus(int managerId, boolean isActive) throws
    // SQLException {
    // String updateSQL = "UPDATE Manager SET isActive = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setBoolean(1, isActive);
    // stmt.setInt(2, managerId);
    // stmt.executeUpdate();
    // }
    // }

    public void updateManagerActiveStatus(int managerId, boolean isActive) throws SQLException {
        String callSQL = "{call UpdateManagerIsActive(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(callSQL)) {
            stmt.setInt(1, managerId);
            stmt.setBoolean(2, isActive);
            stmt.registerOutParameter(3, Types.BOOLEAN); // For the OUT parameter `p_success`

            stmt.execute();

            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager's active status.");
            }
        }
    }

    // public void updateManagerOnlineStatus(int managerId, boolean isOnline) throws
    // SQLException {
    // String updateSQL = "UPDATE Manager SET isOnline = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setBoolean(1, isOnline);
    // stmt.setInt(2, managerId);
    // stmt.executeUpdate();
    // }
    // }

    public void updateManagerOnlineStatus(int managerId, boolean isOnline) throws SQLException {
        String callSQL = "{call UpdateManagerIsOnline(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(callSQL)) {
            stmt.setInt(1, managerId);
            stmt.setBoolean(2, isOnline);
            stmt.registerOutParameter(3, Types.BOOLEAN); // For the OUT parameter `p_success`

            stmt.execute();

            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager's online status.");
            }
        }
    }

    // public void updateManagerFavoriteFood(int managerId, String food) throws
    // SQLException {
    // String updateSQL = "UPDATE Manager SET favorite_food = ? WHERE id = ?";
    // try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
    // stmt.setString(1, food);
    // stmt.setInt(2, managerId);
    // stmt.executeUpdate();
    // }
    // }

    public void updateManagerFavoriteFood(int managerId, String food) throws SQLException {
        String callSQL = "{call UpdateManagerFavoriteFood(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(callSQL)) {
            stmt.setInt(1, managerId);
            stmt.setString(2, food);
            stmt.registerOutParameter(3, Types.BOOLEAN);

            stmt.execute();

            boolean success = stmt.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update manager's favorite food.");
            }
        }
    }

    public Manager getRestaurantManagerDB(int managerId) throws SQLException {
        String query = "SELECT id, name, phone_number, email, password, restaurant_id, favorite_food " +
                "FROM GetManagerID WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Manager(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("restaurant_id"),
                        rs.getString("favorite_food"));
            } else {
                return null;
            }
        }
    }

    public Manager getRestaurantManagerDBPhoneNumber(String phoneNumber) throws SQLException {
        String query = "SELECT id, name, phone_number, email, password, restaurant_id, favorite_food " +
                "FROM GetManagerPhone WHERE phone_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Manager(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("restaurant_id"),
                        rs.getString("favorite_food"));
            } else {
                return null;
            }
        }
    }

    public boolean validateManager(String phoneNumber, String password) throws SQLException {
        String query = "SELECT id FROM Manager WHERE phone_number = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If a result is returned, the credentials are valid
        }
    }

    public boolean validateManagerFood(String phoneNumber, String favoriteFood) throws SQLException {
        String query = "SELECT id FROM Manager WHERE phone_number = ? AND favorite_food = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            stmt.setString(2, favoriteFood);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If a result is returned, the credentials are valid
        }
    }

    public String getPhoneNumberByEmailAndPassword(String email, String password) throws SQLException {
        String query = "SELECT phone_number FROM Manager WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("phone_number") : null;
        }
    }

    public HashMap<Integer, String> getAllManagerEmails() throws SQLException {
        HashMap<Integer, String> emailMap = new HashMap<>();
        String query = "SELECT id, email FROM Manager";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int managerId = rs.getInt("id");
                String email = rs.getString("email");
                emailMap.put(managerId, email);
            }
        }
        return emailMap;
    }

    public HashMap<Integer, String> getAllManagerPhoneNumbers() throws SQLException {
        HashMap<Integer, String> phoneMap = new HashMap<>();
        String query = "SELECT id, phone_number FROM Manager";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int managerId = rs.getInt("id");
                String phoneNumber = rs.getString("phone_number");
                phoneMap.put(managerId, phoneNumber);
            }
        }
        return phoneMap;
    }

    public boolean getManagerOnlineStatus(int managerId) throws SQLException {
        String query = "SELECT isOnline FROM Manager WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isOnline");
            } else {
                System.out.println("An error occurred!");
                return false;
            }
        }
    }

    public boolean getManagerActiveStatus(int managerId) {
        String query = "SELECT isActive FROM Manager WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isActive");
            } else {
                System.out.println("An error occurred!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPhoneFromEmail(String email) throws SQLException {
        String query = "SELECT phone_number FROM Manager WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("phone_number");
            } else {
                System.out.println("An error occurred!");
                return null;
            }
        }
    }

    public void increaseRevenue(int restaurantId, double amount) throws SQLException {
        String procedureCall = "{CALL AddToRestaurantRevenue(?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
            callableStatement.setInt(1, restaurantId);
            callableStatement.setInt(2, (int) amount);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);

            callableStatement.execute();

            boolean success = callableStatement.getBoolean(3);
            if (!success) {
                throw new SQLException("Failed to update revenue.");
            }

            System.out.println("Revenue updated successfully for restaurant ID " + restaurantId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update revenue.");
        }
    }

    // public void incrementDishOrders(int dishId) {
    // String procedureCall = "{CALL IncrementDishOrders(?)}";

    // try {
    // CallableStatement callableStatement = connection.prepareCall(procedureCall);

    // callableStatement.setInt(1, dishId);
    // callableStatement.execute();

    // System.out.println("Dish orders incremented successfully for dish ID " +
    // dishId);
    // } catch (SQLException e) {
    // e.printStackTrace();
    // System.out.println("Failed to increment dish orders.");
    // }
    // }

    // public void viewOrders(Manager manager) {
    // int restaurantId = manager.getRestaurantID();
    // String tableName = "Temp_Restaurant_Menu_" + restaurantId;

    // String query = "SELECT d.id AS dish_id, " +
    // "d.name AS dish_name, " +
    // "d.price, " +
    // "d.no_of_orders " +
    // "FROM " + tableName + " t " +
    // "JOIN Dish d ON t.dish_id = d.id " +
    // "ORDER BY d.no_of_orders DESC";

    // try {
    // PreparedStatement pstmt = connection.prepareStatement(query);
    // ResultSet rs = pstmt.executeQuery();

    // System.out.println("Dish Orders:");
    // while (rs.next()) {
    // int dishId = rs.getInt("dish_id");
    // String dishName = rs.getString("dish_name");
    // int price = rs.getInt("price");
    // int noOfOrders = rs.getInt("no_of_orders");

    // System.out.println("Dish ID: " + dishId + ", " +
    // "Dish Name: " + dishName + ", " +
    // "Price: $" + price + ", " +
    // "No. of Orders: " + noOfOrders);
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // System.out.println("Failed to retrieve orders.");
    // }
    // }

}
