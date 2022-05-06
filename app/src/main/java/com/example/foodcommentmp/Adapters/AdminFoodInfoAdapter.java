package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Activitys.AdminFoodInfoUpdateActivity;
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

    // 默认值
    private int defItem = -1;
    private OnItemListener onItemListener;

    // 选中对象存储值
    private FoodOverView chosen;

    /**
     * 初始化适配齐的数据集合
     * @param context
     * @param foodOverViewList 适配器使用的数据集
     */
    public AdminFoodInfoAdapter(Context context, List<FoodOverView> foodOverViewList){
        this.context = context;
        this.foodOverViewList = foodOverViewList;
    }

    public void setOnItemListener(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }

    // 设置点击事件
    public interface OnItemListener{
        void onClick(View v, int position);
    }

    // 获取点击位置
    public void setDefSelect(int position){
        this.defItem = position;
        notifyDataSetChanged();
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

        // 设置单选事件
        if(defItem != -1){

            if(defItem == position){
                // 获取选中对象
                chosen = foodOverView;
                Log.i("管理员招牌菜信息", "对象为:");
                Log.i("管理员招牌菜信息", chosen.getFoodName());
                Log.i("管理员招牌菜信息", String.valueOf(chosen.getFoodLikes()));
                Log.i("管理员招牌菜信息", chosen.getRestaurantName());
                Log.i("管理员招牌菜信息", chosen.getFoodImage());

                Intent intent = new Intent(context, AdminFoodInfoUpdateActivity.class);
                intent.putExtra("招牌菜信息删除更新", chosen);
                context.startActivity(intent);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.foodOverViewList.size();
    }

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


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemListener != null){
                        onItemListener.onClick(view, getLayoutPosition());
                    }
                }
            });
        }

    }

}
