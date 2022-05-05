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

import com.example.foodcommentmp.Adapters.AdminFoodInfoAdapter;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.FoodInfoAdminViewModel;
import com.example.foodcommentmp.pojo.FoodOverView;

import java.util.ArrayList;
import java.util.List;

public class FoodInfoAdminFragment extends Fragment {

    private FoodInfoAdminViewModel foodInfoAdminViewModel;
    private RecyclerView recyclerView;
    private AdminFoodInfoAdapter adminFoodInfoAdapter;
    private List<FoodOverView> foodOverViewList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // todo 假数据
        for(int i = 0; i < 50; ++i){
            FoodOverView foodOverView = new FoodOverView();
            foodOverView.setFoodName("烤冷面");
            foodOverView.setFoodLikes(i+1);
            foodOverView.setFoodImage("/food/test.jpg");
            foodOverView.setRestaurantName("麻辣拌");

            foodOverViewList.add(foodOverView);
        }

        /**
         * 设置RecycleView
         */
        View view = inflater.inflate(R.layout.fragment_food_info_admin_, container, false);
        recyclerView = view.findViewById(R.id.food_info_recycle_view);
        adminFoodInfoAdapter = new AdminFoodInfoAdapter(getActivity(), foodOverViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adminFoodInfoAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}