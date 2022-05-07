package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.FoodOverView;

import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-04 17:30
 */
public class FoodInfoAdminViewModel extends ViewModel {

    private MutableLiveData<List<FoodOverView>> foodOverViewMutableLiveData;

    public MutableLiveData<List<FoodOverView>> getFoodOverViewMutableLiveData(){
        if(foodOverViewMutableLiveData == null){
            foodOverViewMutableLiveData = new MutableLiveData<>();
        }
        return foodOverViewMutableLiveData;
    }
}
