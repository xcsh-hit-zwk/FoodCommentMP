package com.example.foodcommentmp.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author: zhangweikun
 * @create: 2022-05-12 9:36
 */

public interface RestaurantService {

    @POST("RestaurantOverView/GetCity")
    Call<ResponseBody> getCity(@Body Map<String, String> city);


}
