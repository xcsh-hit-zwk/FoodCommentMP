package com.example.foodcommentmp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcommentmp.pojo.UpdateFoodOverView;
import com.example.foodcommentmp.pojo.UpdateRestaurantOverView;

/**
 * @author: zhangweikun
 * @create: 2022-05-09 8:49
 */
public class FoodInfoUpdateViewModel extends ViewModel {

    private MutableLiveData<Boolean> deleteSuccessLiveData;
    private MutableLiveData<Boolean> updateSuccessLiveData;
    private MutableLiveData<Boolean> showDialogLiveData;

    private MutableLiveData<UpdateFoodOverView> foodOverViewLiveData;

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

    public MutableLiveData<Boolean> getShowDialogLiveData() {
        if(showDialogLiveData == null){
            showDialogLiveData = new MutableLiveData<>();
            showDialogLiveData.setValue(false);
            return showDialogLiveData;
        }
        return showDialogLiveData;
    }

    public MutableLiveData<UpdateFoodOverView> getFoodOverViewLiveData() {
        if(foodOverViewLiveData == null){
            foodOverViewLiveData = new MutableLiveData<>();
            foodOverViewLiveData.setValue(new UpdateFoodOverView());
            return foodOverViewLiveData;
        }
        return foodOverViewLiveData;
    }
}
