package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.FoodOverView;

import java.util.List;

/**
 * @description: 不需要点击事件
 * @author: zhangweikun
 * @create: 2022-05-16 16:45
 */
public class DetailFoodAdapter extends RecyclerView.Adapter<DetailFoodAdapter.DetailFoodHolder> {

    Context context;
    private List<FoodOverView> foodOverViewList;

    /**
     * 初始化适配器的数据集合
     * @param context
     * @param foodOverViewList 适配器使用的数据集
     */
    public DetailFoodAdapter(Context context, List<FoodOverView> foodOverViewList) {
        this.context = context;
        this.foodOverViewList = foodOverViewList;
    }

    public void setFoodOverViewList(List<FoodOverView> foodOverViewList) {
        this.foodOverViewList = foodOverViewList;
    }

    @NonNull
    @Override
    public DetailFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailFoodHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DetailFoodHolder extends RecyclerView.ViewHolder{
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
    }
}
