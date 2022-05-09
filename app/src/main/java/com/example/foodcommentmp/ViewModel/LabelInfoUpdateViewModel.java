package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.LabelOverView;
import com.example.foodcommentmp.pojo.UpdateLabelOverView;

/**
 * @author: zhangweikun
 * @create: 2022-05-09 8:54
 */
public class LabelInfoUpdateViewModel extends ViewModel {

    private MutableLiveData<Boolean> deleteSuccessLiveData;
    private MutableLiveData<Boolean> updateSuccessLiveData;

    private MutableLiveData<UpdateLabelOverView> labelOverViewLiveData;

    public MutableLiveData<Boolean> getDeleteSuccessLiveData() {
        if(deleteSuccessLiveData == null){
            deleteSuccessLiveData = new MutableLiveData<>();
            deleteSuccessLiveData.setValue(false);
            return deleteSuccessLiveData;
        }
        return deleteSuccessLiveData;
    }

    public MutableLiveData<Boolean> getUpdateSuccessLiveData() {
        if(updateSuccessLiveData == null){
            updateSuccessLiveData = new MutableLiveData<>();
            updateSuccessLiveData.setValue(false);
            return updateSuccessLiveData;
        }
        return updateSuccessLiveData;
    }

    public MutableLiveData<UpdateLabelOverView> getLabelOverViewLiveData() {
        if(labelOverViewLiveData == null){
            labelOverViewLiveData = new MutableLiveData<>();
            labelOverViewLiveData.setValue(new UpdateLabelOverView());
            return labelOverViewLiveData;
        }
        return labelOverViewLiveData;
    }
}
