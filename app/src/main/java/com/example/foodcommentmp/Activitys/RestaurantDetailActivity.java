package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodcommentmp.R;

// todo 要写四个item，分别为招牌菜、标签、评价、同类型餐厅
public class RestaurantDetailActivity extends AppCompatActivity {

    private String restaurantNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);


        final Intent getIntent = getIntent();
        Bundle bundle = getIntent.getBundleExtra("data");
        if (bundle != null){
            restaurantNameStr = bundle.getString("restaurant_name");
            if(restaurantNameStr != null){
                // todo 这里用LiveData去调
            }
        }
    }
}