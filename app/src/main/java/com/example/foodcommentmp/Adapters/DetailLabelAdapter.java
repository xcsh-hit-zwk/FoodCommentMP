package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcommentmp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-16 16:45
 */
public class DetailLabelAdapter extends RecyclerView.Adapter<DetailLabelAdapter.DetailLabelHolder> {

    Context context;
    private List<String> labelList;

    private String label;

    public DetailLabelAdapter(Context context) {
        this.context = context;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    @NonNull
    @Override
    public DetailLabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_detail_label_item, parent, false);
        DetailLabelHolder detailLabelHolder = new DetailLabelHolder(view);

        return detailLabelHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailLabelHolder holder, int position) {
        String labelInfo = labelList.get(position);
        holder.labelInfo.setText(labelInfo);
    }

    @Override
    public int getItemCount() {
        if (this.labelList == null){
            return 0;
        }
        return this.labelList.size();
    }

    class DetailLabelHolder extends RecyclerView.ViewHolder{
        private final TextView labelInfo;

        public DetailLabelHolder(@NonNull View view) {
            super(view);
            labelInfo = (TextView) view.findViewById(R.id.restaurant_detail_label_item_label_info);
        }

        public TextView getLabelInfo() {
            return labelInfo;
        }
    }
}
