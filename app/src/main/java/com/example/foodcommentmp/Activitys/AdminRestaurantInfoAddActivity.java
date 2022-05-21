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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAddViewModel;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAdminViewModel;
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import java.io.File;
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
    private EditText restaurantProvinceEditText;
    private EditText restaurantCityEditText;
    private EditText restaurantBlockEditText;

    private ImageView restaurantImage;

    private String restaurantImageUrl = ImageConfig.DIR + "/restaurant/default.png";

    private RestaurantInfoAddViewModel restaurantInfoAddViewModel;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private int FLAG = 0;

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
        restaurantProvinceEditText = findViewById(R.id.add_restaurant_Province);
        restaurantCityEditText = findViewById(R.id.add_restaurant_city);
        restaurantBlockEditText = findViewById(R.id.add_restaurant_block);

        restaurantImage = findViewById(R.id.restaurant_info_add_image_view);

        File file = new File(restaurantImageUrl);
        Glide.with(this)
                .load(file)
                .centerCrop()
                .into(restaurantImage);

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
        restaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activityResultLauncher.launch(intent);
            }
        });

        // 监听多个输入框
        TextInputHelper textInputHelper = new TextInputHelper(confirmButton, true);
        textInputHelper.addViews(restaurantNameEditText, restaurantTagEditText, restaurantPositionEditText,
                restaurantProvinceEditText, restaurantCityEditText,
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
                restaurantOverView.setRestaurantImage(restaurantImageUrl);
                restaurantOverView.setRestaurantPosition(restaurantPositionEditText.getText().toString());
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
                            FLAG = 1;
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
                        else if(FLAG == 1){
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
                    .centerCrop()
                    .into(restaurantImage);
            restaurantImageUrl = imagePath;
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}