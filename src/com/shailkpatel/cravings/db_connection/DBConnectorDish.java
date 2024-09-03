package com.shailkpatel.cravings.db_connection;

import java.sql.*;
import com.shailkpatel.cravings.model.Dish;
import com.shailkpatel.cravings.util.ArrayList;
import com.shailkpatel.cravings.util.HashMap;

public class DBConnectorDish {
    private static final String URL = "jdbc:mysql://localhost:3306/cravings";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    public DBConnectorDish() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Methods to return hash maps of all IDs and names from Cuisine, Diet, and
    // Course tables
    public HashMap<Integer, String> getAllCuisinesMapDB() throws SQLException {
        return getHashMapFromQuery("SELECT id, name FROM Cuisine");
    }

    public HashMap<Integer, String> getAllDietsMapDB() throws SQLException {
        return getHashMapFromQuery("SELECT id, name FROM Diet");
    }

    public HashMap<Integer, String> getAllCoursesMapDB() throws SQLException {
        return getHashMapFromQuery("SELECT id, name FROM Course");
    }

    public HashMap<Integer, String> getAllDishMapDB() throws SQLException {
        return getHashMapFromQuery("SELECT id, name FROM Dish");
    }

    public HashMap<Integer, String> getAllDishMapRestaurant(int restaurantId) throws SQLException {
        String query = "SELECT rm.dish_id, d.name FROM Restaurant_Menu rm " +
                "JOIN Dish d ON rm.dish_id = d.id WHERE rm.restaurant_id = ?";
        HashMap<Integer, String> map = new HashMap<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("dish_id"), rs.getString("name"));
                }
            }
        }
        return map;
    }

    private HashMap<Integer, String> getHashMapFromQuery(String query) throws SQLException {
        HashMap<Integer, String> map = new HashMap<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("name"));
            }
        }
        return map;
    }

    public void insertCourse(String courseName) throws SQLException {
        String query = "{CALL InsertCourse(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, courseName);
            stmt.registerOutParameter(2, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(2)) {
                throw new SQLException("Failed to insert course");
            }
        }
    }

    public void insertCuisine(String cuisineName) throws SQLException {
        String query = "{CALL InsertCuisine(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, cuisineName);
            stmt.registerOutParameter(2, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(2)) {
                throw new SQLException("Failed to insert cuisine");
            }
        }
    }

    public void insertDiet(String dietName) throws SQLException {
        String query = "{CALL InsertDiet(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, dietName);
            stmt.registerOutParameter(2, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(2)) {
                throw new SQLException("Failed to insert diet");
            }
        }
    }

    public void updateDishName(int dishId, String newName) throws SQLException {
        String query = "{CALL UpdateDishName(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, dishId);
            stmt.setString(2, newName);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(3)) {
                throw new SQLException("Failed to update dish name");
            }
        }
    }

    public void updateDishCourse(int dishId, int newCourseID) throws SQLException {
        String query = "{CALL UpdateDishCourseId(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, dishId);
            stmt.setInt(2, newCourseID);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(3)) {
                throw new SQLException("Failed to update dish course");
            }
        }
    }

    public void updateDishPrice(int dishId, int newPrice) throws SQLException {
        String query = "{CALL UpdateDishPrice(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, dishId);
            stmt.setInt(2, newPrice);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(3)) {
                throw new SQLException("Failed to update dish price");
            }
        }
    }

    public void updateDishPreparationTime(int dishId, int newPrepTime) throws SQLException {
        String query = "{CALL UpdateDishPreparationTime(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, dishId);
            stmt.setInt(2, newPrepTime);
            stmt.registerOutParameter(3, Types.BOOLEAN);
            stmt.execute();
            if (!stmt.getBoolean(3)) {
                throw new SQLException("Failed to update dish preparation time");
            }
        }
    }

    public void updateDishNumberOfStars(int dishId, int newStars) throws SQLException {
        String query = "{CALL UpdateDishStars(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, dishId);
            stmt.setInt(2, newStars);
            stmt.executeUpdate();
        }
    }

    public void updateDishCuisineTagsInDB(int dishId, ArrayList<String> newCuisineTags) throws SQLException {
        // Delete existing tags
        String deleteQuery = "{CALL DeleteFromDishCuisine(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(deleteQuery)) {
            for (int i = 0; i < newCuisineTags.size(); i++) {
                String tag = newCuisineTags.get(i);
                int cuisineId = getCuisineIdByName(tag);
                stmt.setInt(1, dishId);
                stmt.setInt(2, cuisineId);
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert new tags
        String insertQuery = "{CALL InsertIntoDishCuisine(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(insertQuery)) {
            for (int i = 0; i < newCuisineTags.size(); i++) {
                String tag = newCuisineTags.get(i);
                int cuisineId = getCuisineIdByName(tag);
                stmt.setInt(1, dishId);
                stmt.setInt(2, cuisineId);
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDishDietTagsInDB(int dishId, ArrayList<String> newDietTags) throws SQLException {
        // Delete existing tags
        String deleteQuery = "{CALL DeleteFromDishDiet(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(deleteQuery)) {
            for (int i = 0; i < newDietTags.size(); i++) {
                String tag = newDietTags.get(i);
                int dietId = getDietIdByName(tag);
                stmt.setInt(1, dishId);
                stmt.setInt(2, dietId);
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert new tags
        String insertQuery = "{CALL InsertIntoDishDiet(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(insertQuery)) {
            for (int i = 0; i < newDietTags.size(); i++) {
                String tag = newDietTags.get(i);
                int dietId = getDietIdByName(tag);
                stmt.setInt(1, dishId);
                stmt.setInt(2, dietId);
                stmt.registerOutParameter(3, Types.BOOLEAN);
                stmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCuisine(String cuisineName) throws SQLException {
        String query = "DELETE FROM Cuisine WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cuisineName);
            stmt.executeUpdate();
        }
    }

    public void deleteDiet(String dietName) throws SQLException {
        String query = "DELETE FROM Diet WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, dietName);
            stmt.executeUpdate();
        }
    }

    public void deleteCourse(String courseName) throws SQLException {
        String query = "DELETE FROM Course WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseName);
            stmt.executeUpdate();
        }
    }

    public int addDishToDB(Dish dish) throws SQLException {
        String insertDishSQL = "{CALL InsertDish(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(insertDishSQL)) {
            stmt.setString(1, dish.getDishName());
            stmt.setInt(2, dish.getDishPrice());
            stmt.setInt(3, dish.getDishCourseID());
            stmt.setInt(4, dish.getDishPreparationTime());
            stmt.registerOutParameter(5, Types.BOOLEAN); // To capture the success flag

            stmt.execute();

            // Check if the operation was successful
            if (!stmt.getBoolean(5)) {
                throw new SQLException("Failed to insert dish");
            }

            // Retrieve the last inserted dish ID
            String lastInsertIdQuery = "SELECT LAST_INSERT_ID()";
            try (Statement idStmt = connection.createStatement();
                    ResultSet rs = idStmt.executeQuery(lastInsertIdQuery)) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve dish ID");
                }
            }
        }

    }

    public Dish getDishById(int dishId) throws SQLException {
        String query = "SELECT d.name, d.course_id, d.price, d.preparation_time, d.no_of_stars, d.no_of_reviews " +
                "FROM Dish d WHERE d.id = ?";
        Dish dish = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, dishId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dishName = rs.getString("name");
                    int courseID = rs.getInt("course_id");
                    int dishPrice = rs.getInt("price");
                    int dishPreparationTime = rs.getInt("preparation_time");
                    int dishNumberOfStars = rs.getInt("no_of_stars");
                    int dishNumberOfRatings = rs.getInt("no_of_reviews");

                    ArrayList<String> dishCuisineTags = getTagsByDishId(dishId, "Dish_Cuisine", "Cuisine");
                    ArrayList<String> dishDietTags = getTagsByDishId(dishId, "Dish_Diet", "Diet");

                    dish = new Dish(dishId, dishName, courseID, dishPrice, dishPreparationTime, dishNumberOfStars,
                            dishNumberOfRatings, dishCuisineTags, dishDietTags);
                }
            }
        }
        return dish;
    }

    private static ArrayList<String> getTagsByDishId(int dishId, String tableName, String tagTable)
            throws SQLException {
        ArrayList<String> tags = new ArrayList<>();
        String query = "SELECT t.name FROM " + tableName + " dt " +
                "JOIN " + tagTable + " t ON dt." + tagTable.toLowerCase() + "_id = t.id " +
                "WHERE dt.dish_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, dishId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tags.add(rs.getString(1));
                }
            }
        }
        return tags;
    }

    private int getCuisineIdByName(String name) throws SQLException {
        String query = "SELECT id FROM Cuisine WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Cuisine with name " + name + " not found.");
                }
            }
        }
    }

    private int getDietIdByName(String name) throws SQLException {
        String query = "SELECT id FROM Diet WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Diet with name " + name + " not found.");
                }
            }
        }
    }

    public void printDishFromDB(int dishId) {
        try {
            String dishQuery = "SELECT d.id, d.name, c.name as course_name, d.price, d.preparation_time, d.no_of_stars, d.no_of_reviews "
                    + "FROM Dish d JOIN Course c ON d.course_id = c.id WHERE d.id = ?";
            try (PreparedStatement dishStmt = connection.prepareStatement(dishQuery)) {
                dishStmt.setInt(1, dishId);
                try (ResultSet dishResultSet = dishStmt.executeQuery()) {
                    if (dishResultSet.next()) {
                        String dishName = dishResultSet.getString("name");
                        String courseName = dishResultSet.getString("course_name");
                        int price = dishResultSet.getInt("price");
                        int preparationTime = dishResultSet.getInt("preparation_time");
                        int numberOfStars = dishResultSet.getInt("no_of_stars");
                        int numberOfRatings = dishResultSet.getInt("no_of_reviews");

                        System.out.println("Dish ID: " + dishId);
                        System.out.println("Dish Name: " + dishName);
                        System.out.println("Course Name: " + courseName);
                        System.out.println("Price: " + price);
                        System.out.println("Preparation Time: " + preparationTime);
                        if (numberOfRatings == 0) {
                            System.out.println("This Dish is unrated");
                        } else {
                            double rating = (double) numberOfStars / numberOfRatings;
                            System.out.println("Rating: " + rating);
                        }

                        // System.out.println("Cuisines:");
                        // printTags(dishId, "Dish_Cuisine", "Cuisine");

                        // System.out.println("Diets:");
                        // printTags(dishId, "Dish_Diet", "Diet");
                    } else {
                        System.out.println("Dish not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printTags(int dishId, String tableName, String label) throws SQLException {
        String query = "SELECT t.name FROM " + tableName + " dt " +
                "JOIN " + label + " t ON dt." + label.toLowerCase() + "_id = t.id " +
                "WHERE dt.dish_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, dishId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("name"));
                }
            }
        }
    }

    public void deleteDish(int dishId) throws SQLException {
        String deleteCuisineQuery = "{CALL DeleteFromDishCuisine(?, ?, ?)}";
        String deleteDietQuery = "{CALL DeleteFromDishDiet(?, ?, ?)}";
        String deleteDishQuery = "DELETE FROM Dish WHERE id = ?";

        try (CallableStatement deleteCuisineStmt = connection.prepareCall(deleteCuisineQuery);
                CallableStatement deleteDietStmt = connection.prepareCall(deleteDietQuery);
                PreparedStatement deleteDishStmt = connection.prepareStatement(deleteDishQuery)) {

            deleteCuisineStmt.setInt(1, dishId);
            deleteCuisineStmt.registerOutParameter(3, Types.BOOLEAN);
            deleteCuisineStmt.execute();

            deleteDietStmt.setInt(1, dishId);
            deleteDietStmt.registerOutParameter(3, Types.BOOLEAN);
            deleteDietStmt.execute();

            deleteDishStmt.setInt(1, dishId);
            deleteDishStmt.executeUpdate();
        }
    }
}
