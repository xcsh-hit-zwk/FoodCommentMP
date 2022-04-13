package com.example.foodcommentmp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.pojo.User;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> usernameLiveData;
    private MutableLiveData<String> passwordLiveData;
    private MutableLiveData<String> nicknameLiveData;

    private MutableLiveData<Boolean> hasRegisterLiveData;

    public RegisterViewModel(){
        usernameLiveData = new MutableLiveData<>();
        usernameLiveData.setValue("");
        passwordLiveData = new MutableLiveData<>();
        passwordLiveData.setValue("");
        nicknameLiveData = new MutableLiveData<>();
        nicknameLiveData.setValue("");

        hasRegisterLiveData = new MutableLiveData<>();
        hasRegisterLiveData.setValue(false);
    }

    public MutableLiveData<String> getUsernameLiveData() {
        return usernameLiveData;
    }

    public MutableLiveData<String> getPasswordLiveData() {
        return passwordLiveData;
    }

    public MutableLiveData<String> getNicknameLiveData() {
        return nicknameLiveData;
    }

    public MutableLiveData<Boolean> getHasRegisterLiveData() {
        return hasRegisterLiveData;
    }

    public void recodeUserName(String username){
        usernameLiveData.setValue(username);
    }

    public void recodePassWord(String password){
        passwordLiveData.setValue(password);
    }

    public void recodeNickName(String nickname){
        nicknameLiveData.setValue(nickname);
    }

    public void record(String username, String password, String nickname){
        usernameLiveData.setValue(username);
        passwordLiveData.setValue(password);
        if(nickname.equals("")){
            nickname = username;
        }
        nicknameLiveData.setValue(nickname);

        checkSignUp();
    }

    private void checkSignUp(){
        User user = new User(this.usernameLiveData.getValue(),
                this.passwordLiveData.getValue(), this.nicknameLiveData.getValue());
        // Retrofit 网络传输
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.2.2:8080/")
                .build();
        try{
            UserService userService = retrofit.create(UserService.class);
            Log.i("注册", "创建服务成功");
            Call<ResponseBody> call = userService.checkSignup(user);
            Log.i("注册", "网络post成功");
            // 用法和OkHttp的call如出一辙
            // 不同的是如果是Android系统回调方法执行在主线程
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if(response.body() == null){
                            Log.i("注册", "body为空");
                        }
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        boolean answer = jsonObject.getBooleanValue("success");
                        Log.i("注册", "注册结果为: "+String.valueOf(answer));
                        hasRegisterLiveData.setValue(answer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void reset(){
        usernameLiveData.setValue("");
        passwordLiveData.setValue("");
        nicknameLiveData.setValue("");
    }
}
