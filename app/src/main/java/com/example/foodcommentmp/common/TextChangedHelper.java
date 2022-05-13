package com.example.foodcommentmp.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Config.ImageConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-08 9:57
 */
public class TextChangedHelper implements TextWatcher {

    private View mMainView;//操作按钮的View
    private List<EditText> mViewSet;//TextView集合，子类也可以（EditText、TextView、Button）
    private boolean isAlpha;//是否设置透明度
    private Context context;

    public TextChangedHelper(View view, Context context){
        this(view, true, context);
    }

    public TextChangedHelper(View view, boolean alpha, Context context){
        if(view == null){
            throw new IllegalArgumentException("The view is empty");
        }
        mMainView = view;
        isAlpha = alpha;
        this.context = context;
    }

    /**
     * 添加EditText或者TextView监听
     *
     * @param views     传入单个或者多个EditText对象
     */
    public void addViews(EditText... views){
        if(views == null){
            return;
        }

        if(mViewSet == null){
            mViewSet = new ArrayList<>(views.length - 1);
        }

        for(EditText view : views){
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

    // TextWatcher

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if(mViewSet == null){
            return;
        }

        File file = new File(ImageConfig.DIR + mViewSet.get(0).getText().toString());

        Glide.with(context)
                .load(file)
                .circleCrop()
                .into((ImageView) mMainView);
    }

}
