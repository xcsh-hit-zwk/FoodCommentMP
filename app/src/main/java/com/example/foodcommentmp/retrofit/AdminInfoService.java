package com.example.foodcommentmp.retrofit;

import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.LabelOverView;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.UpdateFoodOverView;
import com.example.foodcommentmp.pojo.UpdateLabelOverView;
import com.example.foodcommentmp.pojo.UpdateRestaurantOverView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author: zhangweikun
 * @create: 2022-05-07 9:28
 */

public interface AdminInfoService {

    // 获取全部RestaurantOverView的接口
    @GET("AdminInfo/GetTotalRestaurantOverView")
    Call<ResponseBody> getTotalRestaurantOverView();

    @GET("AdminInfo/GetTotalFoodOverView")
    Call<ResponseBody> getTotalFoodOverView();

    @GET("AdminInfo/GetTotalLabelOverView")
    Call<ResponseBody> getTotalLabelOverView();

    @POST("AdminInfo/AddRestaurant")
    Call<ResponseBody> addRestaurant(@Body RestaurantOverView restaurantOverView);

    @POST("AdminInfo/AddFood")
    Call<ResponseBody> addFood(@Body FoodOverView foodOverView);

    @POST("AdminInfo/AddLabel")
    Call<ResponseBody> addLabel(@Body LabelOverView labelOverView);

    @POST("AdminInfo/DeleteRestaurant")
    Call<ResponseBody> deleteRestaurant(@Body RestaurantOverView restaurantOverView);

    @POST("AdminInfo/DeleteFood")
    Call<ResponseBody> deleteFood(@Body FoodOverView foodOverView);

    @POST("AdminInfo/DeleteLabel")
    Call<ResponseBody> deleteLabel(@Body LabelOverView labelOverView);

    @POST("AdminInfo/GetUpdateRestaurantId")
    Call<ResponseBody> getUpdateRestaurantId(@Body RestaurantOverView restaurantOverView);
    @POST("AdminInfo/UpdateRestaurant")
    Call<ResponseBody> updateRestaurant(@Body UpdateRestaurantOverView updateRestaurantOverView);

    @POST("AdminInfo/GetUpdateFoodId")
    Call<ResponseBody> getUpdateFoodId(@Body FoodOverView foodOverView);
    @POST("AdminInfo/UpdateFood")
    Call<ResponseBody> updateFood(@Body UpdateFoodOverView updateFoodOverView);

    @POST("AdminInfo/GetUpdateLabelId")
    Call<ResponseBody> getUpdateLabelId(@Body LabelOverView labelOverView);
    @POST("AdminInfo/UpdateLabel")
    Call<ResponseBody> updateLabel(@Body UpdateLabelOverView updateLabelOverView);

}
