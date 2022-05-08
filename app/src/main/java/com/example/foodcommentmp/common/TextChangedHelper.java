package com.example.foodcommentmp.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

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

    public TextChangedHelper(View view){
        this(view, true);
    }

    public TextChangedHelper(View view, boolean alpha){
        if(view == null){
            throw new IllegalArgumentException("The view is empty");
        }
        mMainView = view;
        isAlpha = alpha;
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if(mViewSet == null){
            return;
        }

        setEnabled(true);
    }

    /**
     * 设置View的事件
     *
     * @param enabled               启用或者禁用View的事件
     */
    public void setEnabled(boolean enabled) {
        if (enabled == mMainView.isEnabled()) return;

        if (enabled) {
            //启用View的事件
            mMainView.setEnabled(true);
            if (isAlpha) {
                //设置不透明
                mMainView.setAlpha(1f);
            }
        }else {
            //禁用View的事件
            mMainView.setEnabled(false);
            if (isAlpha) {
                //设置半透明
                mMainView.setAlpha(0.5f);
            }
        }
    }

}
