package com.example.foodcommentmp.pojo;

/**
 * @author: zhangweikun
 * @create: 2022-05-17 17:45
 */
public class FoodLiked {

    private String userfoodlikeId;
    private String userId;
    private String foodId;
    private String restaurantId;

    public String getUserfoodlikeId() {
        return userfoodlikeId;
    }

    public void setUserfoodlikeId(String userfoodlikeId) {
        this.userfoodlikeId = userfoodlikeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
