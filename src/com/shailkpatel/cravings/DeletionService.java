// package com.shailkpatel.cravings;

// import java.sql.*;
// import java.util.concurrent.*;

// public class DeletionService {
//     private final Connection connection;
//     private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

//     public DeletionService(Connection connection) {
//         this.connection = connection;
//         scheduleDeletionTasks();
//     }

//     // Schedules the deletion tasks for customers, managers, and restaurants
//     private void scheduleDeletionTasks() {
//         scheduler.scheduleAtFixedRate(this::deleteOldCustomers, 0, 1, TimeUnit.HOURS);
//         scheduler.scheduleAtFixedRate(this::deleteOldManagers, 0, 1, TimeUnit.HOURS);
//         scheduler.scheduleAtFixedRate(this::deleteOldRestaurants, 0, 1, TimeUnit.HOURS);
//     }

//     // Deletes customers who have been inactive for more than 30 days
//     private void deleteOldCustomers() {
//         deleteOldRecords("Deleted_Customer", "customer_id", this::hardDeleteCustomer);
//     }

//     // Deletes managers who have been inactive for more than 30 days
//     private void deleteOldManagers() {
//         deleteOldRecords("Deleted_Manager", "manager_id", this::hardDeleteManager);
//     }

//     // Deletes restaurants that have been inactive for more than 30 days
//     private void deleteOldRestaurants() {
//         deleteOldRecords("Deleted_Restaurant", "restaurant_id", this::hardDeleteRestaurant);
//     }

//     // Helper method to delete old records from the specified table
//     private void deleteOldRecords(String tableName, String columnName, Consumer<Integer> deleteFunction) {
//         String sql = "SELECT " + columnName + " FROM " + tableName
//                 + " WHERE deleted_timestamp < NOW() - INTERVAL 30 DAY";
//         try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
//             while (rs.next()) {
//                 int id = rs.getInt(columnName);
//                 deleteFunction.accept(id);
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     // Deletes a customer and their associated tables
//     public void hardDeleteCustomer(int customerId) {
//         try {
//             // Delete the customer from the Customer table
//             String sql = "DELETE FROM Customer WHERE id = ?";
//             try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                 pstmt.setInt(1, customerId);
//                 pstmt.executeUpdate();
//             }

//             // Drop the associated temporary tables
//             dropTempTable("Temp_Customer_Address_", customerId);
//             dropTempTable("Temp_Customer_Cart_", customerId);
//             dropTempTable("Temp_Customer_Order_", customerId);
//             // Add any other associated tables here

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     // Deletes a manager and their associated tables
//     public void hardDeleteManager(int managerId) {
//         try {
//             // Delete the manager from the Manager table
//             String sql = "DELETE FROM Manager WHERE id = ?";
//             try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                 pstmt.setInt(1, managerId);
//                 pstmt.executeUpdate();
//             }

//             // Drop any associated temporary tables related to managers
//             dropTempTable("Temp_Manager_", managerId);
//             // Add any other associated tables here

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     // Deletes a restaurant and their associated tables
//     public void hardDeleteRestaurant(int restaurantId) {
//         try {
//             // Delete the restaurant from the Restaurant table
//             String sql = "DELETE FROM Restaurant WHERE id = ?";
//             try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                 pstmt.setInt(1, restaurantId);
//                 pstmt.executeUpdate();
//             }

//             // Drop the associated temporary table
//             dropTempTable("Temp_Restaurant_Menu_", restaurantId);
//             // Add any other associated tables here

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     // Utility method to drop a temporary table associated with a given ID
//     private void dropTempTable(String prefix, int id) {
//         String tempTableName = prefix + id;
//         try (PreparedStatement pstmt = connection.prepareStatement("DROP TABLE IF EXISTS " + tempTableName)) {
//             pstmt.executeUpdate();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     // Shuts down the scheduler gracefully
//     public void shutdown() {
//         scheduler.shutdown();
//         try {
//             if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
//                 scheduler.shutdownNow();
//             }
//         } catch (InterruptedException e) {
//             scheduler.shutdownNow();
//         }
//     }
// }
