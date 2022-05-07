package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.RestaurantOverView;

import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-04 17:30
 */
public class RestaurantInfoAdminViewModel extends ViewModel {

    private MutableLiveData<List<RestaurantOverView>> restaurantOverViewMutableLiveData;

    public MutableLiveData<List<RestaurantOverView>> getRestaurantOverViewMutableLiveData(){
        if(restaurantOverViewMutableLiveData == null){
            restaurantOverViewMutableLiveData = new MutableLiveData<>();
        }
        return restaurantOverViewMutableLiveData;
    }
}
