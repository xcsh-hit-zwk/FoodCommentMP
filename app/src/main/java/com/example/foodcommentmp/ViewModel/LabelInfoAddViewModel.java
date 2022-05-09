package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-09 17:34
 */
public class LabelInfoAddViewModel extends ViewModel {

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
