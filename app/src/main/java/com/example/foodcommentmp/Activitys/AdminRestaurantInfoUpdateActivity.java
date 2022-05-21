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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantInfoUpdateViewModel;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.UpdateRestaurantOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminRestaurantInfoUpdateActivity extends AppCompatActivity {

    private EditText restaurantNameEditText;
    private EditText restaurantTagEditText;
    private EditText restaurantPositionEditText;
    private EditText restaurantProvinceEditText;
    private EditText restaurantCityEditText;
    private EditText restaurantBlockEditText;

    private ImageButton deleteButton;
    private ImageButton updateButton;
    private ImageButton exitButton;

    private ImageView restaurantImage;

    private String restaurantImageStr;

    private RestaurantInfoUpdateViewModel restaurantInfoUpdateViewModel;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private int FLAG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_restaurant_info_update);

        RestaurantOverView restaurantOverView = (RestaurantOverView) getIntent()
                .getParcelableExtra("餐厅信息删除更新");

        restaurantInfoUpdateViewModel = new ViewModelProvider(this)
                .get(RestaurantInfoUpdateViewModel.class);

        deleteButton = findViewById(R.id.delete_restaurant_image_button);
        updateButton = findViewById(R.id.update_restaurant_image_button);
        exitButton = findViewById(R.id.restaurant_info_update_exit_button);

        restaurantNameEditText = findViewById(R.id.restaurant_name);
        restaurantTagEditText = findViewById(R.id.restaurant_tag);
        restaurantPositionEditText = findViewById(R.id.restaurant_position);
        restaurantProvinceEditText = findViewById(R.id.restaurant_Province);
        restaurantCityEditText = findViewById(R.id.restaurant_city);
        restaurantBlockEditText = findViewById(R.id.restaurant_block);

        restaurantImage = findViewById(R.id.restaurant_info_update_image_view);

        // 填写传递过来的信息
        restaurantNameEditText.setText(restaurantOverView.getRestaurantName());
        restaurantTagEditText.setText(restaurantOverView.getRestaurantTag());
        restaurantPositionEditText.setText(restaurantOverView.getRestaurantPosition());
        restaurantProvinceEditText.setText(restaurantOverView.getRestaurantProvince());
        restaurantCityEditText.setText(restaurantOverView.getRestaurantCity());
        restaurantBlockEditText.setText(restaurantOverView.getRestaurantBlock());
        restaurantImageStr = restaurantOverView.getRestaurantImage();

        File file = new File(restaurantImageStr);
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

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminRestaurantInfoUpdateActivity.this, AdminMainActivity.class));
            }
        });

        // 删除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restaurantOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                restaurantOverView.setRestaurantTag(restaurantTagEditText.getText().toString());
                restaurantOverView.setRestaurantPosition(restaurantPositionEditText.getText().toString());
                restaurantOverView.setRestaurantImage(restaurantImageStr);
                restaurantOverView.setRestaurantProvince(restaurantProvinceEditText.getText().toString());
                restaurantOverView.setRestaurantCity(restaurantCityEditText.getText().toString());
                restaurantOverView.setRestaurantBlock(restaurantBlockEditText.getText().toString());

                Log.i("餐厅更新信息", "删除");
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ServerConfig.BASE_URL)
                        .build();
                AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                Call<ResponseBody> call = adminInfoService.deleteRestaurant(restaurantOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("删除餐厅", String.valueOf(jsonObject));
                            FLAG = 1;
                            if(success == true){
                                restaurantInfoUpdateViewModel.getDeleteSuccessLiveData().setValue(true);
                            }
                            else {
                                Toast.makeText(AdminRestaurantInfoUpdateActivity.this, "删除餐厅失败",
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

                Call<ResponseBody> call = adminInfoService.getUpdateRestaurantId(restaurantOverView);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {

                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("获取更新餐厅Id", String.valueOf(jsonObject));
                            String restaurantId = jsonObject.getString("data");
                            UpdateRestaurantOverView updateRestaurantOverView = new UpdateRestaurantOverView();
                            FLAG = 1;
                            // 更新对象
                            restaurantOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                            restaurantOverView.setRestaurantTag(restaurantTagEditText.getText().toString());
                            restaurantOverView.setRestaurantPosition(restaurantPositionEditText.getText().toString());
                            restaurantOverView.setRestaurantImage(restaurantImageStr);
                            restaurantOverView.setRestaurantProvince(restaurantProvinceEditText.getText().toString());
                            restaurantOverView.setRestaurantCity(restaurantCityEditText.getText().toString());
                            restaurantOverView.setRestaurantBlock(restaurantBlockEditText.getText().toString());

                            updateRestaurantOverView.setRestaurantId(restaurantId);
                            updateRestaurantOverView.setRestaurantOverView(restaurantOverView);
                            restaurantInfoUpdateViewModel.getRestaurantOverViewLiveData()
                                    .setValue(updateRestaurantOverView);

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

        restaurantInfoUpdateViewModel.getDeleteSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminRestaurantInfoUpdateActivity.this,
                                    AdminMainActivity.class));
                        }
                        else if(FLAG == 1){
                            Log.i("餐厅更新信息", "删除网络回调结果为false，跳转失败");
                        }
                    }
                });

        restaurantInfoUpdateViewModel.getUpdateSuccessLiveData()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean == true){
                            startActivity(new Intent(AdminRestaurantInfoUpdateActivity.this,
                                    AdminMainActivity.class));
                        }
                        else if(FLAG == 1){
                            Log.i("餐厅更新信息", "更新网络回调结果为false，跳转失败");
                        }
                    }
                });

        restaurantInfoUpdateViewModel.getRestaurantOverViewLiveData()
                .observe(this, new Observer<UpdateRestaurantOverView>() {
                    @Override
                    public void onChanged(UpdateRestaurantOverView updateRestaurantOverView) {

                        if(FLAG == 1){
                            Retrofit retrofit = new Retrofit.Builder()
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .baseUrl(ServerConfig.BASE_URL)
                                    .build();
                            AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);

                            Call<ResponseBody> call = adminInfoService.updateRestaurant(updateRestaurantOverView);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {

                                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                                        Boolean success = (Boolean) jsonObject.get("success");
                                        Log.i("更新餐厅", String.valueOf(jsonObject));
                                        if(success == true){
                                            restaurantInfoUpdateViewModel.getUpdateSuccessLiveData().setValue(true);
                                        }
                                        else {
                                            Toast.makeText(AdminRestaurantInfoUpdateActivity.this, "更新餐厅失败",
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
            restaurantImageStr = imagePath;
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}