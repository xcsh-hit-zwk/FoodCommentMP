package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-16 9:06
 */
public class RestaurantDetailViewModel extends ViewModel {

    private MutableLiveData<Boolean> getInfoSuccessLiveData;
    private MutableLiveData<Integer> restaurantLikeLiveData;

    public MutableLiveData<Boolean> getGetInfoSuccessLiveData() {
        if (getInfoSuccessLiveData == null){
            getInfoSuccessLiveData = new MutableLiveData<>();
            getInfoSuccessLiveData.setValue(false);
            return getInfoSuccessLiveData;
        }
        return getInfoSuccessLiveData;
    }

    public MutableLiveData<Integer> getRestaurantLikeLiveData() {
        if (restaurantLikeLiveData == null){
            restaurantLikeLiveData = new MutableLiveData<>();
            restaurantLikeLiveData.setValue(0);
        }
        return restaurantLikeLiveData;
    }

    public void addFoodLike(){
        int likes = restaurantLikeLiveData.getValue();
        restaurantLikeLiveData.setValue(likes+1);
    }

    public void cancelFoodLile(){
        int likes = restaurantLikeLiveData.getValue();
        restaurantLikeLiveData.setValue(likes-1);
    }
}
