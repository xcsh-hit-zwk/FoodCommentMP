package com.example.foodcommentmp.pojo;

import java.util.Objects;

/**
 * @author: zhangweikun
 * @create: 2022-05-17 17:49
 */
public class LikeFood {

    private String username;
    private String foodName;
    private String restaurantName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeFood likeFood = (LikeFood) o;
        return Objects.equals(username, likeFood.username) && Objects.equals(foodName, likeFood.foodName) && Objects.equals(restaurantName, likeFood.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, foodName, restaurantName);
    }
}
