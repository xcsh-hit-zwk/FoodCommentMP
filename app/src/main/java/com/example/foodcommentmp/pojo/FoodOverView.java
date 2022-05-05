package com.example.foodcommentmp.pojo;

import android.content.Intent;

/**
 * @author: zhangweikun
 * @create: 2022-05-05 16:33
 */
public class FoodOverView {

    private String foodName;
    private Integer foodLikes;
    private String restaurantName;
    private String foodImage;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getFoodLikes() {
        return foodLikes;
    }

    public void setFoodLikes(Integer foodLikes) {
        this.foodLikes = foodLikes;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
