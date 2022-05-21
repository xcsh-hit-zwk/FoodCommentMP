package com.example.foodcommentmp.Activitys;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
    private EditText nickname;
    private ImageButton confirmButton;
    private ImageButton cancelButton;
    private ImageView background;

    private String userImageStr;
    private String nicknameStr;
    private String usernameStr;
    private String passwordStr;

    private ModifyUserInfoViewModel modifyUserInfoViewModel;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private UserInfo userInfo = new UserInfo();

    private int FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        modifyUserInfoViewModel = new ViewModelProvider(this).get(ModifyUserInfoViewModel.class);

        headImage = findViewById(R.id.mod_user_head_image);

        nickname = findViewById(R.id.mod_user_nickname);

        confirmButton = findViewById(R.id.mod_user_confirm_button);
        cancelButton = findViewById(R.id.mod_user_cancel_button);

        background = findViewById(R.id.mod_user_main_background);

        final Intent userIntent = getIntent();
        Bundle bundle = userIntent.getBundleExtra("data");
        Log.i("更新用户", "获取Bundle");
        if (bundle != null){
            userImageStr = bundle.getString("head_image");
            nicknameStr = bundle.getString("nickname");
            usernameStr = bundle.getString("username");
            passwordStr = bundle.getString("password");

            userInfo.setUsername(usernameStr);
            userInfo.setPassword(passwordStr);
            userInfo.setUserImage(userImageStr);
            userInfo.setNickname(nicknameStr);

            if(userImageStr != null && nicknameStr != null){
                nickname.setText(nicknameStr);

                File file = new File(userImageStr);

                Glide.with(this)
                        .load(file)
                        .circleCrop()
                        .into(headImage);
            }
        }

        File backgroundFile = new File(ImageConfig.DIR + "/background/ludeng.jpg");
        Glide.with(this)
                .load(backgroundFile)
                .centerCrop()
                .into(background);

        // 图片选择回调
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            //callback
            if (result != null && result.getResultCode() == RESULT_OK){
                if (Build.VERSION.SDK_INT >= 19){
                    handleImageOnKitkat(result.getData());
                }
                else {
                    handleImageBeforeKitkat(result.getData());
                }
            }
        });
        // 图片选择
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activityResultLauncher.launch(intent);
            }
        });

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
                userInfo.setUserImage(userImageStr);

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

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            File file = new File(imagePath);
            Glide.with(this)
                    .load(file)
                    .circleCrop()
                    .into(headImage);
            userImageStr = imagePath;
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}