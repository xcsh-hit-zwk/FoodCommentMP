package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-13 16:15
 */
public class ModifyUserInfoViewModel extends ViewModel {

    private MutableLiveData<Boolean> updateUserInfoLiveData;

    public MutableLiveData<Boolean> getUpdateUserInfoLiveData() {
        if (updateUserInfoLiveData == null){
            updateUserInfoLiveData = new MutableLiveData<>();
            updateUserInfoLiveData.setValue(false);
            return updateUserInfoLiveData;
        }
        return updateUserInfoLiveData;
    }
}
