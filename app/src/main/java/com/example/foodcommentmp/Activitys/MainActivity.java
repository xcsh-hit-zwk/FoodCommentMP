package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodcommentmp.R;

public class MainActivity extends AppCompatActivity {
    Button toUserButton;
    Button toAdminButton;
    Button toPictureButton;
    Button toAdminMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toUserButton = (Button) findViewById(R.id.toLoginMainButton);
        toUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogInMain.class);
                startActivity(intent);
            }
        });

        toAdminButton = (Button) findViewById(R.id.toAdminLoginButton);
        toAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        toAdminMainButton = findViewById(R.id.to_admin_main_button);
        toAdminMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AdminMainActivity.class));
            }
        });

    }
}