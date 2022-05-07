package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAddViewModel;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAdminViewModel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminRestaurantInfoAddActivity extends AppCompatActivity {

    private ImageButton confirmButton;
    private ImageButton cancelButton;

    private EditText restaurantNameEditText;
    private EditText restaurantTagEditText;
    private EditText restaurantPositionEditText;
    private EditText restaurantImageEditText;
    private EditText restaurantProvinceEditText;
    private EditText restaurantCityEditText;
    private EditText restaurantBlockEditText;

    private RestaurantInfoAddViewModel restaurantInfoAddViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_restaurant_info_add);

        restaurantInfoAddViewModel = new ViewModelProvider(this)
                .get(RestaurantInfoAddViewModel.class);

        // 获取控件
        confirmButton = findViewById(R.id.confirm_add_restaurant_info_image_button);
        cancelButton = findViewById(R.id.cancel_add_restaurant_info_image_button);

        restaurantNameEditText = findViewById(R.id.add_restaurant_name);
        restaurantTagEditText = findViewById(R.id.add_restaurant_tag);
        restaurantPositionEditText = findViewById(R.id.add_restaurant_position);
        restaurantImageEditText = findViewById(R.id.add_restaurant_Image);
        restaurantProvinceEditText = findViewById(R.id.add_restaurant_Province);
        restaurantCityEditText = findViewById(R.id.add_restaurant_city);
        restaurantBlockEditText = findViewById(R.id.add_restaurant_block);

        // 确认
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
            }
        });

        // 取消
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminRestaurantInfoAddActivity.this, AdminMainActivity.class));
            }
        });
    }
}