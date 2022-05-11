package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.foodcommentmp.R;

/**
 * @author: zhangweikun
 * @create: 2022-05-11 17:23
 */
public class SpinnerAdaptor extends BaseAdapter {

    private String[] searchMod;
    private Context context;
    private ViewHolder mViewHolder;

    public SpinnerAdaptor(String[] searchMod, Context context){
        this.searchMod = searchMod;
        this.context = context;
    }

    @Override
    public int getCount() {
        return searchMod.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view  = View.inflate(context, R.layout.spinner_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.textView = view.findViewById(R.id.show_spinner);
            view.setTag(mViewHolder);
        }
        else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        mViewHolder.textView.setText(searchMod[i]);

        return view;
    }

    class ViewHolder{
        private TextView textView;
    }
}
