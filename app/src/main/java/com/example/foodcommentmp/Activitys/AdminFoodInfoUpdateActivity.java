package com.example.foodcommentmp.Activitys;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
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
import com.example.foodcommentmp.ViewModel.FoodInfoUpdateViewModel;
import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.UpdateFoodOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import java.io.File;
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
    private ImageButton exitButton;

    private EditText foodNameEditText;
    private EditText restaurantNameEditText;

    private ImageView foodImage;

    private String foodImageStr;

    private FoodInfoUpdateViewModel foodInfoUpdateViewModel;

    private ActivityResultLauncher<Intent> activityResultLauncher;

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
        exitButton = findViewById(R.id.food_info_update_exit_button);

        foodNameEditText = findViewById(R.id.food_name);
        restaurantNameEditText = findViewById(R.id.food_restaurant_name);

        foodImage = findViewById(R.id.food_info_update_image_view);

        // 填写传递过来的信息
        foodNameEditText.setText(foodOverView.getFoodName());
        restaurantNameEditText.setText(foodOverView.getRestaurantName());
        foodImageStr = foodOverView.getFoodImage();

        File file = new File(foodImageStr);
        Glide.with(this)
                .load(file)
                .centerCrop()
                .into(foodImage);

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
        foodImage.setOnClickListener(new View.OnClickListener() {
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
                startActivity(new Intent(AdminFoodInfoUpdateActivity.this, AdminMainActivity.class));
            }
        });

        // 删除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("招牌菜更新信息", "删除");

                foodOverView.setFoodName(foodNameEditText.getText().toString());
                foodOverView.setFoodImage(foodImageStr);
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
                            if (success == true){
                                Log.i("获取更新招牌菜Id", String.valueOf(jsonObject));
                                String foodId = jsonObject.getString("data");
                                FLAG = 1;

                                // 更新对象
                                foodOverView.setFoodName(foodNameEditText.getText().toString());
                                foodOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                                foodOverView.setFoodImage(foodImageStr);

                                updateFoodOverView.setFoodId(foodId);
                                updateFoodOverView.setFoodOverView(foodOverView);
                                foodInfoUpdateViewModel.getShowDialogLiveData().setValue(true);
                            }
                            else {
                                Log.i("招牌菜更新信息", "获取更新Id网络回调结果为false，跳转失败");
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
                    .into(foodImage);
            foodImageStr = imagePath;
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}