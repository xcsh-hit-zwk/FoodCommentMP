package com.example.foodcommentmp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author: zhangweikun
 * @create: 2022-05-05 19:03
 */
public class LabelOverView implements Parcelable {

    private String labelName;
    private String restaurantName;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(labelName);
        parcel.writeString(restaurantName);
    }

    public static final Parcelable.Creator<LabelOverView> CREATOR = new Parcelable.Creator<LabelOverView>(){

        @Override
        public LabelOverView createFromParcel(Parcel parcel) {
            LabelOverView labelOverView = new LabelOverView();
            labelOverView.setLabelName(parcel.readString());
            labelOverView.setRestaurantName(parcel.readString());

            return labelOverView;
        }

        @Override
        public LabelOverView[] newArray(int i) {
            return new LabelOverView[i];
        }
    };
}
