package com.example.foodcommentmp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcommentmp.Activitys.AdminLabelInfoUpdateActivity;
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

    // 默认值
    private int defItem = -1;
    private OnItemListener onItemListener;

    // 选中对象存储值
    private LabelOverView chosen;

    /**
     * 初始化适配器的数据集合
     * @param context
     * @param labelOverViewList 适配器使用的数据集
     */
    public AdminLabelInfoAdapter(Context context, List<LabelOverView> labelOverViewList){
        this.context = context;
        this.labelOverViewList = labelOverViewList;
    }

    public void setLabelOverViewList(List<LabelOverView> labelOverViewList){
        this.labelOverViewList = labelOverViewList;
    }

    public void setOnItemListener(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }

    // 设置点击事件
    public interface OnItemListener{
        void onClick(View v, int position);
    }

    // 获取点击位置
    public void setDefSelect(int position){
        this.defItem = position;
        notifyDataSetChanged();
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

        // 设置单选事件
        if(defItem != -1){

            if (defItem == position){
                // 获取选中对象
                chosen = labelOverView;
                Log.i("管理员标签信息", "对象为:");
                Log.i("管理员标签信息", chosen.getLabelName());
                Log.i("管理员标签信息", chosen.getRestaurantName());

                Intent intent = new Intent(context, AdminLabelInfoUpdateActivity.class);
                intent.putExtra("标签信息删除更新", chosen);
                context.startActivity(intent);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.labelOverViewList.size();
    }

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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemListener != null){
                        onItemListener.onClick(view, getLayoutPosition());
                    }
                }
            });
        }
    }
}
