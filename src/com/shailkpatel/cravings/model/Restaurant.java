package com.shailkpatel.cravings.model;

import com.shailkpatel.cravings.util.ArrayList;
import com.shailkpatel.cravings.util.LinkedList;

public class Restaurant {

    private int restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private int themeID;
    private int numberOfStars;
    private int numberOfReviews;
    private boolean isAcceptingOrders;
    private String rCode;

    private LinkedList<Dish> restaurantMenu = new LinkedList<>();

    public Restaurant() {
    }

    public Restaurant(int restaurantId, String restaurantName, String restaurantAddress, int themeID,
            LinkedList<Dish> restaurantMenu, int numberOfReviews, int numberOfStars, String rCode) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.themeID = themeID;
        this.restaurantMenu = restaurantMenu;
        this.numberOfReviews = numberOfReviews;
        this.numberOfStars = numberOfStars;
        this.isAcceptingOrders = true;
        this.rCode = rCode;
    }

    public Restaurant(String restaurantName, String restaurantAddress, int themeID, String rCode) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.themeID = themeID;
        this.rCode = rCode;
    }

    public Restaurant(int id, String name, String address, int themeID2, java.util.LinkedList<Dish> restaurantMenu2,
            int numberOfReviews2, int numberOfStars2, String rCode2) {
        //TODO Auto-generated constructor stub
    }

    public void printThemes(ArrayList<String> themeList) {
        System.out.println("Available Themes:");
        for (int i = 0; i < themeList.size(); i++) {
            System.out.println((i + 1) + ". " + themeList.get(i));
        }
    }

    public void addToDishMenu(Dish dish) {
        restaurantMenu.addLast(dish);
    }

    public void printRestaurant() {
        System.out.println(this);
    }

    // public void printMenu() {
    // for (Dish dish : restaurantMenu) {
    // System.out.println(dish);
    // }
    // if (restaurantMenu.isEmpty()) {
    // System.out.println("Menu is empty.");
    // }
    // }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public int getThemeID() {
        return themeID;
    }

    public LinkedList<Dish> getRestaurantMenu() {
        return restaurantMenu;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public boolean getAcceptingOrders() {
        return isAcceptingOrders;
    }

    public String getRating() {
        if (numberOfReviews == 0) {
            return "This restaurant is not rated";
        } else {
            return "Rating: " + (double) numberOfStars / numberOfReviews;
        }
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setRestaurantThemeID(int themeID) {
        this.themeID = themeID;
    }

    public void setNumberOfStars(int numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public void setRestaurantMenu(LinkedList<Dish> restaurantMenu) {
        this.restaurantMenu = restaurantMenu;
    }

    public void setAcceptingOrders(boolean isAcceptingOrders) {
        this.isAcceptingOrders = isAcceptingOrders;
    }

    public String getrCode() {
        return rCode;
    }

    public void setrCode(String rCode) {
        this.rCode = rCode;
    }

    @Override
    public String toString() {
        return "Restaurant Details: \n" +
                "ID: " + restaurantId + "\n" +
                "Name: " + restaurantName + "\n" +
                "Address: " + restaurantAddress + "\n" +
                "Theme: " + themeID + "\n" +
                "Active: " + isAcceptingOrders + "\n" +
                getRating() + "\n" +
                "Menu: " + (restaurantMenu.isEmpty() ? "Menu is empty." : restaurantMenu) + "\n";
    }

    public void setId(int id) {
        this.restaurantId = id;
    }
}
