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
import com.example.foodcommentmp.ViewModel.LabelInfoAddViewModel;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.LabelOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLabelInfoAddActivity extends AppCompatActivity {

    private ImageButton confirmButton;
    private ImageButton cancelButton;

    private EditText labelNameEditText;
    private EditText restaurantNameEditText;

    private LabelInfoAddViewModel labelInfoAddViewModel;

    private int FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_label_info_add);

        labelInfoAddViewModel = new ViewModelProvider(this)
                .get(LabelInfoAddViewModel.class);

        confirmButton = findViewById(R.id.confirm_add_label_image_button);
        cancelButton = findViewById(R.id.cancel_add_label_image_button);

        labelNameEditText = findViewById(R.id.add_label_name);
        restaurantNameEditText = findViewById(R.id.add_label_restaurant_name);

        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(confirmButton, true);
        textInputHelper.addViews(labelNameEditText, restaurantNameEditText);

        // 确认
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();

                LabelOverView labelOverView = new LabelOverView();
                labelOverView.setLabelName(labelNameEditText.getText().toString());
                labelOverView.setRestaurantName(restaurantNameEditText.getText().toString());

                AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                Call<ResponseBody> call = adminInfoService.addLabel(labelOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("添加标签", String.valueOf(jsonObject));
                            FLAG = 1;
                            if(success == true){
                                labelInfoAddViewModel.getAddSuccessLiveData().setValue(true);
                            }
                            else {
                                Toast.makeText(AdminLabelInfoAddActivity.this, "新增标签失败1",
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

        // 取消
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLabelInfoAddActivity.this,
                        AdminMainActivity.class));
            }
        });

        labelInfoAddViewModel.getAddSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminLabelInfoAddActivity.this,
                                    AdminMainActivity.class));
                        }
                        else if(FLAG == 1){
                            Log.i("添加标签", "网络回调结果为false，跳转失败");
                        }
                    }
                });
    }
}