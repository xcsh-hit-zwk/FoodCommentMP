package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.LabelOverView;

import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-04 17:31
 */
public class LabelInfoAdminViewModel extends ViewModel {

    private MutableLiveData<List<LabelOverView>> labelOverViewMutableLiveData;

    public MutableLiveData<List<LabelOverView>> getLabelOverViewMutableLiveData(){
        if(labelOverViewMutableLiveData == null){
            labelOverViewMutableLiveData = new MutableLiveData<>();
        }
        return labelOverViewMutableLiveData;
    }
}
