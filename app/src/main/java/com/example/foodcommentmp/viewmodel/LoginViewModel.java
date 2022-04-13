package com.example.foodcommentmp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.pojo.Account;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> usernameLiveData;
    private MutableLiveData<String> passwordLiveData;
    private MutableLiveData<Boolean> hasLoginLivaData;

    public LoginViewModel(){
        usernameLiveData = new MutableLiveData<>();
        usernameLiveData.setValue("");
        passwordLiveData = new MutableLiveData<>();
        passwordLiveData.setValue("");
        hasLoginLivaData = new MutableLiveData<>();
        hasLoginLivaData.setValue(false);
    }

    public MutableLiveData<String> getUsernameLiveData() {
        return usernameLiveData;
    }

    public MutableLiveData<String> getPasswordLiveData() {
        return passwordLiveData;
    }
    public MutableLiveData<Boolean> getHasLoginLivaData() {
        return hasLoginLivaData;
    }


    public void recodeUserName(String username){
        usernameLiveData.setValue(username);
    }

    public void recodePassWord(String password){
        passwordLiveData.setValue(password);
    }

    public void record(String username, String password){
        usernameLiveData.setValue(username);
        passwordLiveData.setValue(password);

        checkLogin();
        Log.i("登录信息", "账号为: " + usernameLiveData.getValue());
        Log.i("登录信息", "密码为: " + passwordLiveData.getValue());

    }

    private void checkLogin(){
        Account account = new Account(this.usernameLiveData.getValue(),
                this.passwordLiveData.getValue());
        // Retrofit 网络传输
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.2.2:8080/")
                .build();
        UserService userService = retrofit.create(UserService.class);
        Call<ResponseBody> call = userService.checkLoginAccount(account);
        // 用法和OkHttp的call如出一辙
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    boolean answer = jsonObject.getBooleanValue("success");
                    Log.i("json结果", "登录结果为: " + String.valueOf(answer));
                    hasLoginLivaData.setValue(answer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void reset(){
        usernameLiveData.setValue("");
        passwordLiveData.setValue("");
    }
}
