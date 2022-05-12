package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.User;

public class UserInfoViewModel extends ViewModel {

    private MutableLiveData<Boolean> getUserSuccessLiveData;

    public MutableLiveData<Boolean> getGetUserSuccessLiveData() {
        if (getUserSuccessLiveData == null){
            getUserSuccessLiveData = new MutableLiveData<>();
            getUserSuccessLiveData.setValue(false);
            return getUserSuccessLiveData;
        }
        return getUserSuccessLiveData;
    }
}