package com.example.foodcommentmp.ViewModel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-08 10:09
 */
public class RestaurantInfoUpdateViewModel extends ViewModel {

    private MutableLiveData<Boolean> deleteSuccessLiveData;
    private MutableLiveData<Boolean> updateSuccessLiveData;

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
}
