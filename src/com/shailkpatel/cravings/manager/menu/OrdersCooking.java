package com.shailkpatel.cravings.manager.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.shailkpatel.cravings.db_connection.DBConnectorManager;
import com.shailkpatel.cravings.manager.Manager;

public class OrdersCooking {

    public void printOrdersCooking(Manager manager) {
        int restaurantId = manager.getRestaurantID();

        // Query to fetch orders currently being cooked along with dish details
        String query = "SELECT oc.customer_id, d.id AS dish_id, d.name AS dish_name, " +
                "DATE_ADD(oc.start_time, INTERVAL MAX(d.preparation_time + 5) MINUTE) AS end_time " +
                "FROM OrdersCooking oc " +
                "JOIN OrdersCookingDish ocd ON oc.cooking_order_id = ocd.cooking_order_id " +
                "JOIN Dish d ON ocd.dish_id = d.id " +
                "WHERE oc.restaurant_id = ? " +
                "GROUP BY oc.customer_id, d.id, d.name, oc.start_time";

        try {
            DBConnectorManager dbConnectorManager = new DBConnectorManager();
            Connection connection = dbConnectorManager.connection;
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            boolean hasOrders = false;

            System.out.println("Orders currently being cooked for restaurant ID: " + restaurantId);
            while (rs.next()) {
                hasOrders = true; // Set to true if there is at least one order
                int customerId = rs.getInt("customer_id");
                int dishId = rs.getInt("dish_id");
                String dishName = rs.getString("dish_name");
                String endTime = rs.getTimestamp("end_time").toString();

                System.out.println("Customer ID: " + customerId + ", Dish ID: " + dishId +
                        ", Dish Name: " + dishName + ", Order Ready by: " + endTime);
            }

            if (!hasOrders) {
                System.out.println("Currently no orders cooking.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while fetching orders.");
        }
    }

}
