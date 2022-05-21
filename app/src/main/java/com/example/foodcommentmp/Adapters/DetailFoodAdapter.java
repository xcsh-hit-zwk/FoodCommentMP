package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantDetailViewModel;
import com.example.foodcommentmp.pojo.FoodLiked;
import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.LikeFood;
import com.example.foodcommentmp.pojo.SearchInfo;
import com.example.foodcommentmp.retrofit.RestaurantService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: zhangweikun
 * @create: 2022-05-16 16:45
 */
// todo 招牌菜点赞数反馈给餐厅点赞还是有问题，并且招牌菜点赞也有问题
public class DetailFoodAdapter extends RecyclerView.Adapter<DetailFoodAdapter.DetailFoodHolder> {

    Context context;
    private List<FoodOverView> foodOverViewList;
    private List<FoodLiked> foodLikedList;

    private MutableLiveData<Integer> restaurantLikeLiveData;

    private String restaurantName;
    private String username;

    private int restaurantLike;

    private List<LikeFood> likeFoodList;

    private Boolean[] flag;


    public DetailFoodAdapter(Context context, String restaurantName, String username) {
        this.context = context;
        this.restaurantName = restaurantName;
        this.username = username;
        flag = new Boolean[0];
        Arrays.fill(flag, Boolean.FALSE);
    }

    public void setRestaurantLike(int restaurantLike) {
        this.restaurantLike = restaurantLike;
    }

    public void setFoodOverViewList(List<FoodOverView> foodOverViewList) {
        this.foodOverViewList = foodOverViewList;
        flag = new Boolean[foodOverViewList.size()];
        Arrays.fill(flag, Boolean.FALSE);
    }

    public void setFoodLikedList(List<FoodLiked> foodLikedList) {
        this.foodLikedList = foodLikedList;
    }

    public void setRestaurantLikeLiveData(MutableLiveData<Integer> restaurantLikeLiveData) {
        this.restaurantLikeLiveData = restaurantLikeLiveData;
//        restaurantLikeLiveData.setValue(restaurantLike);
    }

    public void setLikeFoodList(List<LikeFood> likeFoodList) {
        this.likeFoodList = likeFoodList;
    }

    @NonNull
    @Override
    public DetailFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_detail_food_item, parent, false);
        DetailFoodHolder detailFoodHolder = new DetailFoodHolder(view);

        return detailFoodHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailFoodHolder holder, int position) {
        FoodOverView foodOverView = foodOverViewList.get(position);
        int pos = position;

        File file = null;
        try {
            file = new File(foodOverView.getFoodImage());
        }catch (Exception e){
            e.printStackTrace();
        }
        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(holder.getFoodImage());

        holder.getFoodName().setText(foodOverView.getFoodName());
        holder.getFoodLikes().setText(String.valueOf(foodOverView.getFoodLikes()));

        if (!foodLikedList.isEmpty()){
            // 初始化点赞记录
            Iterator<FoodLiked> foodLikedIterator = foodLikedList.iterator();
            while (foodLikedIterator.hasNext()){
                FoodLiked foodLiked = foodLikedIterator.next();
                if (foodOverView.getFoodName().equals(foodLiked.getFoodName())){
                    holder.likeButton.setBackgroundResource(R.drawable.ic_liked);
                    LikeFood likeFood = new LikeFood();
                    likeFood.setUsername(username);
                    likeFood.setFoodName(foodOverView.getFoodName());
                    likeFood.setRestaurantName(restaurantName);
                    likeFoodList.add(likeFood);
                    flag[pos] = true;
                }
            }
        }


        // 响应点赞事件
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag[pos] == false){
                    flag[pos] = true;
                    holder.likeButton.setBackgroundResource(R.drawable.ic_liked);
                    int likes = Integer.parseInt(holder.foodLikes.getText().toString());
                    likes += 1;
                    holder.foodLikes.setText(String.valueOf(likes));
                    LikeFood likeFood = new LikeFood();
                    likeFood.setFoodName(foodOverView.getFoodName());
                    likeFood.setRestaurantName(restaurantName);
                    likeFood.setUsername(username);
                    likeFoodList.add(likeFood);

                    restaurantLikeLiveData.setValue(1);
                }
                // 取消点赞
                else {
                    flag[pos] = false;
                    holder.likeButton.setBackgroundResource(R.drawable.ic_before_like);
                    int likes = Integer.parseInt(holder.foodLikes.getText().toString());
                    likes -= 1;
                    holder.foodLikes.setText(String.valueOf(likes));
                    LikeFood likeFood = new LikeFood();
                    likeFood.setFoodName(foodOverView.getFoodName());
                    likeFood.setRestaurantName(foodOverView.getRestaurantName());
                    likeFood.setUsername(username);
                    if (likeFoodList.remove(likeFood)){
                        Log.i("取消点赞招牌菜", "移除点赞成功");
                    }

                    restaurantLikeLiveData.setValue(-1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.foodLikedList == null){
            return 0;
        }
        return this.foodOverViewList.size();
    }

    class DetailFoodHolder extends RecyclerView.ViewHolder {
        private final ImageView foodImage;
        private final TextView foodName;
        private final TextView foodLikes;
        private final ImageButton likeButton;

        public DetailFoodHolder(@NonNull View view) {
            super(view);
            foodImage = (ImageView) view.findViewById(R.id.restaurant_detail_food_item_food_image);
            foodName = (TextView) view.findViewById(R.id.restaurant_detail_food_item_food_name);
            foodLikes = (TextView) view.findViewById(R.id.restaurant_detail_food_item_food_likes);
            likeButton = (ImageButton) view.findViewById(R.id.restaurant_detail_food_item_food_likes_button);
        }

        public ImageView getFoodImage() {
            return foodImage;
        }

        public TextView getFoodName() {
            return foodName;
        }

        public TextView getFoodLikes() {
            return foodLikes;
        }

        public ImageButton getLikeButton() {
            return likeButton;
        }
    }
}
