package com.example.foodcommentmp.ViewModel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.UpdateRestaurantOverView;

/**
 * @author: zhangweikun
 * @create: 2022-05-08 10:09
 */
public class RestaurantInfoUpdateViewModel extends ViewModel {

    private MutableLiveData<Boolean> deleteSuccessLiveData;
    private MutableLiveData<Boolean> updateSuccessLiveData;

    private MutableLiveData<UpdateRestaurantOverView> restaurantOverViewLiveData;

    public MutableLiveData<Boolean> getDeleteSuccessLiveData(){
        if(deleteSuccessLiveData == null){
            deleteSuccessLiveData = new MutableLiveData<>();
            deleteSuccessLiveData.setValue(false);
            return deleteSuccessLiveData;
        }
        return deleteSuccessLiveData;
    }

     public MutableLiveData<Boolean> getUpdateSuccessLiveData(){
        if(updateSuccessLiveData == null){
            updateSuccessLiveData = new MutableLiveData<>();
            updateSuccessLiveData.setValue(false);
            return updateSuccessLiveData;
        }
        return updateSuccessLiveData;
     }

    public MutableLiveData<UpdateRestaurantOverView> getRestaurantOverViewLiveData() {
        if(restaurantOverViewLiveData == null){
            restaurantOverViewLiveData = new MutableLiveData<>();
            restaurantOverViewLiveData.setValue(new UpdateRestaurantOverView());
            return restaurantOverViewLiveData;
        }
        return restaurantOverViewLiveData;
    }
}
