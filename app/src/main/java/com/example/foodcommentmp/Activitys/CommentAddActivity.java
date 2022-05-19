package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.foodcommentmp.common.TextInputHelper;
import com.example.foodcommentmp.pojo.CommentAddEntity;
import com.example.foodcommentmp.retrofit.CommentService;
import com.example.foodcommentmp.retrofit.RestaurantService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAddActivity extends AppCompatActivity {

    private TextView restaurantName;
    private EditText comment;
    private ImageButton confirmButton;
    private ImageButton cancelButton;

    private String restaurantNameStr;
    private String restaurantImageStr;
    private String username;
    private String commentInfo;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_add);

        restaurantName = findViewById(R.id.comment_add_restaurant_name);
        comment = findViewById(R.id.comment_add_comment_edit);
        confirmButton = findViewById(R.id.comment_add_confirm_button);
        cancelButton = findViewById(R.id.comment_add_cancel_button);

        final Intent getDataIntent = getIntent();
        if (getDataIntent != null){
            restaurantNameStr = getDataIntent.getStringExtra("restaurant_name");
            restaurantImageStr = getDataIntent.getStringExtra("restaurant_image");
            if (restaurantNameStr != null && restaurantImageStr != null){
                restaurantName.setText(restaurantNameStr);
            }
        }

        sharedPreferences = this.getSharedPreferences("user_account", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        TextInputHelper textInputHelper = new TextInputHelper(confirmButton);
        textInputHelper.addViews(comment);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.equals("")){
                    Toast.makeText(CommentAddActivity.this, "用户不存在，请退出重试",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    commentInfo = comment.getText().toString();
                    CommentService commentService = createRetrofit();

                    CommentAddEntity commentAddEntity = new CommentAddEntity();
                    commentAddEntity.setUsername(username);
                    commentAddEntity.setRestaurantName(restaurantNameStr);
                    commentAddEntity.setCommentInfo(commentInfo);
                    Call<ResponseBody> call = commentService.addComment(commentAddEntity);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject = JSON.parseObject(response.body().string());
                                Boolean success = (Boolean) jsonObject.get("success");
                                Log.i("添加评论", String.valueOf(jsonObject));
                                if (success == true){
                                    String commentId = jsonObject.getString("data");
                                    Intent intent = new Intent(CommentAddActivity.this,
                                            RestaurantDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("restaurant_name", restaurantNameStr);
                                    bundle.putString("restaurant_image", restaurantImageStr);
                                    bundle.putString("comment_id", commentId);
                                    intent.putExtra("comment_added", bundle);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(CommentAddActivity.this, "评论提交失败，请退出重试",
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentAddActivity.this, RestaurantDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("restaurant_name", restaurantNameStr);
                bundle.putString("restaurant_image", restaurantImageStr);
                intent.putExtra("restaurant_detail", bundle);
                startActivity(intent);
            }
        });
    }

    public CommentService createRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        CommentService commentService = retrofit.create(CommentService.class);

        return commentService;
    }
}