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
import com.example.foodcommentmp.pojo.CommentLiked;
import com.example.foodcommentmp.pojo.LikeComment;
import com.example.foodcommentmp.pojo.LikeFood;
import com.example.foodcommentmp.pojo.RestaurantComment;
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
 * @create: 2022-05-16 16:46
 */
public class DetailCommentAdapter extends RecyclerView.Adapter<DetailCommentAdapter.DetailCommentHolder> {

    Context context;
    private List<RestaurantComment> restaurantCommentList;
    private List<CommentLiked> commentLikedList;
    private List<LikeComment> likeCommentList;

    private String restaurantName;
    private String username;

    private Boolean[] flag;

    public DetailCommentAdapter(Context context, String restaurantName, String username) {
        this.context = context;
        this.restaurantName = restaurantName;
        this.username = username;
        flag = new Boolean[0];
        Arrays.fill(flag, Boolean.FALSE);
    }


    public void setRestaurantCommentList(List<RestaurantComment> restaurantCommentList) {
        this.restaurantCommentList = restaurantCommentList;
        flag = new Boolean[restaurantCommentList.size()];
        Arrays.fill(flag, Boolean.FALSE);
    }

    public void setCommentLikedList(List<CommentLiked> commentLikedList) {
        this.commentLikedList = commentLikedList;
    }
    public void setLikeCommentList(List<LikeComment> likeCommentList) {
        this.likeCommentList = likeCommentList;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFlag(Boolean[] flag) {
        this.flag = flag;
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
        int pos = position;

        File file = new File(restaurantComment.getUserImage());
        Glide.with(context)
                .load(file)
                .circleCrop()
                .into(holder.userImage);

        holder.nickname.setText(restaurantComment.getNickname());
        holder.commentInfo.setText(restaurantComment.getCommentInfo());
        holder.commentLikes.setText(String.valueOf(restaurantComment.getCommentLike()));

        if (!commentLikedList.isEmpty()){
            // 初始化点赞记录
            Iterator<CommentLiked> commentLikedIterator = commentLikedList.iterator();
            while (commentLikedIterator.hasNext()){
                CommentLiked commentLiked = commentLikedIterator.next();
                if (restaurantComment.getCommentId().equals(commentLiked.getCommentId())){
                    holder.likeButton.setBackgroundResource(R.drawable.ic_liked);
                    LikeComment likeComment = new LikeComment();
                    likeComment.setUsername(username);
                    likeComment.setCommentId(restaurantComment.getCommentId());
                    likeComment.setRestaurantName(restaurantName);
                    likeCommentList.add(likeComment);
                    flag[pos] = true;
                }
            }
        }

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag[pos] == false){
                    flag[pos] = true;
                    holder.likeButton.setBackgroundResource(R.drawable.ic_liked);
                    int likes = Integer.parseInt(holder.commentLikes.getText().toString());
                    likes += 1;
                    holder.commentLikes.setText(String.valueOf(likes));
                    LikeComment likeComment = new LikeComment();
                    likeComment.setCommentId(restaurantComment.getCommentId());
                    likeComment.setUsername(username);
                    likeComment.setRestaurantName(restaurantName);
                    likeCommentList.add(likeComment);
                }
                // 取消点赞
                else {
                    flag[pos] = false;
                    holder.likeButton.setBackgroundResource(R.drawable.ic_before_like);
                    int likes = Integer.parseInt(holder.commentLikes.getText().toString());
                    likes -= 1;
                    holder.commentLikes.setText(String.valueOf(likes));
                    LikeComment likeComment = new LikeComment();
                    likeComment.setCommentId(restaurantComment.getCommentId());
                    likeComment.setUsername(username);
                    likeComment.setRestaurantName(restaurantName);
                    if (likeCommentList.remove(likeComment)){
                        Log.i("评论点赞", "onClick: 取消评论点赞成功");
                    }

                }
            }
        });

//        // 响应点赞事件
//        holder.likeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (flag[pos] == false){
//
//                    // 添加点赞数
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .baseUrl(ServerConfig.BASE_URL)
//                            .build();
//                    RestaurantService restaurantService = retrofit.create(RestaurantService.class);
//
//                    LikeComment likeComment = new LikeComment();
//                    likeComment.setCommentId(restaurantComment.getCommentId());
//                    likeComment.setRestaurantName(restaurantName);
//                    likeComment.setUsername(username);
//                    Call<ResponseBody> call = restaurantService.addCommentLike(likeComment);
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            try{
//                                JSONObject jsonObject = JSON.parseObject(response.body().string());
//                                Boolean success = (Boolean) jsonObject.get("success");
//                                Log.i("点赞评论", String.valueOf(jsonObject));
//                                if (success == true){
//                                    int likes = Integer.parseInt(holder.commentLikes.getText().toString());
//                                    Log.i("点赞评论", "点赞后:" + String.valueOf(likes));
//                                    holder.commentLikes.setText(String.valueOf(likes+1));
//                                    flag[pos] = true;
//                                    // 点击变红
//                                    holder.likeButton.setBackgroundResource(R.drawable.ic_liked);
//                                }
//                                else {
//                                    Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
//                                }
//                            }catch (Exception e){
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                        }
//                    });
//                }
//                else {
//                    // 点击变灰
//                    holder.likeButton.setBackgroundResource(R.drawable.ic_before_like);
//
//                    // 减少点赞数
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .baseUrl(ServerConfig.BASE_URL)
//                            .build();
//                    RestaurantService restaurantService = retrofit.create(RestaurantService.class);
//
//                    LikeComment likeComment = new LikeComment();
//                    likeComment.setCommentId(restaurantComment.getCommentId());
//                    likeComment.setRestaurantName(restaurantName);
//                    likeComment.setUsername(username);
//                    Call<ResponseBody> call = restaurantService.cancelCommentLike(likeComment);
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            try{
//                                JSONObject jsonObject = JSON.parseObject(response.body().string());
//                                Boolean success = (Boolean) jsonObject.get("success");
//                                Log.i("取消点赞评论", String.valueOf(jsonObject));
//                                if (success == true){
//                                    int likes = Integer.parseInt(holder.commentLikes.getText().toString());
//                                    Log.i("点赞评论", "取消点赞后:" + String.valueOf(likes));
//                                    holder.commentLikes.setText(String.valueOf(likes-1));
//                                    flag[pos] = false;
//                                }
//                                else {
//                                    Log.i("取消点赞失败", String.valueOf(jsonObject));
//                                }
//                            }catch (Exception e){
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                        }
//                    });
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (this.restaurantCommentList == null){
            return 0;
        }
        return this.restaurantCommentList.size();
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
