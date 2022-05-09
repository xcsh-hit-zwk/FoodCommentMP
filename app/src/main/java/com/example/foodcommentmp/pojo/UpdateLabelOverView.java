package com.example.foodcommentmp.pojo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author: zhangweikun
 * @create: 2022-05-09 8:52
 */
public class UpdateLabelOverView extends ViewModel {

    private String labelId;
    private LabelOverView labelOverView;

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public LabelOverView getLabelOverView() {
        return labelOverView;
    }

    public void setLabelOverView(LabelOverView labelOverView) {
        this.labelOverView = labelOverView;
    }
}
