package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodcommentmp.R;
import com.example.foodcommentmp.databinding.ActivityLogInMainBinding;

public class LogInMain extends AppCompatActivity {
    ActivityLogInMainBinding activityLogInMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLogInMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_log_in_main);

        activityLogInMainBinding.toRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInMain.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        activityLogInMainBinding.toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInMain.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}