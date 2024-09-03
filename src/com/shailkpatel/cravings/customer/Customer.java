package com.shailkpatel.cravings.customer;

import com.shailkpatel.cravings.model.Order;
import com.shailkpatel.cravings.util.LinkedList;
import com.shailkpatel.cravings.util.Stack;
import com.shailkpatel.cravings.model.Dish;

public class Customer {

    private int customerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean isActive;
    private boolean isOnline;

    private String[] addresses = new String[5]; // Array to hold up to 5 addresses
    private String favoriteFood;

    private LinkedList<Order> orders = new LinkedList<>();
    private Stack<Dish> cart = new Stack<>();

    public Customer() {
        // Default constructor
    }

    public Customer(int customerId, String name, String phoneNumber, String email, String password,
            String[] addresses, String favoriteFood, LinkedList<Order> orders, Stack<Dish> cart) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.isOnline = true;
        this.addresses = addresses;
        this.favoriteFood = favoriteFood;
        this.orders = orders;
        this.cart = cart;
    }

    public Customer(String name, String phoneNumber, String email, String password, String[] addresses,
            String favoriteFood) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.addresses = addresses;
        this.favoriteFood = favoriteFood;
    }

    // Methods to view, add, and manage orders
    public void viewOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders to display.");
        } else {
            orders.display();
        }
    }

    public void addOrder(Order order) {
        if (order != null) {
            orders.addFirst(order);
            System.out.println("Order added successfully.");
        } else {
            System.out.println("Invalid order.");
        }
    }

    // Methods to manage the cart
    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            cart.viewStack();
        }
    }

    public void addCart(Dish dish) {
        if (dish != null) {
            cart.push(dish);
            System.out.println("Dish added to cart.");
        } else {
            System.out.println("Invalid dish.");
        }
    }

    public void removeDishFromCart() {
        if (!cart.isEmpty()) {
            Dish removedDish = cart.pop();
            System.out.println("Removed dish from cart: " + removedDish.getDishName());
        } else {
            System.out.println("Cart is already empty.");
        }
    }

    public void deleteAccount() {
        this.isActive = false;
        System.out.println("Account deleted (or marked as inactive).");
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String[] getAddresses() {
        return addresses;
    }

    public void setAddresses(String[] addresses) {
        if (addresses.length <= 5) {
            this.addresses = addresses;
        } else {
            System.out.println("Cannot exceed 5 addresses.");
        }
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public LinkedList<Order> getOrders() {
        return orders;
    }

    public Stack<Dish> getCart() {
        return cart;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer: ")
                .append("\nCustomer ID: ").append(customerId)
                .append("\nName: ").append(name)
                .append("\nPhone Number: ").append(phoneNumber)
                .append("\nEmail: ").append(email)
                .append("\nActive: ").append(isActive)
                .append("\nOnline: ").append(isOnline)
                .append("\nAddresses: ");

        if (addresses.length == 0) {
            sb.append("No addresses set.");
        } else {
            for (String address : addresses) {
                sb.append(address).append(", ");
            }
        }

        sb.append("\nFavorite Food: ").append(favoriteFood);
        return sb.toString();
    }
}
