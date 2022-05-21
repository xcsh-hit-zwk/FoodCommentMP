package com.example.foodcommentmp.retrofit;

import com.example.foodcommentmp.pojo.Account;
import com.example.foodcommentmp.pojo.RegisterAccount;
import com.example.foodcommentmp.pojo.SearchInfo;
import com.example.foodcommentmp.pojo.User;
import com.example.foodcommentmp.pojo.UserInfo;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @description: User类和Account类的网络传输接口
 * @author: zhangweikun
 * @create: 2022-04-12 16:24
 **/
public interface UserService {

    @POST("User/Login")
    Call<ResponseBody> checkLoginAccount(@Body Account account);

    @POST("User/SignUp")
    Call<ResponseBody> checkSignup(@Body RegisterAccount registerAccount);

    @POST("User/GetUser")
    Call<ResponseBody> getUser(@Body Account account);

    @POST("User/UpdateUserInfo")
    Call<ResponseBody> updateUserInfo(@Body UserInfo userInfo);

    // searchWay = "ModifyUserComment", info = "%username"
    @POST("User/ModifyUserComment")
    Call<ResponseBody> getUserComment(@Body SearchInfo searchInfo);

    // searchWay = "GetUserImage", info = "%username"
    @POST("User/GetUserImage")
    Call<ResponseBody> getUserImage(@Body SearchInfo searchInfo);

}
