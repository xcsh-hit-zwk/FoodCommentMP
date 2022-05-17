package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.pojo.SearchInfo;
import com.example.foodcommentmp.retrofit.RestaurantService;

import java.io.File;
import java.util.Arrays;
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
public class DetailFoodAdapter extends RecyclerView.Adapter<DetailFoodAdapter.DetailFoodHolder> {

    Context context;
    private List<FoodOverView> foodOverViewList;

    private FoodOverView foodOverView;

    private Boolean[] flag;

    private int pos;

    /**
     * 初始化适配器的数据集合
     * @param context
     * @param foodOverViewList 适配器使用的数据集
     */
    public DetailFoodAdapter(Context context, List<FoodOverView> foodOverViewList) {
        this.context = context;
        this.foodOverViewList = foodOverViewList;
        flag = new Boolean[foodOverViewList.size()];
        Arrays.fill(flag, Boolean.FALSE);
    }

    public void setFoodOverViewList(List<FoodOverView> foodOverViewList) {
        this.foodOverViewList = foodOverViewList;
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
        foodOverView = foodOverViewList.get(position);
        pos = holder.getBindingAdapterPosition();

        File file = null;
        try {
            file = new File(ImageConfig.DIR + foodOverView.getFoodImage());
        }catch (Exception e){
            e.printStackTrace();
        }
        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(holder.getFoodImage());

        holder.getFoodName().setText(foodOverView.getFoodName());
        holder.getFoodLikes().setText(String.valueOf(foodOverView.getFoodLikes()));

        // 响应点赞事件
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag[pos] == false){
                    // 点击变红
                    holder.likeButton.setBackgroundResource(R.drawable.ic_liked);

                    // 添加点赞数
                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(ServerConfig.BASE_URL)
                            .build();
                    RestaurantService restaurantService = retrofit.create(RestaurantService.class);

                    SearchInfo searchInfo = new SearchInfo();
                    searchInfo.setSearchWay(foodOverView.getRestaurantName());
                    searchInfo.setInfo(foodOverView.getFoodName());
                    Call<ResponseBody> call = restaurantService.addFoodLike(searchInfo);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject = JSON.parseObject(response.body().string());
                                Boolean success = (Boolean) jsonObject.get("success");
                                Log.i("点赞招牌菜", String.valueOf(jsonObject));
                                if(success == true){
                                    int likes = foodOverView.getFoodLikes();
                                    holder.foodLikes.setText(String.valueOf(likes+1));
                                }
                                else {
                                    Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
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
                // 取消点赞
                else {
                    // 点击变灰
                    holder.likeButton.setBackgroundResource(R.drawable.ic_before_like);

                    // 减少点赞数
                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(ServerConfig.BASE_URL)
                            .build();
                    RestaurantService restaurantService = retrofit.create(RestaurantService.class);

                    SearchInfo searchInfo = new SearchInfo();
                    searchInfo.setSearchWay(foodOverView.getRestaurantName());
                    searchInfo.setInfo(foodOverView.getFoodName());
                    Call<ResponseBody> call = restaurantService.cancelFoodLike(searchInfo);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JSONObject jsonObject = JSON.parseObject(response.body().string());
                                Boolean success = (Boolean) jsonObject.get("success");
                                Log.i("点赞招牌菜", String.valueOf(jsonObject));
                                if(success == true){
                                    int likes = foodOverView.getFoodLikes();
                                    holder.foodLikes.setText(String.valueOf(likes-1));
                                }
                                else {
                                    Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return 0;
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
