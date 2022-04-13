package com.example.foodcommentmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
    MutableLiveData<Boolean> hasRegisterLiveData;

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

        hasRegisterLiveData = registerViewModel.getHasRegisterLiveData();
        hasRegisterLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean.booleanValue() == true){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "账户已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}