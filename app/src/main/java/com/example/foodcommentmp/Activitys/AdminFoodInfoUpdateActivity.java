package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.FoodInfoUpdateViewModel;
import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.UpdateFoodOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import java.lang.ref.PhantomReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AdminFoodInfoUpdateActivity extends AppCompatActivity {

    private ImageButton deleteButton;
    private ImageButton updateButton;

    private EditText foodNameEditText;
    private EditText restaurantNameEditText;
    private EditText foodImageEditText;

    private FoodInfoUpdateViewModel foodInfoUpdateViewModel;

    private FoodOverView foodOverView;
    private UpdateFoodOverView updateFoodOverView;

    private int FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_info_update_activity);

        foodInfoUpdateViewModel = new ViewModelProvider(this)
                .get(FoodInfoUpdateViewModel.class);

        foodOverView = (FoodOverView) getIntent()
                .getParcelableExtra("招牌菜信息删除更新");
        updateFoodOverView = new UpdateFoodOverView();

        deleteButton = findViewById(R.id.delete_food_image_button);
        updateButton = findViewById(R.id.update_food_image_button);

        foodNameEditText = findViewById(R.id.food_name);
        restaurantNameEditText = findViewById(R.id.food_restaurant_name);
        foodImageEditText = findViewById(R.id.food_image);

        // 填写传递过来的信息
        foodNameEditText.setText(foodOverView.getFoodName());
        restaurantNameEditText.setText(foodOverView.getRestaurantName());
        foodImageEditText.setText(foodOverView.getFoodImage());

        // 删除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("招牌菜更新信息", "删除");

                foodOverView.setFoodName(foodNameEditText.getText().toString());
                foodOverView.setFoodImage(foodImageEditText.getText().toString());
                foodOverView.setRestaurantName(restaurantNameEditText.getText().toString());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ServerConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                Call<ResponseBody> call = adminInfoService.deleteFood(foodOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("删除招牌菜", String.valueOf(jsonObject));
                            if(success == true){
                                foodInfoUpdateViewModel.getDeleteSuccessLiveData().setValue(true);
                                FLAG = 1;
                            }
                            else {
                                Toast.makeText(AdminFoodInfoUpdateActivity.this, "删除招牌菜失败",
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

        // 更新
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
                AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                Call<ResponseBody> call = adminInfoService.getUpdateFoodId(foodOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("获取更新餐厅Id", String.valueOf(jsonObject));
                            String foodId = jsonObject.getString("data");
                            FLAG = 1;

                            // 更新对象
                            foodOverView.setFoodName(foodNameEditText.getText().toString());
                            foodOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                            foodOverView.setFoodImage(foodImageEditText.getText().toString());

                            updateFoodOverView.setFoodId(foodId);
                            updateFoodOverView.setFoodOverView(foodOverView);
                            foodInfoUpdateViewModel.getShowDialogLiveData().setValue(true);

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

        foodInfoUpdateViewModel.getDeleteSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminFoodInfoUpdateActivity.this,
                                    AdminMainActivity.class));
                        }
                        else if(FLAG == 1){
                            Log.i("招牌菜更新信息", "删除网络回调结果为false，跳转失败");
                        }
                    }
                });

        foodInfoUpdateViewModel.getShowDialogLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            showNormalDialog();
                        }
                        else if(FLAG == 1){
                            Log.i("招牌菜更新信息", "弹窗回调结果为false，跳转失败");
                        }
                    }
                });

        foodInfoUpdateViewModel.getFoodOverViewLiveData()
                .observe(this, new Observer<UpdateFoodOverView>() {
                    @Override
                    public void onChanged(UpdateFoodOverView updateFoodOverView) {
                        if (FLAG == 1){
                            Retrofit retrofit = new Retrofit.Builder()
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .baseUrl(ServerConfig.BASE_URL)
                                    .build();
                            AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                            Call<ResponseBody> call = adminInfoService.updateFood(updateFoodOverView);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                                        Boolean success = (Boolean) jsonObject.get("success");
                                        Log.i("更新招牌菜", String.valueOf(jsonObject));
                                        if(success == true){
                                            foodInfoUpdateViewModel.getUpdateSuccessLiveData().setValue(true);
                                        }
                                        else {
                                            Toast.makeText(AdminFoodInfoUpdateActivity.this, "更新招牌菜失败",
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
                    }
                });

        foodInfoUpdateViewModel.getUpdateSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminFoodInfoUpdateActivity.this,
                                    AdminMainActivity.class));
                        }
                        else if(FLAG == 1){
                            Log.i("招牌菜更新信息", "更新网络回调结果为false，跳转失败");
                        }
                    }
                });
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(AdminFoodInfoUpdateActivity.this);
        normalDialog.setIcon(R.drawable.ic_restaurant_modify);
        normalDialog.setTitle("更新招牌菜");
        normalDialog.setMessage("是否清空点赞数");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        foodOverView.setFoodLikes(0);
                        updateFoodOverView.setFoodOverView(foodOverView);

                        foodInfoUpdateViewModel.getFoodOverViewLiveData()
                                .setValue(updateFoodOverView);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    // 测试
    // 打印UpDateFoodOverView
    private void Print(UpdateFoodOverView updateFoodOverView){
        Log.i("打印UpdateFood", "---------------------");
        Log.i("打印UpdateFood", updateFoodOverView.getFoodId());
        Log.i("打印UpdateFood", "---------------------");
    }
}