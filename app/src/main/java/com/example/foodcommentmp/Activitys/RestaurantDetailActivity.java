package com.example.foodcommentmp.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Adapters.DetailCommentAdapter;
import com.example.foodcommentmp.Adapters.DetailFoodAdapter;
import com.example.foodcommentmp.Adapters.DetailLabelAdapter;
import com.example.foodcommentmp.Adapters.DetailSameTagAdapter;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantDetailViewModel;
import com.example.foodcommentmp.pojo.CommentLiked;
import com.example.foodcommentmp.pojo.FoodLiked;
import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.RestaurantComment;
import com.example.foodcommentmp.pojo.RestaurantDetail;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.SearchInfo;
import com.example.foodcommentmp.retrofit.CommentService;
import com.example.foodcommentmp.retrofit.RestaurantService;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private ImageButton addCommentButton;
    private ImageButton exitButton;

    private RestaurantDetailViewModel mViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private DetailCommentAdapter detailCommentAdapter;
    private DetailFoodAdapter detailFoodAdapter;
    private DetailLabelAdapter detailLabelAdapter;
    private DetailSameTagAdapter detailSameTagAdapter;

    private RestaurantDetail restaurantDetail = new RestaurantDetail();

    private int FLAG = 0;
    private int likeFLAG = 0;
    private int commentFLAG = 0;

    private String username;

    private String restaurantNameStr;
    private String restaurantImageStr;
    private String commentId;

    private RestaurantComment restaurantComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        sharedPreferences = this.getSharedPreferences("user_account", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username", "");

        mViewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);
        mViewModel.getRestaurantLikeLiveData();

        // 初始化控件
        restaurantImage = findViewById(R.id.restaurant_detail_restaurant_image);
        restaurantName = findViewById(R.id.restaurant_detail_restaurant_name);
        restaurantLikes = findViewById(R.id.restaurant_detail_restaurant_likes);
        restaurantTag = findViewById(R.id.restaurant_detail_restaurant_tag);
        restaurantBlock = findViewById(R.id.restaurant_detail_restaurant_neighborhood);
        restaurantPosition = findViewById(R.id.restaurant_detail_restaurant_position);
        foodRecycleView = findViewById(R.id.restaurant_detail_food_recycle_view);
        labelRecycleView = findViewById(R.id.restaurant_detail_restaurant_label_recycle_view);
        commentRecycleView = findViewById(R.id.restaurant_detail_comment_recycle_view);
        sameTagRecycleView = findViewById(R.id.restaurant_detail_same_tag_recycle_view);
        addCommentButton = findViewById(R.id.restaurant_detail_add_comment);
        exitButton = findViewById(R.id.restaurant_detail_exit_button);

        final Intent getIntent = getIntent();
        Bundle bundle = getIntent.getBundleExtra("restaurant_detail");
        if (bundle != null){
            restaurantNameStr = bundle.getString("restaurant_name");
            restaurantImageStr = bundle.getString("restaurant_image");
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

        // 添加完评论以后
        final Intent commentAddedIntent = getIntent();
        Bundle commentBundle = commentAddedIntent.getBundleExtra("comment_added");
        if (commentBundle != null){
            restaurantNameStr = commentBundle.getString("restaurant_name");
            restaurantImageStr = commentBundle.getString("restaurant_image");
            Log.i("新增评论", restaurantNameStr);
            commentId = commentBundle.getString("comment_id");
            if (restaurantNameStr != null && restaurantImageStr  != null && commentId  != null){
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

                mViewModel.getCommentAddSuccessLiveData().setValue(true);
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        RestaurantService restaurantService = retrofit.create(RestaurantService.class);

        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setSearchWay("餐厅详情");
        searchInfo.setInfo(restaurantName.getText().toString());
        Call<ResponseBody> call = restaurantService.getRestaurantDetail(searchInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    Boolean success = (Boolean) jsonObject.get("success");
                    Log.i("获取餐厅详情", String.valueOf(jsonObject));
                    if (success == true){
                        restaurantDetail = fillRestaurantDetail(jsonObject);
                        Log.i("获取餐厅详情", "取得餐厅详情");
                        mViewModel.getGetInfoSuccessLiveData().setValue(true);
                        FLAG = 1;
                    }
                    else {
                        Log.i("获取餐厅详情", "取得餐厅详情失败");
                        Toast.makeText(RestaurantDetailActivity.this, "获取信息失败",
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

        // 初始化菜品列表
        foodRecycleView.setLayoutManager(new LinearLayoutManager
                (RestaurantDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        foodRecycleView.setItemAnimator(new DefaultItemAnimator());
        foodRecycleView.addItemDecoration(new DividerItemDecoration
                (RestaurantDetailActivity.this, DividerItemDecoration.HORIZONTAL));
        detailFoodAdapter = new DetailFoodAdapter(RestaurantDetailActivity.this,
                restaurantNameStr, username);
        foodRecycleView.setAdapter(detailFoodAdapter);
        // 初始化标签列表
        labelRecycleView.setLayoutManager(new GridLayoutManager(RestaurantDetailActivity.this,
                3));
        labelRecycleView.setItemAnimator(new DefaultItemAnimator());
        detailLabelAdapter = new DetailLabelAdapter(RestaurantDetailActivity.this);
        labelRecycleView.setAdapter(detailLabelAdapter);
        // 初始化评论列表
        commentRecycleView.setLayoutManager(new LinearLayoutManager
                (RestaurantDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        commentRecycleView.setItemAnimator(new DefaultItemAnimator());
        commentRecycleView.addItemDecoration(new DividerItemDecoration
                (RestaurantDetailActivity.this, DividerItemDecoration.VERTICAL));
        detailCommentAdapter = new DetailCommentAdapter(RestaurantDetailActivity.this,
                restaurantNameStr, username);
        commentRecycleView.setAdapter(detailCommentAdapter);
        // 初始化推荐列表
        sameTagRecycleView.setLayoutManager(new LinearLayoutManager
                (RestaurantDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        sameTagRecycleView.setItemAnimator(new DefaultItemAnimator());
        sameTagRecycleView.addItemDecoration(new DividerItemDecoration
                (RestaurantDetailActivity.this, DividerItemDecoration.HORIZONTAL));
        detailSameTagAdapter = new DetailSameTagAdapter(RestaurantDetailActivity.this);
        sameTagRecycleView.setAdapter(detailSameTagAdapter);

        mViewModel.getGetInfoSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true){
                    Log.i("装填适配器", "开始装填适配器");
                    restaurantLikes.setText(String.valueOf(restaurantDetail.getRestaurantLikes()));
                    restaurantTag.setText(restaurantDetail.getRestaurantTag());
                    restaurantBlock.setText(restaurantDetail.getRestaurantBlock());
                    restaurantPosition.setText(restaurantDetail.getRestaurantPosition());

                    detailFoodAdapter.setFoodOverViewList(restaurantDetail.getFoodList());
                    detailFoodAdapter.setFoodLikedList(restaurantDetail.getLikedFoodList());
                    detailFoodAdapter.setRestaurantLike(restaurantDetail.getRestaurantLikes());
                    detailFoodAdapter.setRestaurantLikeLiveData(mViewModel.getRestaurantLikeLiveData());
                    detailFoodAdapter.notifyDataSetChanged();

                    detailLabelAdapter.setLabelList(restaurantDetail.getLabelList());
                    detailLabelAdapter.notifyDataSetChanged();

                    if (commentFLAG == 1){
                        restaurantDetail.getCommentList().add(0, restaurantComment);
                    }
                    detailCommentAdapter.setRestaurantCommentList(restaurantDetail.getCommentList());
                    detailCommentAdapter.setCommentLikedList(restaurantDetail.getLikedCommentList());
                    detailCommentAdapter.notifyDataSetChanged();

                    detailSameTagAdapter.setRestaurantOverViewList(restaurantDetail.getSameTagList());
                    detailSameTagAdapter.notifyDataSetChanged();

                    detailSameTagAdapter.setOnItemListener(new DetailSameTagAdapter.OnItemListener(){

                        @Override
                        public void onClick(View v, int position) {
                            detailSameTagAdapter.setDefSelect(position);
                        }
                    });
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantDetailActivity.this, BrowseRestaurantOverViewActivity.class));
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetailActivity.this, CommentAddActivity.class);
                intent.putExtra("restaurant_name", restaurantNameStr);
                intent.putExtra("restaurant_image", restaurantImageStr);
                startActivity(intent);
            }
        });

        mViewModel.getRestaurantLikeLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (likeFLAG == 1){
                    String temp = String.valueOf(mViewModel.getRestaurantLikeLiveData().getValue());
                    restaurantLikes.setText(temp);
                }
                else {
                    likeFLAG = 1;
                }
            }
        });

        // 新增评论置顶
        mViewModel.getCommentAddSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true){
                    Retrofit commentRetrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(ServerConfig.BASE_URL)
                            .build();
                    CommentService commentService = commentRetrofit.create(CommentService.class);

                    SearchInfo commentSearchInfo = new SearchInfo();
                    commentSearchInfo.setSearchWay("GetComment");
                    commentSearchInfo.setInfo(commentId);
                    Call<ResponseBody> commentCall = commentService.getComment(commentSearchInfo);
                    commentCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject = JSON.parseObject(response.body().string());
                                Boolean success = (Boolean) jsonObject.get("success");
                                Log.i("新增评论置顶", String.valueOf(jsonObject));
                                if (success == true){
                                    restaurantComment = jsonObject
                                            .getObject("data", RestaurantComment.class);
                                    commentFLAG = 1;
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

    private RestaurantDetail fillRestaurantDetail(JSONObject jsonObject){
        RestaurantDetail restaurantDetail = new RestaurantDetail();

        try {
            JSONObject data = JSON.parseObject(jsonObject.getString("data"));
            restaurantDetail.setRestaurantId(data.getString("restaurantId"));
            restaurantDetail.setRestaurantName(data.getString("restaurantName"));
            restaurantDetail.setRestaurantLikes(data.getInteger("restaurantLikes"));
            restaurantDetail.setRestaurantTag(data.getString("restaurantTag"));
            restaurantDetail.setRestaurantBlock(data.getString("restaurantBlock"));
            restaurantDetail.setRestaurantPosition(data.getString("restaurantPosition"));
            restaurantDetail.setFoodList(JSON.parseArray(data.getString("foodList"), FoodOverView.class));
            restaurantDetail.setLabelList(JSON.parseArray(data.getString("labelList"), String.class));
            restaurantDetail.setCommentList(JSON.parseArray(data.getString("commentList"), RestaurantComment.class));
            restaurantDetail.setSameTagList(JSON.parseArray(data.getString("sameTagList"), RestaurantOverView.class));
            restaurantDetail.setLikedCommentList(JSON.parseArray(data.getString("likedCommentList"), CommentLiked.class));
            restaurantDetail.setLikedFoodList(JSON.parseArray(data.getString("likedFoodList"), FoodLiked.class));
        }catch (Exception e){
            e.printStackTrace();
        }


        return restaurantDetail;
    }
}