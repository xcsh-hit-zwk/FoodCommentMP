package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.RestaurantComment;

import java.io.File;
import java.util.List;

import retrofit2.Retrofit;

/**
 * @author: zhangweikun
 * @create: 2022-05-16 16:46
 */
public class DetailCommentAdapter extends RecyclerView.Adapter<DetailCommentAdapter.DetailCommentHolder> {

    Context context;
    private List<RestaurantComment> restaurantCommentList;

    private RestaurantComment restaurantComment;

    /**
     * 初始化适配器的数据集合
     * @param context
     * @param restaurantCommentList 适配器使用的数据集
     */
    public DetailCommentAdapter(Context context, List<RestaurantComment> restaurantCommentList) {
        this.context = context;
        this.restaurantCommentList = restaurantCommentList;
    }

    public void setRestaurantCommentList(List<RestaurantComment> restaurantCommentList) {
        this.restaurantCommentList = restaurantCommentList;
    }

    @NonNull
    @Override
    public DetailCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_detail_comment_item, parent, false);
        DetailCommentHolder detailCommentHolder = new DetailCommentHolder(view);

        return detailCommentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailCommentHolder holder, int position) {
        RestaurantComment restaurantComment = restaurantCommentList.get(position);

        File file = new File(ImageConfig.DIR + restaurantComment.getUserImage());
        Glide.with(context)
                .load(file)
                .circleCrop()
                .into(holder.userImage);

        holder.nickname.setText(restaurantComment.getNickname());
        holder.commentInfo.setText(restaurantComment.getCommentInfo());
        holder.commentLikes.setText(String.valueOf(restaurantComment.getCommentLike()));

        // 响应点赞事件
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击变红
                holder.likeButton.setBackgroundResource(R.drawable.ic_liked);

                // todo 这里要网络接口，还有后端的接口也没写
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DetailCommentHolder extends RecyclerView.ViewHolder {
        private final ImageView userImage;
        private final TextView nickname;
        private final TextView commentInfo;
        private final TextView commentLikes;
        private final ImageButton likeButton;

        public DetailCommentHolder(@NonNull View view) {
            super(view);
            userImage = (ImageView) view.findViewById(R.id.restaurant_detail_comment_item_user_image);
            nickname = (TextView) view.findViewById(R.id.restaurant_detail_comment_item_user_info_nickname);
            commentInfo = (TextView) view.findViewById(R.id.restaurant_detail_comment_item_user_info_comment);
            commentLikes = (TextView) view.findViewById(R.id.restaurant_detail_comment_item_likes);
            likeButton = (ImageButton) view.findViewById(R.id.restaurant_detail_comment_item_likes_button);
        }
    }
}
