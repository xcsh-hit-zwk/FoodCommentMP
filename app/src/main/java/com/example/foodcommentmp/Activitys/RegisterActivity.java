package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RegisterViewModel;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.RegisterAccount;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton confirmButton, toLoginButton;
    private EditText usernameEditText, passwordEditText, nicknameEditText;

    private RegisterViewModel registerViewModel;

    private int FLAG = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        confirmButton = (ImageButton) findViewById(R.id.register_confirm_button);
        toLoginButton = (ImageButton) findViewById(R.id.to_login_button);

        usernameEditText = (EditText) findViewById(R.id.register_username);
        passwordEditText = (EditText) findViewById(R.id.register_password);
        nicknameEditText = (EditText) findViewById(R.id.register_nickname);

        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(confirmButton, true);
        textInputHelper.addViews(usernameEditText, passwordEditText);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                password = MD5.string2MD5(password);
                String nickname = nicknameEditText.getText().toString();

                // 如果未输入昵称
                if(nickname.equals("")){
                    nickname = username;
                }

                // 创建网路传输对象RegisterAccount
                RegisterAccount registerAccount = new RegisterAccount(username, password, nickname);

                // 网络接口
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
                UserService userService = retrofit.create(UserService.class);
                Call<ResponseBody> call = userService.checkSignup(registerAccount);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            Boolean success = (Boolean) JSON.parseObject(response.body().string())
                                    .get("success");
                            if(success == true){
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT)
                                        .show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT)
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

        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEditText.setText("");
                passwordEditText.setText("");
                nicknameEditText.setText("");
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }
}