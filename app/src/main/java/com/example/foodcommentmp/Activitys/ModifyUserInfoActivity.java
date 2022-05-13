package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.ModifyUserInfoViewModel;
import com.example.foodcommentmp.common.TextChangedHelper;
import com.example.foodcommentmp.pojo.UserInfo;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModifyUserInfoActivity extends AppCompatActivity {

    private ImageView headImage;
    private EditText userImageUrl;
    private EditText nickname;
    private ImageButton confirmButton;
    private ImageButton cancelButton;
    private ImageView background;

    private ModifyUserInfoViewModel modifyUserInfoViewModel;

    private UserInfo userInfo = new UserInfo();

    private int FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        modifyUserInfoViewModel = new ViewModelProvider(this).get(ModifyUserInfoViewModel.class);

        headImage = findViewById(R.id.mod_user_head_image);

        userImageUrl = findViewById(R.id.mod_user_head_image_path);
        nickname = findViewById(R.id.mod_user_nickname);

        confirmButton = findViewById(R.id.mod_user_confirm_button);
        cancelButton = findViewById(R.id.mod_user_cancel_button);

        background = findViewById(R.id.mod_user_main_background);

        final Intent userIntent = getIntent();
        Bundle bundle = userIntent.getBundleExtra("data");
        Log.i("更新用户", "获取Bundle");
        if (bundle != null){
            String userImageStr = bundle.getString("head_image");
            String nicknameStr = bundle.getString("nickname");
            String usernameStr = bundle.getString("username");
            String passwordStr = bundle.getString("password");
            userInfo.setUsername(usernameStr);
            userInfo.setPassword(passwordStr);
            userInfo.setUserImage(userImageStr);
            userInfo.setNickname(nicknameStr);
            if(userImageStr != null && nicknameStr != null){
                userImageUrl.setText(userImageStr);
                nickname.setText(nicknameStr);
            }
        }

        File file = new File(ImageConfig.DIR + userImageUrl.getText().toString());

        Glide.with(this)
                .load(file)
                .circleCrop()
                .into(headImage);
        Log.i("更新用户", "渲染图片完成");

        File backgroundFile = new File(ImageConfig.DIR + "/background/ludeng.jpg");
        Glide.with(this)
                .load(backgroundFile)
                .centerCrop()
                .into(background);

        TextChangedHelper textChangedHelper = new TextChangedHelper(headImage, this);
        textChangedHelper.addViews(userImageUrl);
        Log.i("更新用户", "添加EditText监听");

        // 确认
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
                UserService userService = retrofit.create(UserService.class);

                userInfo.setNickname(nickname.getText().toString());
                userInfo.setUserImage(userImageUrl.getText().toString());

                Call<ResponseBody> call = userService.updateUserInfo(userInfo);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("更新用户", String.valueOf(jsonObject));
                            FLAG = 1;
                            if(success == true){
                                modifyUserInfoViewModel.getUpdateUserInfoLiveData().setValue(true);
                            }
                            else {
                                Log.i("更新用户", "更新失败");
                                Toast.makeText(ModifyUserInfoActivity.this,
                                        "更新失败", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(ModifyUserInfoActivity.this, BrowseRestaurantOverViewActivity.class));
            }
        });

        modifyUserInfoViewModel.getUpdateUserInfoLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean == true){
                            Log.i("更新用户", "跳转成功");
                            startActivity(new Intent(ModifyUserInfoActivity.this,
                                    BrowseRestaurantOverViewActivity.class));
                        }
                        else if (FLAG == 1){
                            Log.i("更新用户", "跳转失败");
                            Toast.makeText(ModifyUserInfoActivity.this,
                                    "跳转失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}