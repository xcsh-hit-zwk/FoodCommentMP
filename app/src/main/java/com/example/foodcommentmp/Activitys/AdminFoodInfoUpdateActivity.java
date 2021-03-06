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
                .getParcelableExtra("???????????????????????????");
        updateFoodOverView = new UpdateFoodOverView();

        deleteButton = findViewById(R.id.delete_food_image_button);
        updateButton = findViewById(R.id.update_food_image_button);
        exitButton = findViewById(R.id.food_info_update_exit_button);

        foodNameEditText = findViewById(R.id.food_name);
        restaurantNameEditText = findViewById(R.id.food_restaurant_name);

        foodImage = findViewById(R.id.food_info_update_image_view);

        // ???????????????????????????
        foodNameEditText.setText(foodOverView.getFoodName());
        restaurantNameEditText.setText(foodOverView.getRestaurantName());
        foodImageStr = foodOverView.getFoodImage();

        File file = new File(foodImageStr);
        Glide.with(this)
                .load(file)
                .centerCrop()
                .into(foodImage);

        // ??????????????????
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
        // ????????????
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

        // ??????
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("?????????????????????", "??????");

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
                            Log.i("???????????????", String.valueOf(jsonObject));
                            if(success == true){
                                foodInfoUpdateViewModel.getDeleteSuccessLiveData().setValue(true);
                                FLAG = 1;
                            }
                            else {
                                Toast.makeText(AdminFoodInfoUpdateActivity.this, "?????????????????????",
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

        // ??????
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
                                Log.i("?????????????????????Id", String.valueOf(jsonObject));
                                String foodId = jsonObject.getString("data");
                                FLAG = 1;

                                // ????????????
                                foodOverView.setFoodName(foodNameEditText.getText().toString());
                                foodOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                                foodOverView.setFoodImage(foodImageStr);

                                updateFoodOverView.setFoodId(foodId);
                                updateFoodOverView.setFoodOverView(foodOverView);
                                foodInfoUpdateViewModel.getShowDialogLiveData().setValue(true);
                            }
                            else {
                                Log.i("?????????????????????", "????????????Id?????????????????????false???????????????");
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
                            Log.i("?????????????????????", "???????????????????????????false???????????????");
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
                            Log.i("?????????????????????", "?????????????????????false???????????????");
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
                                        Log.i("???????????????", String.valueOf(jsonObject));
                                        if(success == true){
                                            foodInfoUpdateViewModel.getUpdateSuccessLiveData().setValue(true);
                                        }
                                        else {
                                            Toast.makeText(AdminFoodInfoUpdateActivity.this, "?????????????????????",
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
                            Log.i("?????????????????????", "???????????????????????????false???????????????");
                        }
                    }
                });
    }

    private void showNormalDialog(){
        /* @setIcon ?????????????????????
         * @setTitle ?????????????????????
         * @setMessage ???????????????????????????
         * setXXX????????????Dialog???????????????????????????????????????
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(AdminFoodInfoUpdateActivity.this);
        normalDialog.setIcon(R.drawable.ic_restaurant_modify);
        normalDialog.setTitle("???????????????");
        normalDialog.setMessage("?????????????????????");
        normalDialog.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        foodOverView.setFoodLikes(0);
                        updateFoodOverView.setFoodOverView(foodOverView);

                        foodInfoUpdateViewModel.getFoodOverViewLiveData()
                                .setValue(updateFoodOverView);
                    }
                });
        normalDialog.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // ??????
        normalDialog.show();
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //?????????document?????????uri????????????document id??????
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
            //?????????content?????????uri??????????????????????????????
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //?????????File?????????uri?????????????????????????????????
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//??????????????????????????????

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //??????uri???selection??????????????????????????????
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