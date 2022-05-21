package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Activitys.AdminRestaurantInfoUpdateActivity;
import com.example.foodcommentmp.Activitys.RestaurantDetailActivity;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.RestaurantDetail;
import com.example.foodcommentmp.pojo.RestaurantOverView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-16 16:46
 */
public class DetailSameTagAdapter extends RecyclerView.Adapter<DetailSameTagAdapter.DetailSameTagHolder> {

    Context context;
    private List<RestaurantOverView> restaurantOverViewList;

    // 默认值
    private int defItem = -1;
    private OnItemListener onItemListener;

    // 选中对象存储
    private RestaurantOverView chosen;

    public DetailSameTagAdapter(Context context) {
        this.context = context;
    }

    public void setRestaurantOverViewList(List<RestaurantOverView> restaurantOverViewList){
        this.restaurantOverViewList = restaurantOverViewList;
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

    @NonNull
    @Override
    public DetailSameTagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_detail_same_tag_item, parent, false);
        DetailSameTagHolder detailSameTagHolder = new DetailSameTagHolder(view);

        return detailSameTagHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailSameTagHolder holder, int position) {
        RestaurantOverView restaurantOverView = restaurantOverViewList.get(position);

        File file = null;
        try {
            file = new File(restaurantOverView.getRestaurantImage());
        }catch (Exception e){
            e.printStackTrace();
        }
        // Glide加载图片
        Glide.with(context)
                .load(file)
                .into(holder.restaurantImage);
        holder.restaurantName.setText(restaurantOverView.getRestaurantName());
        holder.restaurantLikes.setText(String.valueOf(restaurantOverView.getLikes()));
        holder.restaurantBlock.setText(restaurantOverView.getRestaurantBlock());

        // 设置单选事件
        if(defItem != -1){

            if(defItem == position){

                // 获取选中对象
                chosen = restaurantOverView;
                // 打印获得对象日志，
                Log.i("同标签推荐餐厅", "对象为:");
                Log.i("同标签推荐餐厅", chosen.getRestaurantName());
                Log.i("同标签推荐餐厅", String.valueOf(chosen.getLikes()));
                Log.i("同标签推荐餐厅", chosen.getRestaurantTag());
                Log.i("同标签推荐餐厅", chosen.getRestaurantPosition());
                Log.i("同标签推荐餐厅", chosen.getRestaurantImage());
                Log.i("同标签推荐餐厅", chosen.getRestaurantProvince());
                Log.i("同标签推荐餐厅", chosen.getRestaurantCity());
                Log.i("同标签推荐餐厅", chosen.getRestaurantBlock());

                Intent intent = new Intent(context, RestaurantDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("restaurant_name", chosen.getRestaurantName());
                bundle.putString("restaurant_image", chosen.getRestaurantImage());
                intent.putExtra("restaurant_detail", bundle);
                context.startActivity(intent);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (this.restaurantOverViewList == null){
            return 0;
        }
        return this.restaurantOverViewList.size();
    }


    class DetailSameTagHolder extends RecyclerView.ViewHolder {
        private final ImageView restaurantImage;
        private final TextView restaurantName;
        private final TextView restaurantLikes;
        private final TextView restaurantBlock;

        public DetailSameTagHolder(@NonNull View view) {
            super(view);
            restaurantImage = view.findViewById(R.id.restaurant_detail);
            restaurantName = view.findViewById(R.id.restaurant_detail_same_tag_item_restaurant_name);
            restaurantLikes = view.findViewById(R.id.restaurant_detail_same_tag_item_restaurant_likes);
            restaurantBlock = view.findViewById(R.id.restaurant_detail_same_tag_item_restaurant_block);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemListener != null){
                        onItemListener.onClick(view, getLayoutPosition());
                    }
                }
            });
        }
    }
}
