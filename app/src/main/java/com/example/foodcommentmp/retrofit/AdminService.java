package com.example.foodcommentmp.retrofit;

import com.example.foodcommentmp.pojo.AdminAccount;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * description:
 * @author: zhangweikun
 * @create: 2022-04-30 10:07
 */
public interface AdminService {

    @POST("Admin/Login")
    Call<ResponseBody> checkLogin(@Body AdminAccount adminAccount);
}
