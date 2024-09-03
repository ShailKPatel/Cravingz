package com.shailkpatel.cravings.db_connection;

import com.shailkpatel.cravings.customer.Customer;
import com.shailkpatel.cravings.model.Dish;
import com.shailkpatel.cravings.model.Order;
import com.shailkpatel.cravings.util.ArrayList;
import com.shailkpatel.cravings.util.HashMap;
import com.shailkpatel.cravings.util.LinkedList;
import com.shailkpatel.cravings.util.Stack;

import java.sql.*;
import java.time.LocalDateTime;

public class DBConnectorCustomer {
    private static final String URL = "jdbc:mysql://localhost:3306/cravings";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    public DBConnectorCustomer() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomerDB(Customer customer) throws SQLException {
        String procedure = "{CALL registerCustomer(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhoneNumber());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPassword());
            stmt.setString(5, customer.getFavoriteFood());
            stmt.registerOutParameter(6, Types.BOOLEAN);
            stmt.execute();

            boolean success = stmt.getBoolean(6);
            if (success) {
                System.out.println("Customer registered successfully.");
                String getIdQuery = "SELECT LAST_INSERT_ID()";
                int customerId;
                try (Statement idStmt = connection.createStatement();
                        ResultSet rs = idStmt.executeQuery(getIdQuery)) {
                    if (rs.next()) {
                        customerId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve customer ID.");
                    }
                }

                String addressProcedure = "{CALL InsertIntoCustomerAddress(?, ?, ?)}";
                try (CallableStatement addressStmt = connection.prepareCall(addressProcedure)) {
                    for (String address : customer.getAddresses()) {
                        addressStmt.setInt(1, customerId);
                        addressStmt.setString(2, address);
                        addressStmt.registerOutParameter(3, Types.BOOLEAN);
                        addressStmt.execute();

                        boolean addressSuccess = addressStmt.getBoolean(3);
                        if (!addressSuccess) {
                            System.out.println("Failed to insert address: " + address);
                        }
                    }
                }
            } else {
                System.out.println("Customer registration failed.");
            }
        }
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber) throws SQLException {
        String query = "SELECT * FROM Customer WHERE phone_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("id");
                String[] addresses = getCustomerAddresses(customerId);
                LinkedList<Order> orders = getCustomerOrders(customerId);
                Stack<Dish> cart = getCustomerCart(customerId);

                Customer customer = new Customer(
                        customerId,
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password"),
                        addresses,
                        rs.getString("favorite_food"),
                        orders,
                        cart);

                customer.setActive(rs.getBoolean("isActive"));
                customer.setOnline(rs.getBoolean("isOnline"));

                return customer;
            } else {
                return null;
            }
        }
    }

    public boolean validateCustomerFood(String phoneNumber, String favoriteFood) throws SQLException {
        String procedure = "{CALL ConfirmCustomerByPhoneAndFood(?, ?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setString(1, phoneNumber);
            stmt.setString(2, favoriteFood);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();
            return stmt.getBoolean(3);
        }
    }

    public void updateCustomerName(int customerId, String newName) throws SQLException {
        String procedure = "{CALL UpdateCustomerName(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, newName);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();

            if (stmt.getBoolean(3)) {
                System.out.println("Customer name updated successfully.");
            } else {
                System.out.println("Customer name update failed.");
            }
        }
    }

    public void updateCustomerPhoneNumber(int customerId, String newPhoneNumber) throws SQLException {
        String procedure = "{CALL UpdateCustomerPhoneNumber(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, newPhoneNumber);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();

            if (stmt.getBoolean(3)) {
                System.out.println("Customer phone number updated successfully.");
            } else {
                System.out.println("Customer phone number update failed.");
            }
        }
    }

    public void updateCustomerEmail(int customerId, String newEmail) throws SQLException {
        String procedure = "{CALL UpdateCustomerEmail(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, newEmail);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();

            if (stmt.getBoolean(3)) {
                System.out.println("Customer email updated successfully.");
            } else {
                System.out.println("Customer email update failed.");
            }
        }
    }

    public void updateCustomerPassword(int customerId, String newPassword) throws SQLException {
        String procedure = "{CALL UpdateCustomerPassword(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, newPassword);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();

            if (stmt.getBoolean(3)) {
                System.out.println("Customer password updated successfully.");
            } else {
                System.out.println("Customer password update failed.");
            }
        }
    }

    public void updateCustomerAddresses(int customerId, String[] newAddresses) throws SQLException {
        String deleteSQL = "{CALL DeleteFromCustomerAddress(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(deleteSQL)) {
            for (String address : newAddresses) {
                stmt.setInt(1, customerId);
                stmt.setString(2, address);
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        }

        String insertSQL = "{CALL InsertIntoCustomerAddress(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(insertSQL)) {
            for (String address : newAddresses) {
                stmt.setInt(1, customerId);
                stmt.setString(2, address);
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        }
    }

    public void updateCustomerFavoriteFood(int customerId, String food) throws SQLException {
        String procedure = "{CALL UpdateCustomerFavoriteFood(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, food);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();

            if (stmt.getBoolean(3)) {
                System.out.println("Customer favorite food updated successfully.");
            } else {
                System.out.println("Customer favorite food update failed.");
            }
        }
    }

    public boolean validateCustomer(String phoneNumber, String encryptedPassword) throws SQLException {
        String query = "SELECT id FROM Customer WHERE phone_number = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, phoneNumber);
            stmt.setString(2, encryptedPassword);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public String getPhoneNumberByEmailAndPassword(String email, String encryptedPassword) throws SQLException {
        String query = "SELECT phone_number FROM Customer WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, encryptedPassword);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("phone_number");
            }
        }
        return null;
    }

    public Customer getCustomerDB(int customerId) throws SQLException {
        String query = "SELECT id, name, phone_number, email, password, isActive, isOnline, favorite_food FROM Customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password"),
                        getCustomerAddresses(customerId),
                        rs.getString("favorite_food"),
                        getCustomerOrders(customerId),
                        getCustomerCart(customerId));
                customer.setActive(rs.getBoolean("isActive"));
                customer.setOnline(rs.getBoolean("isOnline"));
                return customer;
            } else {
                return null;
            }
        }
    }

    public String[] getCustomerAddresses(int customerId) throws SQLException {
        String query = "SELECT address FROM Customer_Address WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            String[] addresses = new String[5];
            int index = 0;
            while (rs.next()) {
                addresses[index++] = rs.getString("address");
            }
            return addresses;
        }
    }

    public LinkedList<Order> getCustomerOrders(int customerId) throws SQLException {
        LinkedList<Order> orders = new LinkedList<>();
        String ordersQuery = "SELECT * FROM Customer_Order WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(ordersQuery)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int addressId = rs.getInt("address_id");
                int restaurantId = rs.getInt("restaurant_id");
                boolean isReviewed = rs.getBoolean("isReviewed");
                LocalDateTime timestamp = rs.getTimestamp("order_time").toLocalDateTime();

                ArrayList<Dish> dishes = getDishesForOrder(orderId);
                String deliveryAddress = getDeliveryAddress(addressId);

                Order order = new Order(orderId, customerId, restaurantId, dishes, deliveryAddress, isReviewed,
                        timestamp);
                orders.addLast(order);
            }
        }
        return orders;
    }

    private ArrayList<Dish> getDishesForOrder(int orderId) throws SQLException {
        ArrayList<Dish> dishes = new ArrayList<>();
        String query = "SELECT dish_id, quantity FROM Order_Dish WHERE order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int dishId = rs.getInt("dish_id");
                DBConnectorDish db = new DBConnectorDish();
                Dish dish = db.getDishById(dishId);
                dishes.add(dish);
            }
        }
        return dishes;
    }

    private String getDeliveryAddress(int addressId) throws SQLException {
        String query = "SELECT address FROM Customer_Address WHERE address_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, addressId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("address");
            } else {
                return "Address not found";
            }
        }
    }

    public Stack<Dish> getCustomerCart(int customerId) throws SQLException {
        Stack<Dish> cart = new Stack<>();
        String query = "SELECT dish_id FROM Customer_Cart WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DBConnectorDish dbcd = new DBConnectorDish();
                Dish dish = dbcd.getDishById(rs.getInt("dish_id"));
                cart.push(dish);
            }
        }
        return cart;
    }

    public void saveCartDB(int customerId, Stack<Dish> cart) throws SQLException {
        String deleteFromCartSQL = "{CALL DeleteFromCustomerCart(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(deleteFromCartSQL)) {
            while (!cart.isEmpty()) {
                Dish dish = cart.pop();
                stmt.setInt(1, customerId);
                stmt.setInt(2, dish.getDishId());
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        }

        String insertSQL = "{CALL InsertIntoCustomerCart(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(insertSQL)) {
            while (!cart.isEmpty()) {
                Dish dish = cart.pop();
                stmt.setInt(1, customerId);
                stmt.setInt(2, dish.getDishId());
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        }
    }

    public HashMap<Integer, String> getAllCustomerEmails() throws SQLException {
        HashMap<Integer, String> emailMap = new HashMap<>();
        String query = "SELECT id, email FROM Customer";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int customerId = rs.getInt("id");
                String email = rs.getString("email");
                emailMap.put(customerId, email);
            }
        }
        return emailMap;
    }

    public HashMap<Integer, String> getAllCustomerPhoneNumbers() throws SQLException {
        HashMap<Integer, String> phoneMap = new HashMap<>();
        String query = "SELECT id, phone_number FROM Customer";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int customerId = rs.getInt("id");
                String phoneNumber = rs.getString("phone_number");
                phoneMap.put(customerId, phoneNumber);
            }
        }
        return phoneMap;
    }

    public boolean getCustomerOnlineStatus(int customerId) throws SQLException {
        String query = "SELECT isOnline FROM Customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isOnline");
            } else {
                System.out.println("An error occurred!");
                return false;
            }
        }
    }

    public boolean getCustomerActiveStatus(int customerId) throws SQLException {
        String query = "SELECT isActive FROM Customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("isActive");
            } else {
                System.out.println("An error occurred!");
                return false;
            }
        }
    }

    public String getPhoneFromEmail(String email) throws SQLException {
        String query = "SELECT phone_number FROM Customer WHERE email = ?";
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

    public boolean loginCustomer(int customerId) throws SQLException {
        String procedure = "{CALL LoginCustomer(?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.registerOutParameter(2, Types.BOOLEAN);
            stmt.execute();
            return stmt.getBoolean(2);
        }
    }

    public boolean logoutCustomer(int customerId) throws SQLException {
        String procedure = "{CALL LogoutCustomer(?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.registerOutParameter(2, Types.BOOLEAN);
            stmt.execute();
            return stmt.getBoolean(2);
        }
    }

    public void updateCustomerActiveStatus(int customerId, boolean isActive) throws SQLException {
        String updateSQL = "{CALL UpdateCustomerIsActive(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(updateSQL)) {
            stmt.setInt(1, customerId);
            stmt.setBoolean(2, isActive);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();

            if (stmt.getBoolean(3)) {
                System.out.println("Customer active status updated successfully.");
            } else {
                System.out.println("Customer active status update failed.");
            }
        }
    }

    public void viewCookingOrders(int customerId) throws SQLException {
        String query = "SELECT oc.cooking_order_id, d.name, ocd.quantity, oc.start_time " +
                "FROM OrdersCooking oc " +
                "JOIN OrdersCookingDish ocd ON oc.cooking_order_id = ocd.cooking_order_id " +
                "JOIN Dish d ON ocd.dish_id = d.id " +
                "WHERE oc.customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int cookingOrderId = rs.getInt("cooking_order_id");
                    String dishName = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    Timestamp startTime = rs.getTimestamp("start_time");

                    System.out.println("Order ID: " + cookingOrderId);
                    System.out.println("Dish: " + dishName);
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Start Time: " + startTime);
                    System.out.println("-----------------------------");
                }
            }
        }
    }

    public void viewCookedOrders(int customerId) throws SQLException {
        String query = "SELECT co.order_id, d.name, od.quantity, co.order_time " +
                "FROM Customer_Order co " +
                "JOIN Order_Dish od ON co.order_id = od.order_id " +
                "JOIN Dish d ON od.dish_id = d.id " +
                "WHERE co.customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String dishName = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    Timestamp orderTime = rs.getTimestamp("order_time");

                    System.out.println("Order ID: " + orderId);
                    System.out.println("Dish: " + dishName);
                    System.out.println("Quantity: " + quantity);
                    System.out.println("Order Time: " + orderTime);
                    System.out.println("-----------------------------");
                }
            }
        }
    }

    public void viewCart(int customerId) throws SQLException {
        String query = "SELECT d.name " +
                "FROM Customer_Cart cc " +
                "JOIN Dish d ON cc.dish_id = d.id " +
                "WHERE cc.customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("Your cart is empty.");
                } else {
                    while (rs.next()) {
                        String dishName = rs.getString("name");
                        System.out.println("Dish: " + dishName);
                    }
                }
            }
        }
    }

    public int insertOrder(int customerId, int addressId, int restaurantID, boolean isReviewed) throws SQLException {
        String procedure = "{CALL InsertCustomerOrder(?, ?, ?, NOW(), ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, addressId);
            stmt.setInt(2, restaurantID);
            stmt.setInt(3, customerId);
            stmt.registerOutParameter(4, Types.BOOLEAN); // p_success
            stmt.registerOutParameter(5, Types.INTEGER); // p_order_id
            stmt.execute();
            boolean success = stmt.getBoolean(4);
            if (success) {
                return stmt.getInt(5);
            } else {
                throw new SQLException("Failed to insert order.");
            }
        }
    }

    public void addToCart(int customerId, int dishID) throws SQLException {
        String procedure = "{CALL InsertIntoCustomerCart(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedure)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, dishID);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(3)) {
                throw new SQLException("Failed to add dish to cart.");
            }
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to connect to the database.");
            }
        }
        return connection;
    }

}
