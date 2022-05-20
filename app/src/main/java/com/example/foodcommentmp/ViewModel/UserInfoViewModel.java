package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.UserInfoComment;

import java.util.ArrayList;
import java.util.List;

public class UserInfoViewModel extends ViewModel {

    private MutableLiveData<Boolean> getUserSuccessLiveData;
    private MutableLiveData<List<UserInfoComment>> userInfoCommentLiveData;

    public MutableLiveData<Boolean> getGetUserSuccessLiveData() {
        if (getUserSuccessLiveData == null){
            getUserSuccessLiveData = new MutableLiveData<>();
            getUserSuccessLiveData.setValue(false);
            return getUserSuccessLiveData;
        }
        return getUserSuccessLiveData;
    }

    public MutableLiveData<List<UserInfoComment>> getUserInfoCommentLiveData() {
        if(userInfoCommentLiveData == null){
            userInfoCommentLiveData = new MutableLiveData<>();
            userInfoCommentLiveData.setValue(new ArrayList<>());
            return userInfoCommentLiveData;
        }
        return userInfoCommentLiveData;
    }
}