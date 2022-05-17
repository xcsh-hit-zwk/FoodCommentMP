package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Activitys.AdminRestaurantInfoUpdateActivity;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.RestaurantOverView;

import java.io.File;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-05 8:46
 */
public class AdminRestaurantInfoAdaptor extends RecyclerView.Adapter<AdminRestaurantInfoAdaptor.AdminRestaurantInfoHolder> {

    Context context;
    private List<RestaurantOverView> restaurantOverViewList;

    // 默认值
    private int defItem = -1;
    private OnItemListener onItemListener;

    // 选中对象存储
    private RestaurantOverView chosen;

    /**
     * 初始化适配器的数据集合
     * @param context
     * @param restaurantOverViewList 适配器使用的数据集
     */
    public AdminRestaurantInfoAdaptor(Context context, List<RestaurantOverView> restaurantOverViewList){
        this.context = context;
        this.restaurantOverViewList = restaurantOverViewList;
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

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdminRestaurantInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_info_item, parent, false);
        AdminRestaurantInfoHolder adminRestaurantInfoHolder = new AdminRestaurantInfoHolder(view);

        return  adminRestaurantInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRestaurantInfoHolder holder, int position) {
        RestaurantOverView restaurantOverView = restaurantOverViewList.get(position);

        File file = null;
        try {
            file = new File(ImageConfig.DIR + restaurantOverView.getRestaurantImage());
        }catch (Exception e){
            e.printStackTrace();
        }
        // Glide加载图片
        Glide.with(context)
                .load(file)
                .into(holder.getRestaurantImageView());
        holder.getRestaurantNameTextView().setText(restaurantOverView.getRestaurantName());
        holder.getRestaurantLikeTextView().setText(String.valueOf(restaurantOverView.getLikes()));
        holder.getRestaurantLabelTextView().setText(restaurantOverView.getRestaurantTag());
        String showPosition = restaurantOverView.getRestaurantProvince() + " " +
                restaurantOverView.getRestaurantCity() + " " +
                restaurantOverView.getRestaurantBlock();
        holder.getRestaurantPositionTextView().setText(showPosition);

        // 设置单选事件
        if(defItem != -1){

            if(defItem == position){

                // 获取选中对象
                chosen = restaurantOverView;
                // 打印获得对象日志，
                Log.i("管理员餐厅信息", "对象为:");
                Log.i("管理员餐厅信息", chosen.getRestaurantName());
                Log.i("管理员餐厅信息", String.valueOf(chosen.getLikes()));
                Log.i("管理员餐厅信息", chosen.getRestaurantTag());
                Log.i("管理员餐厅信息", chosen.getRestaurantPosition());
                Log.i("管理员餐厅信息", chosen.getRestaurantImage());
                Log.i("管理员餐厅信息", chosen.getRestaurantProvince());
                Log.i("管理员餐厅信息", chosen.getRestaurantCity());
                Log.i("管理员餐厅信息", chosen.getRestaurantBlock());

                Intent intent = new Intent(context, AdminRestaurantInfoUpdateActivity.class);
                intent.putExtra("餐厅信息删除更新", chosen);
                context.startActivity(intent);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.restaurantOverViewList.size();
    }

    class AdminRestaurantInfoHolder extends RecyclerView.ViewHolder{

        private final ImageView restaurantImageView;
        private final TextView restaurantNameTextView;
        private final TextView restaurantLikeTextView;
        private final TextView restaurantLabelTextView;
        private final TextView restaurantPositionTextView;

        public ImageView getRestaurantImageView() {
            return restaurantImageView;
        }

        public TextView getRestaurantNameTextView() {
            return restaurantNameTextView;
        }

        public TextView getRestaurantLikeTextView() {
            return restaurantLikeTextView;
        }

        public TextView getRestaurantLabelTextView() {
            return restaurantLabelTextView;
        }

        public TextView getRestaurantPositionTextView() {
            return restaurantPositionTextView;
        }

        public AdminRestaurantInfoHolder(View view){
            super(view);
            // Define click listener for the ViewHolder's View
            restaurantImageView = (ImageView) view
                    .findViewById(R.id.restaurant_info_item_image_view);
            restaurantNameTextView = (TextView) view
                    .findViewById(R.id.restaurant_info_item_restaurant_name_text_view);
            restaurantLikeTextView = (TextView) view
                    .findViewById(R.id.restaurant_info_item_restaurant_like_text_view);
            restaurantLabelTextView = (TextView) view
                    .findViewById(R.id.restaurant_info_item_restaurant_label_text_view);
            restaurantPositionTextView = (TextView) view
                    .findViewById(R.id.restaurant_info_item_restaurant_position_text_view);


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
