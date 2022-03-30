package com.example.foodcommentmp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginToRegisterButton = (Button) findViewById(R.id.Login_toRegister_button);
        Button loginConfirmButton = (Button) findViewById(R.id.Login_confirm_text_button);

        loginToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("登录转注册按钮处理", "点击去往注册");
                EditText userNameEditText = (EditText) findViewById(R.id.Login_user_name_EditText);
                EditText passwordEditText = (EditText) findViewById(R.id.Login_password_EditText);
                userNameEditText.setText("");
                passwordEditText.setText("");
                Log.i("登录转注册按钮处理", "清空输入信息完成");

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("登录信息确认按钮", "接收输入信息，即将开始处理");
                EditText userNameEditText = (EditText) findViewById(R.id.Login_user_name_EditText);
                EditText passwordEditText = (EditText) findViewById(R.id.Login_password_EditText);

                String userName = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(userName.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT)
                            .show();
                }else if(userName.isEmpty() || password.isEmpty()){

                    if(userName.isEmpty()){
                        Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
                                .show();
                    }else if(password.isEmpty()){
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
                                .show();
                    }

                }else {
                    Log.i("登录信息确认按钮", "用户名为:" + userName);
                    Log.i("登录信息确认按钮", "密码为:" + password);
                }
            }
        });
    }
}