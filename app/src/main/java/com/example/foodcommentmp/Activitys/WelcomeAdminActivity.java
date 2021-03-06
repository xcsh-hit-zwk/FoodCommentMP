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
import com.example.foodcommentmp.ViewModel.AdminLoginViewModel;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.pojo.AdminAccount;
import com.example.foodcommentmp.retrofit.AdminService;

import java.io.File;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WelcomeAdminActivity extends AppCompatActivity {

    private ImageView background;
    private TextView greetingsTextView;
    private Calendar calendar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private AdminLoginViewModel adminLoginViewModel;

    private int FLAG = 0;

    private static final int delayTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);

        adminLoginViewModel = new ViewModelProvider(this).get(AdminLoginViewModel.class);

        sharedPreferences = this.getSharedPreferences("admin_account", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        greetingsTextView = findViewById(R.id.admin_greetings_text_view);
        background = findViewById(R.id.admin_welcome_background_image_view);

        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 8){
            greetingsTextView.setText("???????????????????????????");
        }
        else if (hour >= 8 && hour < 12){
            greetingsTextView.setText("???????????????????????????");
        }
        else if (hour >= 12 && hour < 14){
            greetingsTextView.setText("???????????????????????????");
        }
        else if (hour >= 14 && hour < 17){
            greetingsTextView.setText("???????????????????????????");
        }
        else if (hour >= 17 && hour < 24){
            greetingsTextView.setText("???????????????????????????");
        }
        else if (hour >= 24 && hour < 6){
            greetingsTextView.setText("??????????????????????????????");
        }

        File file = null;
        try {
            file = new File(ImageConfig.DIR + "/background/tower.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
        // Glide????????????
        Glide.with(this)
                .load(file)
                .centerCrop()
                .into(background);

        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        Log.i("?????????????????????", username);
        Log.i("?????????????????????", password);

        if (!"".equals(username) && !"".equals(password)){

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ServerConfig.BASE_URL)
                    .build();
            AdminService adminService = retrofit.create(AdminService.class);

            AdminAccount adminAccount = new AdminAccount();
            adminAccount.setUsername(username);
            adminAccount.setPassword(MD5.string2MD5(password));

            Call<ResponseBody> call = adminService.checkLogin(adminAccount);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        Boolean success = (Boolean) jsonObject.get("success");
                        Log.i("?????????????????????", String.valueOf(jsonObject));
                        if (success == true){
                            adminLoginViewModel.getLoginSuccessLiveData().setValue(true);
                            FLAG = 1;
                        }
                        else {
                            Log.i("?????????????????????", "????????????");
                        }
                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
        else {
            greetingsTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("????????????", "?????????/??????");
                    startActivity(new Intent(WelcomeAdminActivity.this, AdminLoginActivity.class));
                }
            }, delayTime);
        }

        adminLoginViewModel.getLoginSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == true){
                    greetingsTextView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("????????????", "?????????/??????");
                            startActivity(new Intent(WelcomeAdminActivity.this, AdminMainActivity.class));
                        }
                    }, delayTime);
                }
                else if (FLAG == 1){
                    Log.i("?????????????????????", "??????????????????1");
                }
            }
        });

    }
}