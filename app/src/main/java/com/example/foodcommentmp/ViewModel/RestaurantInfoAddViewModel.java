package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-07 18:10
 */
public class RestaurantInfoAddViewModel extends ViewModel {

    private MutableLiveData<Boolean> addSuccessLiveData;

    public MutableLiveData<Boolean> getAddSuccessLiveData(){
        if(addSuccessLiveData == null){
            addSuccessLiveData = new MutableLiveData<>();
            addSuccessLiveData.setValue(false);
            return addSuccessLiveData;
        }
        return addSuccessLiveData;
    }

}
