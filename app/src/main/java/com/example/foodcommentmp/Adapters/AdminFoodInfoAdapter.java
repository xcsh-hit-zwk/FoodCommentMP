package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.FoodOverView;

import java.io.File;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-05 16:30
 */
public class AdminFoodInfoAdapter extends RecyclerView.Adapter<AdminFoodInfoAdapter.AdminFoodInfoHolder> {

    Context context;
    private List<FoodOverView> foodOverViewList;

    class AdminFoodInfoHolder extends RecyclerView.ViewHolder{

        private final ImageView foodImageView;
        private final TextView foodNameTextView;
        private final TextView foodLikeTextView;
        private final TextView foodRestaurantNameTextView;

        public ImageView getFoodImageView() {
            return foodImageView;
        }

        public TextView getFoodNameTextView() {
            return foodNameTextView;
        }

        public TextView getFoodLikeTextView() {
            return foodLikeTextView;
        }

        public TextView getFoodRestaurantNameTextView() {
            return foodRestaurantNameTextView;
        }

        public AdminFoodInfoHolder(View view){
            super(view);
            // Define click listener for the ViewHolder's View
            foodImageView = (ImageView) view
                    .findViewById(R.id.food_info_item_image_view);
            foodNameTextView = (TextView) view
                    .findViewById(R.id.food_info_item_food_name_text_view);
            foodLikeTextView = (TextView) view
                    .findViewById(R.id.food_info_item_food_like_text_view);
            foodRestaurantNameTextView = (TextView) view
                    .findViewById(R.id.food_info_item_restaurant_name_text_view);
        }

    }

    /**
     * 初始化适配齐的数据集合
     * @param context
     * @param foodOverViewList 适配器使用的数据集
     */
    public AdminFoodInfoAdapter(Context context, List<FoodOverView> foodOverViewList){
        this.context = context;
        this.foodOverViewList = foodOverViewList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdminFoodInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_info_item, parent, false);
        AdminFoodInfoHolder adminFoodInfoHolder = new AdminFoodInfoHolder(view);

        return adminFoodInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFoodInfoHolder holder, int position){
        FoodOverView foodOverView = foodOverViewList.get(position);
        File file = new File(ImageConfig.DIR + foodOverView.getFoodImage());
        // Glide加载图片
        Glide.with(context)
                .load(file)
                .into(holder.getFoodImageView());
        holder.getFoodNameTextView().setText(foodOverView.getFoodName());
        holder.getFoodLikeTextView().setText(String.valueOf(foodOverView.getFoodLikes()));
        holder.getFoodRestaurantNameTextView().setText(foodOverView.getRestaurantName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.foodOverViewList.size();
    }
}
