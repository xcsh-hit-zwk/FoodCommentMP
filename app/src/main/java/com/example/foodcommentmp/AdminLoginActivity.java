package com.example.foodcommentmp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.AdminAccount;
import com.example.foodcommentmp.retrofit.AdminService;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLoginActivity extends AppCompatActivity {
    ImageButton adminConfirmButton;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        adminConfirmButton = (ImageButton) findViewById(R.id.admin_confirm_button);

        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(adminConfirmButton, true);
        textInputHelper.addViews(usernameEditText, passwordEditText);

        adminConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                password = MD5.string2MD5(password);
                AdminAccount adminAccount = new AdminAccount(username, password);

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
                                Toast.makeText(AdminLoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
                                        .show();
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
    }
}