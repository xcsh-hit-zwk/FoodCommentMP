package com.example.foodcommentmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodcommentmp.databinding.ActivityRegisterBinding;
import com.example.foodcommentmp.viewmodel.LoginViewModel;
import com.example.foodcommentmp.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    RegisterViewModel registerViewModel;
    ActivityRegisterBinding registerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerBinding.setData(registerViewModel);
        registerBinding.setLifecycleOwner(this);

        registerBinding.RegisterToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("注册前往登录界面按钮", "点击前往登录界面，即将开始清空数据");
                registerBinding.RegisterUserNameEditText.setText("");
                registerBinding.RegisterPasswordEditText.setText("");
                registerBinding.RegisterNicknameEditText.setText("");
                registerViewModel.reset();

                Log.i("注册前往登录界面按钮", "清空数据完成，即将开始界面跳转");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

//        registerBinding.RegisterConfirmTextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("获取注册信息按钮", "开始抓取输入注册信息");
//
//                String userName = registerBinding.RegisterUserNameEditText.getText().toString();
//                String password = registerBinding.RegisterPasswordEditText.getText().toString();
//                String nickName = registerBinding.RegisterNicknameEditText.getText().toString();
//
//                if(userName.isEmpty() && password.isEmpty()){
//                    Toast.makeText(RegisterActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT)
//                            .show();
//                }else if(userName.isEmpty() || password.isEmpty()){
//
//                    if(userName.isEmpty()){
//                        Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
//                                .show();
//                    }else if(password.isEmpty()){
//                        Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//
//                }else {
//                    if (nickName.isEmpty()){
//                        nickName = userName;
//                    }
//                    Log.i("获取注册信息按钮", "用户名为:" + userName);
//                    Log.i("获取注册信息按钮", "密码为:" + password);
//                    Log.i("获取注册信息按钮", "昵称为:" + nickName);
//                }
//            }
//        });
    }
}