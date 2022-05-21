package com.example.foodcommentmp.Activitys;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RegisterViewModel;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.RegisterAccount;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton confirmButton, toLoginButton, toAdminButton;
    private EditText usernameEditText, passwordEditText, nicknameEditText;
    private ImageView userImage;
    private EditText secondPasswordEditText;

    private RegisterViewModel registerViewModel;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private int FLAG = 0;

    private String username;
    private String password;
    private String secondPassword;
    private String nickname;
    private String userImageUrl = ImageConfig.DIR + "/head_image/default.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        confirmButton = findViewById(R.id.register_confirm_button);
        toLoginButton = findViewById(R.id.to_login_button);
        toAdminButton = findViewById(R.id.to_admin_image_button);

        usernameEditText = findViewById(R.id.register_username);
        passwordEditText = findViewById(R.id.register_password);
        secondPasswordEditText = findViewById(R.id.register_second_password);
        nicknameEditText = findViewById(R.id.register_nickname);

        userImage = findViewById(R.id.register_select_image);

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
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activityResultLauncher.launch(intent);
            }
        });
        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(confirmButton, true);
        textInputHelper.addViews(usernameEditText, passwordEditText);

        toAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, WelcomeAdminActivity.class));
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                secondPassword = secondPasswordEditText.getText().toString();
                if (!password.equals(secondPassword)){
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致，请重试",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    String MD5password = MD5.string2MD5(password);
                    nickname = nicknameEditText.getText().toString();

                    // 如果未输入昵称
                    if(nickname.equals("")){
                        nickname = username;
                    }

                    // 创建网路传输对象RegisterAccount
                    RegisterAccount registerAccount = new RegisterAccount(username, MD5password, nickname, userImageUrl);

                    // 网络接口
                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(ServerConfig.BASE_URL)
                            .build();
                    UserService userService = retrofit.create(UserService.class);
                    Call<ResponseBody> call = userService.checkSignup(registerAccount);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                Boolean success = (Boolean) JSON.parseObject(response.body().string())
                                        .get("success");
                                if(success == true){
                                    registerViewModel.getRegisterSuccessLiveData().setValue(true);
                                    FLAG = 1;
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } catch (IOException e) {
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

        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerViewModel.getRegisterSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("user_image", userImageUrl);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                }
                else if (FLAG == 1){
                    Log.i("注册", "注册状态回调失败");
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
                    .into(userImage);
            userImageUrl = imagePath;
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}