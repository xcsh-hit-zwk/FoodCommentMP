package com.example.foodcommentmp.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Adapters.UserInfoCommentAdapter;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.UserInfoViewModel;
import com.example.foodcommentmp.pojo.UserInfoComment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel mViewModel;
    private ImageView background;
    private ImageView userImage;

    private RecyclerView recyclerView;
    private UserInfoCommentAdapter userInfoCommentAdapter;

    private List<UserInfoComment> userInfoCommentList = new ArrayList<>();

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        return inflater.inflate(R.layout.user_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        background = view.findViewById(R.id.user_background);
        userImage = view.findViewById(R.id.user_image);

        File file = new File(ImageConfig.DIR + "/background/user_background.jpg");
        // todo 这个头像后期换成数据库头像
        File imageFile = new File(ImageConfig.DIR + "/head_image/yuguigou.jpg");
        // Glide加载图片
        Glide.with(getContext())
                .load(file)
                .centerCrop()
                .into(background);

        Glide.with(getContext())
                .load(imageFile)
                .circleCrop()
                .into(userImage);

        // todo 这里是假数据
        for(int i = 0; i < 6; ++i){
            UserInfoComment userInfoComment = new UserInfoComment();
            userInfoComment.setUsername("1");
            userInfoComment.setCommentInfo("评论内容");
            userInfoComment.setUserImage("/head_image/yuguigou.jpg");
            userInfoComment.setRestaurantImage("/restaurant/test.jpg");
            userInfoComment.setNickname("新茶试火");
            userInfoComment.setRestaurantName("相伴一生麻辣拌");
            userInfoComment.setRestaurantPosition("山东省 威海市 环翠区  ");
            userInfoComment.setRestaurantTag("麻辣拌");

            userInfoCommentList.add(userInfoComment);
        }

        // 初始化RecycleView
        recyclerView = view.findViewById(R.id.user_info_comment_recycle_view);
        recyclerView.setLayoutManager (new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator (new DefaultItemAnimator());
        recyclerView.addItemDecoration (new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));
        userInfoCommentAdapter = new UserInfoCommentAdapter(getActivity(), userInfoCommentList);
        recyclerView.setAdapter(userInfoCommentAdapter);
        userInfoCommentAdapter.notifyDataSetChanged();

//        userInfoCommentAdapter.setOnItemListener(new UserInfoCommentAdapter.OnItemListener() {
//            @Override
//            public void onClick(View v, int position) {
//                userInfoCommentAdapter.setDefSelect(position);
//            }
//        });
    }

}