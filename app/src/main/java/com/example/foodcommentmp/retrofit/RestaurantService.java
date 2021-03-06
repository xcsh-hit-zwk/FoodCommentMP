package com.example.foodcommentmp.retrofit;

import com.example.foodcommentmp.pojo.LikeComment;
import com.example.foodcommentmp.pojo.LikeFood;
import com.example.foodcommentmp.pojo.SearchInfo;

import java.util.List;
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

    @POST("RestaurantOverView/GetRestaurantOverView")
    Call<ResponseBody> getRestaurantOverView(@Body SearchInfo searchInfo);

    @POST("RestaurantDetail/GetRestaurantDetail")
    Call<ResponseBody> getRestaurantDetail(@Body SearchInfo searchInfo);

    @POST("RestaurantDetail/AddFoodLike")
    Call<ResponseBody> addFoodLike(@Body List<LikeFood> likeFoodList);

    @POST("RestaurantDetail/AddCommentLike")
    Call<ResponseBody> addCommentLike(@Body List<LikeComment> likeCommentList);

}
