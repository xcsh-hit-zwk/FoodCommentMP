package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.LabelOverView;

import java.io.File;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-05 19:10
 */
public class AdminLabelInfoAdapter extends RecyclerView.Adapter<AdminLabelInfoAdapter.AdminLabelInfoHolder> {

    Context context;
    private List<LabelOverView> labelOverViewList;

    class AdminLabelInfoHolder extends RecyclerView.ViewHolder{

        private final TextView labelNameTextView;
        private final TextView labelRestaurantTextView;

        public TextView getLabelNameTextView() {
            return labelNameTextView;
        }

        public TextView getLabelRestaurantTextView() {
            return labelRestaurantTextView;
        }

        public AdminLabelInfoHolder(View view){
            super(view);
            // Define click listener for the ViewHolder's View
            labelNameTextView = (TextView) view
                    .findViewById(R.id.label_info_admin_label_name_text_view);
            labelRestaurantTextView = (TextView) view
                    .findViewById(R.id.label_info_admin_restaurant_name_text_view);
        }
    }

    /**
     * 初始化适配齐的数据集合
     * @param context
     * @param labelOverViewList 适配器使用的数据集
     */
    public AdminLabelInfoAdapter(Context context, List<LabelOverView> labelOverViewList){
        this.context = context;
        this.labelOverViewList = labelOverViewList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdminLabelInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.label_info_item, parent, false);
        AdminLabelInfoHolder adminLabelInfoHolder = new AdminLabelInfoHolder(view);

        return adminLabelInfoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminLabelInfoHolder holder, int position){
        LabelOverView labelOverView = labelOverViewList.get(position);
        holder.getLabelNameTextView().setText(labelOverView.getLabelName());
        holder.getLabelRestaurantTextView().setText(labelOverView.getRestaurantName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.labelOverViewList.size();
    }
}
