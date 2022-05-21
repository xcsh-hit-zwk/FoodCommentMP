package com.example.foodcommentmp.Activitys;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.CommentModifyViewModel;
import com.example.foodcommentmp.pojo.SearchInfo;
import com.example.foodcommentmp.retrofit.CommentService;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentModifyActivity extends AppCompatActivity {

    private ImageView userImageImageView;
    private TextView nicknameTextView;
    private TextView restaurantNameTextView;
    private EditText commentEditText;
    private ImageButton modifyButton;
    private ImageButton deleteButton;
    private ImageButton exitButton;

    private String commentId;
    private String nickname;
    private String headImage;
    private String commentInfo;
    private String restaurantName;

    private int FLAG = 0;

    private CommentModifyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_modify);

        mViewModel = new ViewModelProvider(this).get(CommentModifyViewModel.class);

        userImageImageView = findViewById(R.id.comment_modify_user_image);
        nicknameTextView = findViewById(R.id.comment_modify_nickname);
        restaurantNameTextView = findViewById(R.id.comment_modify_restaurant_name);
        commentEditText = findViewById(R.id.comment_modify_comment_edit);
        modifyButton = findViewById(R.id.comment_modify_modify_button);
        deleteButton = findViewById(R.id.comment_modify_delete_button);
        exitButton = findViewById(R.id.comment_modify_exit_button);

        final Intent modIntent = getIntent();
        Bundle bundle = modIntent.getBundleExtra("modify_comment");
        if (bundle != null){
            commentId = bundle.getString("comment_id");
            nickname = bundle.getString("nick_name");
            headImage = bundle.getString("head_image");
            commentInfo = bundle.getString("comment_info");
            restaurantName = bundle.getString("restaurant_name");

            if (commentId != null && nickname != null && headImage != null && commentInfo != null && restaurantName != null){
                mViewModel.getGetBaseInfoSuccessLiveData().setValue(true);
                FLAG = 1;
            }
        }

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommentModifyActivity.this, BrowseRestaurantOverViewActivity.class));
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInfo searchInfo = new SearchInfo();
                searchInfo.setSearchWay(commentId);

                String newCommentInfo = commentEditText.getText().toString();
                if (commentInfo.equals(newCommentInfo)){
                    Toast.makeText(CommentModifyActivity.this, "请修改评论内容后提交",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    searchInfo.setInfo(newCommentInfo);
                    CommentService commentService = getCommentService();

                    Call<ResponseBody> call = commentService.modifyComment(searchInfo);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject = JSON.parseObject(response.body().string());
                                Boolean success = (Boolean) jsonObject.get("success");
                                Log.i("修改评论", String.valueOf(jsonObject));
                                if (success == true){
                                    startActivity(new Intent(
                                            CommentModifyActivity.this, BrowseRestaurantOverViewActivity.class
                                    ));
                                }
                                else {
                                    Toast.makeText(CommentModifyActivity.this,
                                            "修改评论失败，请重试", Toast.LENGTH_SHORT).show();
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInfo searchInfo = new SearchInfo();
                searchInfo.setSearchWay("DeleteComment");
                searchInfo.setInfo(commentId);

                CommentService commentService = getCommentService();

                Call<ResponseBody> call = commentService.deleteComment(searchInfo);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(response.body().string());
                            Boolean success = (Boolean) jsonObject.get("success");
                            Log.i("删除评论", String.valueOf(jsonObject));
                            if (success == true){
                                startActivity(new Intent(
                                        CommentModifyActivity.this, BrowseRestaurantOverViewActivity.class
                                ));
                            }
                            else {
                                Toast.makeText(CommentModifyActivity.this,
                                        "删除评论失败，请重试", Toast.LENGTH_SHORT).show();

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

        mViewModel.getGetBaseInfoSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true){

                    File file = new File(ImageConfig.DIR + headImage);
                    Glide.with(CommentModifyActivity.this)
                            .load(file)
                            .circleCrop()
                            .into(userImageImageView);

                    nicknameTextView.setText(nickname);
                    restaurantNameTextView.setText(restaurantName);
                    commentEditText.setText(commentInfo);
                }
                else if (FLAG == 1){
                    Log.i("修改删除评论", "获取基础信息LiveData报错");
                }
            }
        });
    }

    private CommentService getCommentService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        CommentService commentService = retrofit.create(CommentService.class);

        return commentService;
    }
}