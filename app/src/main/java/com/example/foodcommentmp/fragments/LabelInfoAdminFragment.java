package com.example.foodcommentmp.fragments;

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

import com.example.foodcommentmp.Adapters.AdminLabelInfoAdapter;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.LabelInfoAdminViewModel;
import com.example.foodcommentmp.pojo.LabelOverView;

import java.util.ArrayList;
import java.util.List;

public class LabelInfoAdminFragment extends Fragment {

    private LabelInfoAdminViewModel labelInfoAdminViewModel;
    private RecyclerView recyclerView;
    private AdminLabelInfoAdapter adminLabelInfoAdapter;
    private List<LabelOverView> labelOverViewList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // todo 假数据
        for(int i = 0; i < 50; ++i){
            LabelOverView labelOverView = new LabelOverView();
            labelOverView.setLabelName("麻辣烫");
            labelOverView.setRestaurantName("相拌一生麻辣拌");

            labelOverViewList.add(labelOverView);
        }

        /**
         * 设置RecycleView
         */
        View view = inflater.inflate(R.layout.fragment_label_info_admin, container, false);
        recyclerView = view.findViewById(R.id.label_info_recycle_view);
        adminLabelInfoAdapter = new AdminLabelInfoAdapter(getActivity(), labelOverViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adminLabelInfoAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}