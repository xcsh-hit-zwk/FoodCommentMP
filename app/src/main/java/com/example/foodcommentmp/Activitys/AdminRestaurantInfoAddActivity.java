package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAddViewModel;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAdminViewModel;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(confirmButton, true);
        textInputHelper.addViews(restaurantNameEditText, restaurantTagEditText, restaurantPositionEditText,
                restaurantImageEditText, restaurantProvinceEditText, restaurantCityEditText,
                restaurantBlockEditText);

        // 确认
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();

                RestaurantOverView restaurantOverView = new RestaurantOverView();
                restaurantOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                restaurantOverView.setRestaurantTag(restaurantTagEditText.getText().toString());
                restaurantOverView.setRestaurantPosition(restaurantPositionEditText.getText().toString());
                restaurantOverView.setRestaurantImage(restaurantImageEditText.getText().toString());
                restaurantOverView.setRestaurantProvince(restaurantProvinceEditText.getText().toString());
                restaurantOverView.setRestaurantCity(restaurantCityEditText.getText().toString());
                restaurantOverView.setRestaurantBlock(restaurantBlockEditText.getText().toString());
                restaurantOverView.setLikes(0);

                AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                Call<ResponseBody> call = adminInfoService.addRestaurant(restaurantOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("添加餐厅", String.valueOf(jsonObject));
                            if(success == true){
                                restaurantInfoAddViewModel.getAddSuccessLiveData().setValue(true);
                            }
                            else {
                                Toast.makeText(AdminRestaurantInfoAddActivity.this, "新增餐厅失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        restaurantInfoAddViewModel.getAddSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminRestaurantInfoAddActivity.this,
                                    AdminMainActivity.class));
                        }
                        else {
                            Log.i("添加餐厅", "网络回调结果为false，跳转失败");
                        }
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