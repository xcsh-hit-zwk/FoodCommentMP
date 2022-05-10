package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-10 17:04
 */
public class AdminLoginViewModel extends ViewModel {

    private MutableLiveData<Boolean> loginSuccessLiveData;

    public MutableLiveData<Boolean> getLoginSuccessLiveData() {
        if(loginSuccessLiveData == null){
            loginSuccessLiveData = new MutableLiveData<>();
            loginSuccessLiveData.setValue(false);
            return loginSuccessLiveData;
        }
        return loginSuccessLiveData;
    }

}
