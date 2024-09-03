package com.shailkpatel.cravings.model;

import com.shailkpatel.cravings.util.ArrayList;

public class Dish {
    private int dishId;
    private String dishName;
    private int dishPrice;
    private int dishPreparationTime;
    private int courseID;
    private int dishNumberOfStars;
    private int dishNumberOfRatings;
    private ArrayList<String> dishCuisineTags = new ArrayList<>();
    private ArrayList<String> dishDietTags = new ArrayList<>();

    public Dish() {
    }

    public Dish(int dishId, String dishName, int courseID, int dishPrice, int dishPreparationTime,
            int dishNumberOfStars, int dishNumberOfRatings, ArrayList<String> dishCuisineTags,
            ArrayList<String> dishDietTags) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.courseID = courseID;
        this.dishPrice = dishPrice;
        this.dishPreparationTime = dishPreparationTime;
        this.dishNumberOfStars = dishNumberOfStars;
        this.dishNumberOfRatings = dishNumberOfRatings;
        this.dishCuisineTags = dishCuisineTags;
        this.dishDietTags = dishDietTags;
    }

    public Dish(String dishName, int courseID, int dishPrice, int dishPreparationTime,
            ArrayList<String> dishCuisineTags, ArrayList<String> dishDietTags) {
        this.dishName = dishName;
        this.courseID = courseID;
        this.dishPrice = dishPrice;
        this.dishPreparationTime = dishPreparationTime;
        this.dishCuisineTags = dishCuisineTags;
        this.dishDietTags = dishDietTags;
    }

    // Setters
    public void setId(int dishId) {
        this.dishId = dishId;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setDishCourse(int courseID) {
        this.courseID = courseID;
    }

    public void setDishPrice(int dishPrice) {
        this.dishPrice = dishPrice;
    }

    public void setDishPreparationTime(int dishPreparationTime) {
        this.dishPreparationTime = dishPreparationTime;
    }

    public void setDishCuisineTags(ArrayList<String> dishCuisineTags) {
        this.dishCuisineTags = dishCuisineTags;
    }

    public void setDishDietTags(ArrayList<String> dishDietTags) {
        this.dishDietTags = dishDietTags;
    }

    // Methods to manage dish tags
    public void addDishCuisineTag(String cuisineTag) {
        if (!dishCuisineTags.contains(cuisineTag) && dishCuisineTags.size() < 3) {
            dishCuisineTags.add(cuisineTag);
        }
    }

    public void addDishDietTag(String dietTag) {
        if (!dishDietTags.contains(dietTag) && dishDietTags.size() < 5) {
            dishDietTags.add(dietTag);
        }
    }

    public void removeDishCuisineTag(String cuisineTag) {
        dishCuisineTags.remove(cuisineTag);
    }

    public void removeDishDietTag(String dietTag) {
        dishDietTags.remove(dietTag);
    }

    // Method to handle reviews
    public void addDishReview(int stars) {
        if (stars >= 0 && stars <= 5) {
            this.dishNumberOfStars += stars;
            this.dishNumberOfRatings++;
        } else {
            System.out.println("Invalid rating. Must be between 0 and 5.");
        }
    }

    // Getters
    public int getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public int getDishCourseID() {
        return courseID;
    }

    public ArrayList<String> getDishCuisineTags() {
        return dishCuisineTags;
    }

    public ArrayList<String> getDishDietTags() {
        return dishDietTags;
    }

    public int getDishNumberOfRatings() {
        return dishNumberOfRatings;
    }

    public int getDishPrice() {
        return dishPrice;
    }

    public int getDishPreparationTime() {
        return dishPreparationTime;
    }

    public String getDishRatings() {
        if (this.dishNumberOfRatings == 0) {
            return "No ratings";
        } else {
            return String.format("%.2f", (float) this.dishNumberOfStars / this.dishNumberOfRatings);
        }
    }

    public int getDishNumberOfStars() {
        return this.dishNumberOfStars;
    }

    @Override
    public String toString() {
        return "Dish: " +
                "\nDish ID: " + dishId +
                "\nDish Name: " + dishName +
                "\nCourse: " + courseID +
                "\nPrice: " + dishPrice +
                "\nPreparation Time: " + dishPreparationTime +
                "\nAverage Rating: " + getDishRatings() +
                "\nNumber of Ratings: " + dishNumberOfRatings +
                "\nCuisine Tags: " + dishCuisineTags +
                "\nDiet Tags: " + dishDietTags;
    }

}
