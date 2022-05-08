package com.example.foodcommentmp.pojo;

/**
 * @author: zhangweikun
 * @create: 2022-05-08 16:21
 */
public class UpdateRestaurantOverView {

    private String restaurantId;
    private RestaurantOverView restaurantOverView;

    public UpdateRestaurantOverView() {
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public RestaurantOverView getRestaurantOverView() {
        return restaurantOverView;
    }

    public void setRestaurantOverView(RestaurantOverView restaurantOverView) {
        this.restaurantOverView = restaurantOverView;
    }
}
