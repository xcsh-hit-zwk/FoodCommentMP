package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.AdminLoginViewModel;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.AdminAccount;
import com.example.foodcommentmp.retrofit.AdminService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLoginActivity extends AppCompatActivity {

    private ImageButton adminConfirmButton;
    private ImageButton toUserButton;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private AdminLoginViewModel adminLoginViewModel;

    private int FLAG = 0;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminLoginViewModel = new ViewModelProvider(this).get(AdminLoginViewModel.class);

        sharedPreferences = this.getSharedPreferences("admin_account", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        usernameEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);

        adminConfirmButton = findViewById(R.id.admin_confirm_button);
        toUserButton = findViewById(R.id.to_user_image_button);

        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(adminConfirmButton, true);
        textInputHelper.addViews(usernameEditText, passwordEditText);

        toUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLoginActivity.this, MainActivity.class));
            }
        });

        adminConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                String MD5password = MD5.string2MD5(password);
                AdminAccount adminAccount = new AdminAccount(username, MD5password);

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
                AdminService adminService = retrofit.create(AdminService.class);
                Call<ResponseBody> call = adminService.checkLogin(adminAccount);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Boolean success = (Boolean) JSON.parseObject(response.body().string())
                                    .get("success");
                            if(success == true){
                                adminLoginViewModel.getLoginSuccessLiveData().setValue(true);
                                FLAG = 1;
                                Log.i("管理员手动登录", username);
                                Log.i("管理员手动登录", password);
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.commit();
                            }
                            else {
                                Toast.makeText(AdminLoginActivity.this, "账户/密码错误", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        adminLoginViewModel.getLoginSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == true){
                    startActivity(new Intent(AdminLoginActivity.this, WelcomeAdminActivity.class));
                }
                else if (FLAG == 1){
                    Log.i("管理员登录", "登录状态回调错误");
                }
            }
        });
    }
}