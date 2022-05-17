package com.example.foodcommentmp.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantDetailViewModel;

import java.io.File;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ImageView restaurantImage;
    private TextView restaurantName;
    private TextView restaurantLikes;
    private TextView restaurantTag;
    private TextView restaurantBlock;
    private TextView restaurantPosition;
    private RecyclerView foodRecycleView;
    private RecyclerView labelRecycleView;
    private RecyclerView commentRecycleView;
    private RecyclerView sameTagRecycleView;

    private RestaurantDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        // 初始化控件
        restaurantImage = findViewById(R.id.restaurant_detail_restaurant_image);
        restaurantName = findViewById(R.id.restaurant_detail_restaurant_name);
        restaurantLikes = findViewById(R.id.restaurant_detail_restaurant_likes);
        restaurantTag = findViewById(R.id.restaurant_detail_restaurant_tag);
        restaurantBlock = findViewById(R.id.restaurant_detail_restaurant_neighborhood);
        restaurantPosition = findViewById(R.id.restaurant_detail_restaurant_position);
        foodRecycleView = findViewById(R.id.restaurant_detail_food_recycle_view);
        labelRecycleView = findViewById(R.id.restaurant_detail_restaurant_label_recycle_view);
        commentRecycleView = findViewById(R.id.restaurant_detail_restaurant_label_recycle_view);
        sameTagRecycleView = findViewById(R.id.restaurant_detail_same_tag_recycle_view);

        final Intent getIntent = getIntent();
        Bundle bundle = getIntent.getBundleExtra("restaurant_detail");
        if (bundle != null){
            String restaurantNameStr = bundle.getString("restaurant_name");
            String restaurantImageStr = bundle.getString("restaurant_image");
            if(restaurantNameStr != null && restaurantImageStr != null){
                restaurantName.setText(restaurantNameStr);

                File restaurantImageFile = null;
                try {
                    restaurantImageFile = new File(ImageConfig.DIR + restaurantImageStr);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Glide.with(this)
                        .load(restaurantImageFile)
                        .centerCrop()
                        .into(restaurantImage);
            }
        }

        // todo 要查信息
    }
}