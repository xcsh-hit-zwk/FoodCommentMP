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
import com.example.foodcommentmp.ViewModel.LoginViewModel;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.Account;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.IOException;
import java.util.PrimitiveIterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private ImageButton confirmButton, toRegisterButton;
    private EditText usernameEditText, passwordEditText;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LoginViewModel loginViewModel;

    private int FLAG = 0;

    private String username;
    private String MD5password;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences("user_account", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        confirmButton = (ImageButton) findViewById(R.id.login_confirm_button);
        toRegisterButton = (ImageButton) findViewById(R.id.to_register_button);

        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(confirmButton, true);
        textInputHelper.addViews(usernameEditText, passwordEditText);

        // 判断登录信息
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                MD5password = MD5.string2MD5(password);

                Account account = new Account(username, MD5password);

                // 网络接口
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
                UserService userService = retrofit.create(UserService.class);
                Call<ResponseBody> call = userService.checkLoginAccount(account);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Boolean success = (Boolean) JSON.parseObject(response.body().string())
                                    .get("success");
                            if(success == true){
                                loginViewModel.getCheckLoginLiveData().setValue(true);
                                Log.i("手动登录", username);
                                Log.i("手动登录", password);
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.commit();
                                FLAG = 1;
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "账户/密码错误", Toast.LENGTH_SHORT)
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

        // 跳转至用户注册界面
        toRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginViewModel.getCheckLoginLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == true){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    Log.i("登录界面", "登录状态设置失败");
                }
            }
        });

    }
}