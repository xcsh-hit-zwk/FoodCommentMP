package com.example.foodcommentmp.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author: zhangweikun
 * @create: 2022-05-07 9:28
 */

public interface AdminInfoService {

    // 获取全部RestaurantOverView的接口
    @GET("RestaurantOverView/GetTotalRestaurantOverView")
    Call<ResponseBody> getTotalRestaurantOverView();

    @GET("RestaurantOverView/GetTotalFoodOverView")
    Call<ResponseBody> getTotalFoodOverView();

    @GET("RestaurantOverView/GetTotalLabelOverView")
    Call<ResponseBody> getTotalLabelOverView();

}
