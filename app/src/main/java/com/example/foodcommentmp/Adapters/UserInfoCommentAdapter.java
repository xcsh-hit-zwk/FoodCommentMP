package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Activitys.CommentModifyActivity;
import com.example.foodcommentmp.Activitys.RestaurantDetailActivity;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.UserInfoComment;

import java.io.File;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-12 16:56
 */
public class UserInfoCommentAdapter extends RecyclerView.Adapter<UserInfoCommentAdapter.UserInfoCommentHolder> {

    Context context;
    private List<UserInfoComment> userInfoCommentList;

    // 默认值
    private int defItem = -1;
    private OnItemListener onItemListener;

    // 选中对象存储
    private UserInfoComment userInfoComment;

    /**
     * 初始化适配器的数据集合
     * @param context
     * @param userInfoCommentList 适配器使用的数据集
     */
    public UserInfoCommentAdapter(Context context, List<UserInfoComment> userInfoCommentList) {
        this.context = context;
        this.userInfoCommentList = userInfoCommentList;
        notifyDataSetChanged();
    }

    public void setUserInfoCommentList(List<UserInfoComment> userInfoCommentList) {
        this.userInfoCommentList = userInfoCommentList;
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
    public UserInfoCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_info_comment_item, parent, false);
        UserInfoCommentHolder userInfoCommentHolder = new UserInfoCommentHolder(view);

        return userInfoCommentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoCommentHolder holder, int position) {
        userInfoComment = userInfoCommentList.get(position);

        File userImageFile = null;
        try {
            userImageFile = new File(ImageConfig.DIR + userInfoComment.getUserImage());
        }catch (Exception e){
            e.printStackTrace();
        }

        File restaurantImageFile = null;
        try {
            restaurantImageFile = new File(ImageConfig.DIR + userInfoComment.getRestaurantImage());
        }catch (Exception e){
            e.printStackTrace();
        }

        // 用户头像
        Glide.with(context)
                .load(userImageFile)
                .circleCrop()
                .into(holder.getHeadImage());
        // 餐厅头像
        Glide.with(context)
                .load(restaurantImageFile)
                .into(holder.getCommentRestaurantImage());

        // 填充灰色背景
        GradientDrawable background = new GradientDrawable();
        background.setColor(context.getResources().getColor(R.color.grey));
        background.setCornerRadius(10);
        holder.getCommentBackground().setBackground(background);

        // 设置参数
        holder.getNickname().setText(userInfoComment.getNickname());
        holder.getComment().setText(userInfoComment.getCommentInfo());
        holder.getCommentRestaurantName().setText(userInfoComment.getRestaurantName());
        String info = userInfoComment.getRestaurantPosition() + userInfoComment.getRestaurantTag();
        holder.getCommentRestaurantInfo().setText(info);

        // 点击修改按钮
        holder.getCommentModify().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentModifyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("comment_info", userInfoComment.getCommentInfo());
                bundle.putString("nick_name", userInfoComment.getNickname());
                bundle.putString("head_image", userInfoComment.getUserImage());
                bundle.putString("username", userInfoComment.getUsername());
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            }
        });

        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("restaurant_name", userInfoComment.getRestaurantName());
        intent.putExtra("data", bundle);
        // 点击餐厅
        holder.getCommentBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intent);
            }
        });
        // 点击餐厅
        holder.getCommentRestaurantImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intent);
            }
        });
        // 点击餐厅
        holder.getCommentRestaurantName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intent);
            }
        });
        // 点击餐厅
        holder.getCommentRestaurantInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userInfoCommentList.size();
    }

    class UserInfoCommentHolder extends RecyclerView.ViewHolder{
        private final ImageView headImage;
        private final TextView nickname;
        private final TextView comment;
        private final LinearLayout commentBackground;
        private final ImageView commentRestaurantImage;
        private final TextView commentRestaurantName;
        private final TextView commentRestaurantInfo;
        private final ImageButton commentModify;

//        public UserInfoCommentHolder(@NonNull View view, final OnItemListener onItemListener)
        public UserInfoCommentHolder(@NonNull View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            headImage = (ImageView) view.findViewById(R.id.user_info_image);
            nickname = (TextView) view.findViewById(R.id.user_info_nickname);
            comment = (TextView) view.findViewById(R.id.user_info_comment);
            commentBackground = (LinearLayout) view.findViewById(R.id.user_info_comment_background);
            commentRestaurantImage = (ImageView) view.findViewById(R.id.user_info_comment_restaurant_image);
            commentRestaurantName = (TextView) view.findViewById(R.id.user_info_comment_restaurant_name);
            commentRestaurantInfo = (TextView) view.findViewById(R.id.user_info_comment_restaurant_info);
            commentModify = (ImageButton) view.findViewById(R.id.user_info_comment_modify);

        }

        public ImageView getHeadImage() {
            return headImage;
        }

        public TextView getNickname() {
            return nickname;
        }

        public TextView getComment() {
            return comment;
        }

        public LinearLayout getCommentBackground() {
            return commentBackground;
        }

        public ImageView getCommentRestaurantImage() {
            return commentRestaurantImage;
        }

        public TextView getCommentRestaurantName() {
            return commentRestaurantName;
        }

        public TextView getCommentRestaurantInfo() {
            return commentRestaurantInfo;
        }

        public ImageButton getCommentModify() {
            return commentModify;
        }
    }
}
