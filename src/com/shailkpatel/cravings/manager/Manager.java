package com.shailkpatel.cravings.manager;

public class Manager {

    private int managerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean isActive;
    private boolean isOnline;
    private String favoriteFood;

    private int restaurantID;

    public Manager() {
    }

    public Manager(int managerId, String name, String phoneNumber, String email, String password,
            int restaurantID, String favoriteFood) {
        this.managerId = managerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.restaurantID = restaurantID;
        this.favoriteFood = favoriteFood;

        this.isActive = true;
        this.isOnline = true;
    }

    public Manager(String name, String phoneNumber, String email, String password, int restaurantID,
            String favoriteFood) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.restaurantID = restaurantID;
        this.favoriteFood = favoriteFood;
    }

    public void printRestaurantManager() {
        System.out.println(this.toString());
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public void deleteManager() {
        this.isActive = false;
        this.isOnline = false;
    }

    // Getters and Setters
    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // public void setPhoneNumber() {
    // this.phoneNumber = phoneNumber;
    // }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    @Override
    public String toString() {
        return "RestaurantManager: " +
                "\nManager ID: " + managerId +
                "\nName: " + name +
                "\nPhoneNumber: " + phoneNumber +
                "\nEmail: " + email + '\'' +
                "\nisActive: " + isActive +
                "\nisOnline: " + isOnline +
                "\nRestaurantID " + restaurantID;
    }
}
