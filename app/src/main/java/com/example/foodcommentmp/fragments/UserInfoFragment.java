package com.example.foodcommentmp.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Adapters.UserInfoCommentAdapter;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Activitys.ModifyUserInfoActivity;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.UserInfoViewModel;
import com.example.foodcommentmp.common.MD5;
import com.example.foodcommentmp.pojo.Account;
import com.example.foodcommentmp.pojo.User;
import com.example.foodcommentmp.pojo.UserInfoComment;
import com.example.foodcommentmp.retrofit.UserService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel mViewModel;
    private ImageView background;
    private ImageView userImage;
    private ImageButton modifyUserInfoButton;
    private TextView nickname;

    private RecyclerView recyclerView;
    private UserInfoCommentAdapter userInfoCommentAdapter;
    private SharedPreferences sharedPreferences;

    private User user;

    private List<UserInfoComment> userInfoCommentList = new ArrayList<>();
    private int FLAG = 0;

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

        sharedPreferences = getContext().getSharedPreferences("user_account", Context.MODE_PRIVATE);

        background = view.findViewById(R.id.user_background);
        userImage = view.findViewById(R.id.user_image);
        modifyUserInfoButton = view.findViewById(R.id.modify_user_info);
        nickname = view.findViewById(R.id.user_nickname);

        Account account = new Account();
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        account.setUsername(username);
        account.setPassword(MD5.string2MD5(password));

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        UserService userService = retrofit.create(UserService.class);

        Call<ResponseBody> call = userService.getUser(account);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    Boolean success = (Boolean) jsonObject.get("success");
                    Log.i("获取用户信息", String.valueOf(jsonObject));
                    if (success == true){
                        FLAG = 1;
                        user = jsonObject.getObject("data", User.class);
                        mViewModel.getGetUserSuccessLiveData().setValue(true);
                    }
                    else {
                        Log.i("获取用户信息", "获取用户信息失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        File file = new File(ImageConfig.DIR + "/background/user_background.jpg");
        // Glide加载图片
        Glide.with(getContext())
                .load(file)
                .centerCrop()
                .into(background);


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

        modifyUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ModifyUserInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("head_image", ImageConfig.DIR + user.getUserImage());
                bundle.putString("nickname", user.getNickname());
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });

        mViewModel.getGetUserSuccessLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == true){
                    nickname.setText(user.getNickname());
                    File imageFile = new File(ImageConfig.DIR + user.getUserImage());
                    Glide.with(getContext())
                            .load(imageFile)
                            .circleCrop()
                            .into(userImage);
                }
            }
        });
    }

}