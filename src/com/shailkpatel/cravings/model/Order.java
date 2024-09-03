package com.shailkpatel.cravings.model;

import java.time.LocalDateTime;
import com.shailkpatel.cravings.util.ArrayList;

public class Order {

    private int orderID;
    private LocalDateTime timestamp;
    private int customerID;
    private int restaurantID;
    private String deliveryAddress;
    private double totalAmount;
    private ArrayList<Dish> dishes;
    private boolean reviewed;

    // Default constructor
    public Order() {
    }

    // Parameterized constructor
    public Order(int orderId, int customerID, int restaurantID, ArrayList<Dish> dishes,
            String deliveryAddress, boolean reviewed, LocalDateTime timestamp) {
        this.orderID = orderId;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.timestamp = timestamp;
        this.dishes = dishes;
        this.totalAmount = calculateTotalAmount();
        this.deliveryAddress = deliveryAddress;
        this.reviewed = reviewed;
    }

    // Calculate total amount for the order
    private double calculateTotalAmount() {
        double total = 0.0;
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            total += dish.getDishPrice();
        }
        return total;
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
        this.totalAmount = calculateTotalAmount(); // Recalculate total
    }

    @Override
    public String toString() {
        System.out.println("Order:");
        System.out.println("Order ID: " + orderID);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Customer ID: " + customerID);
        System.out.println("Restaurant ID: " + restaurantID);
        System.out.println("Delivery Address: " + deliveryAddress);
        System.out.println("Total Amount: $" + String.format("%.2f", totalAmount));
        System.out.println("Reviewed: " + reviewed);
        System.out.println("Dishes:");
        dishes.display();

        return "";
    }
}
