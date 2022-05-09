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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.LabelInfoUpdateViewModel;
import com.example.foodcommentmp.pojo.LabelOverView;
import com.example.foodcommentmp.pojo.UpdateLabelOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLabelInfoUpdateActivity extends AppCompatActivity {

    private ImageButton deleteButton;
    private ImageButton updateButton;

    private EditText labelNameEditText;
    private EditText restaurantNameEditText;

    private LabelInfoUpdateViewModel labelInfoUpdateViewModel;

    private LabelOverView labelOverView;
    private UpdateLabelOverView updateLabelOverView;

    private int FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_label_info_update);

        labelOverView = (LabelOverView) getIntent()
                .getParcelableExtra("标签信息删除更新");
        updateLabelOverView = new UpdateLabelOverView();

        labelInfoUpdateViewModel = new ViewModelProvider(this)
                .get(LabelInfoUpdateViewModel.class);

        deleteButton = findViewById(R.id.delete_label_image_button);
        updateButton = findViewById(R.id.update_label_image_button);

        labelNameEditText = findViewById(R.id.label_name);
        restaurantNameEditText = findViewById(R.id.label_restaurant_name);

        // 填写传递过来的信息
        labelNameEditText.setText(labelOverView.getLabelName());
        restaurantNameEditText.setText(labelOverView.getRestaurantName());

        // 删除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                labelOverView.setLabelName(labelNameEditText.getText().toString());
                labelOverView.setRestaurantName(restaurantNameEditText.getText().toString());

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
                AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                Call<ResponseBody> call = adminInfoService.deleteLabel(labelOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("删除标签", String.valueOf(jsonObject));
                            if (success == true){
                                labelInfoUpdateViewModel.getDeleteSuccessLiveData().setValue(true);
                                FLAG = 1;
                            }
                            else {
                                Toast.makeText(AdminLabelInfoUpdateActivity.this, "删除标签失败",
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

                Call<ResponseBody> call = adminInfoService.getUpdateLabelId(labelOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            if (success == true){
                                Log.i("获取更新标签Id", String.valueOf(jsonObject));
                                String labelId = jsonObject.getString("data");
                                FLAG = 1;

                                // 更新对象
                                labelOverView.setLabelName(labelNameEditText.getText().toString());
                                labelOverView.setRestaurantName(restaurantNameEditText.getText().toString());

                                updateLabelOverView.setLabelId(labelId);
                                updateLabelOverView.setLabelOverView(labelOverView);
                                labelInfoUpdateViewModel.getLabelOverViewLiveData()
                                        .setValue(updateLabelOverView);
                            }
                            else {
                                Log.i("标签更新信息", "获取更新Id网络回调结果为false，跳转失败");
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

        labelInfoUpdateViewModel.getDeleteSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminLabelInfoUpdateActivity.this,
                                    AdminMainActivity.class));
                        }
                        else if(FLAG == 1){
                            Log.i("标签更新信息", "删除网络回调结果为false，跳转失败");
                        }
                    }
                });

        labelInfoUpdateViewModel.getLabelOverViewLiveData()
                .observe(this, new Observer<UpdateLabelOverView>() {
                    @Override
                    public void onChanged(UpdateLabelOverView updateLabelOverView) {
                        if(FLAG == 1){
                            Retrofit retrofit = new Retrofit.Builder()
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .baseUrl(ServerConfig.BASE_URL)
                                    .build();
                            AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                            Call<ResponseBody> call = adminInfoService.updateLabel(updateLabelOverView);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                                        Boolean success = (Boolean) jsonObject.get("success");
                                        Log.i("更新招牌菜", String.valueOf(jsonObject));
                                        if (success == true){
                                            labelInfoUpdateViewModel.getUpdateSuccessLiveData().setValue(true);
                                        }
                                        else {
                                            Toast.makeText(AdminLabelInfoUpdateActivity.this, "更新标签失败",
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

        labelInfoUpdateViewModel.getUpdateSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminLabelInfoUpdateActivity.this,
                                    AdminMainActivity.class));
                        }
                        else if(FLAG == 1){
                            Log.i("标签更新信息", "更新网络回调结果为false，跳转失败");
                        }
                    }
                });

    }
}