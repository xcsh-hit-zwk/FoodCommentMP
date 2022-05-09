package com.example.foodcommentmp.pojo;

/**
 * @author: zhangweikun
 * @create: 2022-05-09 8:51
 */
public class UpdateFoodOverView {

    private String foodId;
    private FoodOverView foodOverView;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public FoodOverView getFoodOverView() {
        return foodOverView;
    }

    public void setFoodOverView(FoodOverView foodOverView) {
        this.foodOverView = foodOverView;
    }
}
