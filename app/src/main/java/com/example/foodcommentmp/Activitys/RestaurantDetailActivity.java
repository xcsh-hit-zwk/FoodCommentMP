package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodcommentmp.R;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        textView = findViewById(R.id.text_view);

        final Intent getIntent = getIntent();
        Bundle bundle = getIntent.getBundleExtra("data");
        if (bundle != null){
            String get = bundle.getString("restaurant_name");
            if(get != null){
                textView.setText(get);
            }
        }
    }
}