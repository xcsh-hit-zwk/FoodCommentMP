package com.example.foodcommentmp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.foodcommentmp.R;

/**
 * @author: zhangweikun
 * @create: 2022-05-05 9:44
 */
public class RestaurantOverView implements Parcelable {

    private String restaurantName;
    private Integer likes;
    private String restaurantTag;
    private String restaurantPosition;
    private String restaurantImage;
    private String restaurantProvince;
    private String restaurantCity;
    private String restaurantBlock;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getRestaurantTag() {
        return restaurantTag;
    }

    public void setRestaurantTag(String restaurantTag) {
        this.restaurantTag = restaurantTag;
    }

    public String getRestaurantPosition() {
        return restaurantPosition;
    }

    public void setRestaurantPosition(String restaurantPosition) {
        this.restaurantPosition = restaurantPosition;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public String getRestaurantProvince() {
        return restaurantProvince;
    }

    public void setRestaurantProvince(String restaurantProvince) {
        this.restaurantProvince = restaurantProvince;
    }

    public String getRestaurantCity() {
        return restaurantCity;
    }

    public void setRestaurantCity(String restaurantCity) {
        this.restaurantCity = restaurantCity;
    }

    public String getRestaurantBlock() {
        return restaurantBlock;
    }

    public void setRestaurantBlock(String restaurantBlock) {
        this.restaurantBlock = restaurantBlock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(restaurantName);
        parcel.writeInt(likes);
        parcel.writeString(restaurantTag);
        parcel.writeString(restaurantPosition);
        parcel.writeString(restaurantImage);
        parcel.writeString(restaurantProvince);
        parcel.writeString(restaurantCity);
        parcel.writeString(restaurantBlock);
    }

    public static final Parcelable.Creator<RestaurantOverView> CREATOR =
            new Parcelable.Creator<RestaurantOverView>(){
        @Override
        public RestaurantOverView createFromParcel(Parcel source){
            RestaurantOverView restaurantOverView = new RestaurantOverView();
            restaurantOverView.setRestaurantName(source.readString());
            restaurantOverView.setLikes(source.readInt());
            restaurantOverView.setRestaurantTag(source.readString());
            restaurantOverView.setRestaurantPosition(source.readString());
            restaurantOverView.setRestaurantImage(source.readString());
            restaurantOverView.setRestaurantProvince(source.readString());
            restaurantOverView.setRestaurantCity(source.readString());
            restaurantOverView.setRestaurantBlock(source.readString());

            return restaurantOverView;
        }

        @Override
        public RestaurantOverView[] newArray(int size){
            return new RestaurantOverView[size];
        }
    };
}
