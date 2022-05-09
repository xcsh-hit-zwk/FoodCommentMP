package com.example.foodcommentmp.ViewModel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-09 16:31
 */
public class FoodInfoAddViewModel extends ViewModel {

    private MutableLiveData<Boolean> addSuccessLiveData;

    public MutableLiveData<Boolean> getAddSuccessLiveData() {
        if(addSuccessLiveData == null){
            addSuccessLiveData = new MutableLiveData<>();
            addSuccessLiveData.setValue(false);
            return addSuccessLiveData;
        }
        return addSuccessLiveData;
    }
}
