package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-20 10:03
 */
public class CommentModifyViewModel extends ViewModel {
    private MutableLiveData<Boolean> getBaseInfoSuccessLiveData;

    public MutableLiveData<Boolean> getGetBaseInfoSuccessLiveData() {
        if (getBaseInfoSuccessLiveData == null){
            getBaseInfoSuccessLiveData = new MutableLiveData<>();
            getBaseInfoSuccessLiveData.setValue(false);
            return getBaseInfoSuccessLiveData;
        }
        return getBaseInfoSuccessLiveData;
    }
}
