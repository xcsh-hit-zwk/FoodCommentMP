package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-10 11:16
 */
public class RegisterViewModel extends ViewModel {

    private MutableLiveData<Boolean> registerSuccessLiveData;

    public MutableLiveData<Boolean> getRegisterSuccessLiveData() {
        if (registerSuccessLiveData == null){
            registerSuccessLiveData = new MutableLiveData<>();
            registerSuccessLiveData.setValue(false);
            return registerSuccessLiveData;
        }
        return registerSuccessLiveData;
    }
}
