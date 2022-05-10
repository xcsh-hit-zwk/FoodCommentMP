package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-10 10:32
 */
public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> loginSuccessLiveData;
    private MutableLiveData<Boolean> waitLiveData;

    public MutableLiveData<Boolean> getLoginSuccessLiveData() {
        if(loginSuccessLiveData == null){
            loginSuccessLiveData = new MutableLiveData<>();
            loginSuccessLiveData.setValue(false);
            return loginSuccessLiveData;
        }
        return loginSuccessLiveData;
    }

    public MutableLiveData<Boolean> getWaitLiveData() {
        if(waitLiveData == null){
            waitLiveData = new MutableLiveData<>();
            waitLiveData.setValue(false);
            return waitLiveData;
        }
        return waitLiveData;
    }
}
