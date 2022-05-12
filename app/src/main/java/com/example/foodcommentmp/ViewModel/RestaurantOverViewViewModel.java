package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.RestaurantOverView;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.List;

public class RestaurantOverViewViewModel extends ViewModel {

    private MutableLiveData<Boolean> getSuccessLiveData;
    private MutableLiveData<List<RestaurantOverView>> restaurantOverViewLiveData;

    public MutableLiveData<Boolean> getGetSuccessLiveData() {
        if (getSuccessLiveData == null){
            getSuccessLiveData = new MutableLiveData<>();
            getSuccessLiveData.setValue(false);
            return getSuccessLiveData;
        }
        return getSuccessLiveData;
    }

    public MutableLiveData<List<RestaurantOverView>> getRestaurantOverViewLiveData() {
        if(restaurantOverViewLiveData == null){
            restaurantOverViewLiveData = new MutableLiveData<>();
            restaurantOverViewLiveData.setValue(new ArrayList<>());
            return restaurantOverViewLiveData;
        }
        return restaurantOverViewLiveData;
    }
}