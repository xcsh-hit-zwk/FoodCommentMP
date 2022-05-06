package com.example.foodcommentmp.pojo;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: zhangweikun
 * @create: 2022-05-05 16:33
 */
public class FoodOverView implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodName);
        parcel.writeInt(foodLikes);
        parcel.writeString(restaurantName);
        parcel.writeString(foodImage);
    }

    public static final Parcelable.Creator<FoodOverView> CREATOR = new Parcelable.Creator<FoodOverView>(){

        @Override
        public FoodOverView createFromParcel(Parcel parcel) {
            FoodOverView foodOverView = new FoodOverView();
            foodOverView.setFoodName(parcel.readString());
            foodOverView.setFoodLikes(parcel.readInt());
            foodOverView.setRestaurantName(parcel.readString());
            foodOverView.setFoodImage(parcel.readString());

            return foodOverView;
        }

        @Override
        public FoodOverView[] newArray(int i) {
            return new FoodOverView[i];
        }
    };
}
