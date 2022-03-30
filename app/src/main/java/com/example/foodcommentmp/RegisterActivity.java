package com.example.foodcommentmp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button RegisterToLoginButton = (Button) findViewById(R.id.Register_toLogin_button);
        Button confirmTextButton = (Button) findViewById(R.id.Register_confirm_text_button);

        RegisterToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("注册前往登录界面按钮", "点击前往登录界面，即将开始清空数据");
                EditText userNameEditText = (EditText) findViewById(R.id.Register_user_name_EditText);
                EditText passwordEditText = (EditText) findViewById(R.id.Register_password_EditText);
                EditText nickNameEditText = (EditText) findViewById(R.id.Register_nickname_EditText);
                userNameEditText.setText("");
                passwordEditText.setText("");
                nickNameEditText.setText("");
                Log.i("注册前往登录界面按钮", "清空数据完成，即将开始界面跳转");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        confirmTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("获取注册信息按钮", "开始抓取输入注册信息");
                EditText userNameEditText = (EditText) findViewById(R.id.Register_user_name_EditText);
                EditText passwordEditText = (EditText) findViewById(R.id.Register_password_EditText);
                EditText nickNameEditText = (EditText) findViewById(R.id.Register_nickname_EditText);

                String userName = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String nickName = nickNameEditText.getText().toString();

                if(userName.isEmpty() && password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT)
                            .show();
                }else if(userName.isEmpty() || password.isEmpty()){

                    if(userName.isEmpty()){
                        Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
                                .show();
                    }else if(password.isEmpty()){
                        Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
                                .show();
                    }

                }else {
                    if (nickName.isEmpty()){
                        nickName = userName;
                    }
                    Log.i("获取注册信息按钮", "用户名为:" + userName);
                    Log.i("获取注册信息按钮", "密码为:" + password);
                    Log.i("获取注册信息按钮", "昵称为:" + nickName);
                }
            }
        });
    }
}