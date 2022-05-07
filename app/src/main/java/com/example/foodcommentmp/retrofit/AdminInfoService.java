package com.example.foodcommentmp.retrofit;

import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.LabelOverView;
import com.example.foodcommentmp.pojo.RestaurantOverView;

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

    // todo 这三个接口的前后端还没写
    @POST("AdminInfo/AddRestaurant")
    Call<ResponseBody> addRestaurant(@Body RestaurantOverView restaurantOverView);

    @POST("AdminInfo/AddFood")
    Call<ResponseBody> addFood(@Body FoodOverView foodOverView);

    @POST("AdminInfo/AddLabel")
    Call<ResponseBody> addLabel(@Body LabelOverView labelOverView);

}
