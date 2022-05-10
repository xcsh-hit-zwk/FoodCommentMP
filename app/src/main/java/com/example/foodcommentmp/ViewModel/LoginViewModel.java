package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-10 11:16
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> checkLoginLiveData;

    public MutableLiveData<Boolean> getCheckLoginLiveData() {
        if(checkLoginLiveData == null){
            checkLoginLiveData = new MutableLiveData<>();
            checkLoginLiveData.setValue(false);
            return checkLoginLiveData;
        }
        return checkLoginLiveData;
    }
}
