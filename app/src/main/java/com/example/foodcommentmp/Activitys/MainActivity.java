package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.MainActivityViewModel;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.pojo.Account;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.File;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ImageView background;

    private TextView greetingsTextView;

    private Calendar calendar;

    private SharedPreferences userAccount;

    private MainActivityViewModel mainActivityViewModel;

    private int FLAG = 0;

    private static final int delayTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        greetingsTextView = findViewById(R.id.greetings_text_view);
        background = findViewById(R.id.main_background_image_view);

        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 8){
            greetingsTextView.setText("早上好");
        }
        else if (hour >= 8 && hour < 12){
            greetingsTextView.setText("上午好");
        }
        else if (hour >= 12 && hour < 14){
            greetingsTextView.setText("中午好");
        }
        else if (hour >= 14 && hour < 17){
            greetingsTextView.setText("下午好");
        }
        else if (hour >= 17 && hour < 24){
            greetingsTextView.setText("晚上好");
        }
        else if (hour >= 24 && hour < 6){
            greetingsTextView.setText("该休息啦！");
        }


        File file = new File(ImageConfig.DIR + "/background/daotian.jpg");
        // Glide加载图片
        Glide.with(this)
                .load(file)
                .centerCrop()
                .into(background);

        userAccount = this.getSharedPreferences("user_account", MODE_PRIVATE);

        // 如果不存在本地账户，则进行登录/注册
        String username = userAccount.getString("username", "");
        String password = userAccount.getString("password", "");
        Log.i("自动登录", username);
        Log.i("自动登录", password);

        if (!"".equals(username) && !"".equals(password)){

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ServerConfig.BASE_URL)
                    .build();
            UserService userService = retrofit.create(UserService.class);

            Account account = new Account();
            account.setUsername(username);
            account.setPassword(MD5.string2MD5(password));

            Call<ResponseBody> call = userService.checkLoginAccount(account);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        Boolean success = (Boolean) jsonObject.get("success");
                        Log.i("自动登录", String.valueOf(jsonObject));
                        if(success == true){
                            mainActivityViewModel.getLoginSuccessLiveData().setValue(true);
                            FLAG = 1;
                        }
                        else {
                            Log.i("自动登录", "自动登录失败");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
        else {
            // 随便拽一个view来调PostDelay
            greetingsTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("自动登录", "去登录/注册");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }, delayTime);

        }

        mainActivityViewModel.getLoginSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            greetingsTextView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(MainActivity.this,
                                            BrowseRestaurantOverViewActivity.class));
//                                    startActivity(new Intent(MainActivity.this,
//                                            TestBottomNav.class));
                                }
                            }, delayTime);
                        }
                        else if (FLAG == 1){
                            Log.i("自动登录", "自动登录失败1");
                        }
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}