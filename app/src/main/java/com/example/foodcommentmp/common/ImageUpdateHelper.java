package com.example.foodcommentmp.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.SearchInfo;
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

/**
 * @author: zhangweikun
 * @create: 2022-05-21 9:57
 */
public class ImageUpdateHelper implements TextWatcher {

    private Context context;
    private View mMainView;//操作按钮的View
    private List<EditText> mViewSet;//TextView集合，子类也可以（EditText、TextView、Button）

    public ImageUpdateHelper(Context context, View mMainView) {
        this.context = context;
        this.mMainView = mMainView;
    }

    /**
     * 添加EditText或者TextView监听
     *
     * @param views     传入单个或者多个EditText对象
     */
    public void addViews(EditText... views) {
        if (views == null) return;

        if (mViewSet == null) {
            mViewSet = new ArrayList<>(views.length - 1);
        }

        for (EditText view : views) {
            view.addTextChangedListener(this);
            mViewSet.add(view);
        }
        afterTextChanged(null);
    }

    /**
     * 移除EditText监听，避免内存泄露
     */
    public void removeViews() {
        if (mViewSet == null) return;

        for (EditText view : mViewSet) {
            view.removeTextChangedListener(this);
        }
        mViewSet.clear();
        mViewSet = null;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mViewSet == null) return;
        UserService userService = getUserService();

        if (!mViewSet.get(0).equals("")){
            SearchInfo searchInfo = new SearchInfo();
            searchInfo.setSearchWay("GetUserImage");
            searchInfo.setInfo(mViewSet.get(0).getText().toString());
            Call<ResponseBody> call = userService.getUserImage(searchInfo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        Boolean success = (Boolean) jsonObject.get("success");
                        Log.i("实时更新头像", String.valueOf(jsonObject));
                        if (success == true){
                            String imageUrl = jsonObject.getString("data");
                            File file = new File(imageUrl);
                            Glide.with(context)
                                    .load(file)
                                    .circleCrop()
                                    .into((ImageView) mMainView);
                        }
                        else {
                            Glide.with(context)
                                    .load(R.drawable.ic_default_head_image)
                                    .circleCrop()
                                    .into((ImageView) mMainView);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private UserService getUserService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        UserService userService = retrofit.create(UserService.class);

        return userService;
    }
}
