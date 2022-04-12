package com.example.foodcommentmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.foodcommentmp.databinding.ActivityLoginBinding;
import com.example.foodcommentmp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;
    ActivityLoginBinding loginBinding;
    MutableLiveData<Boolean> isLoginLivaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginBinding.setData(loginViewModel);
        loginBinding.setLifecycleOwner(this);

        loginBinding.LoginToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("登录转注册按钮处理", "点击去往注册");
                loginBinding.LoginUserNameEditText.setText("");
                loginBinding.LoginPasswordEditText.setText("");
                Log.i("登录转注册按钮处理", "清空输入信息完成");

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        isLoginLivaData = loginViewModel.getIsLoginLivaData();
        isLoginLivaData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean.booleanValue() == true){
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "账号错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}