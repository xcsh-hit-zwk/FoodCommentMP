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

import com.example.foodcommentmp.Adapters.AdminRestaurantInfoAdaptor;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAdminViewModel;
import com.example.foodcommentmp.pojo.RestaurantOverView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantInfoAdminFragment extends Fragment {

    private RestaurantInfoAdminViewModel restaurantInfoAdminViewModel;

    private RecyclerView recyclerView;
    private AdminRestaurantInfoAdaptor adminRestaurantInfoAdaptor;
    private List<RestaurantOverView> restaurantOverViewList = new ArrayList<>();

    public static RestaurantInfoAdminFragment newInstance(){
        return new RestaurantInfoAdminFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // todo RecycleView的临时假数据，到时候要替换成RestaurantInfo列表
        for(int i = 0 ; i < 50; ++i){
            RestaurantOverView restaurantOverView = new RestaurantOverView();
            restaurantOverView.setRestaurantId("1");
            restaurantOverView.setRestaurantName("相拌一生麻辣拌");
            restaurantOverView.setLikes(i+1);
            restaurantOverView.setRestaurantTag("麻辣拌");
            restaurantOverView.setRestaurantImage("/restaurant/test.jpg");
            restaurantOverView.setRestaurantProvince("山东省");
            restaurantOverView.setRestaurantCity("威海市");
            restaurantOverView.setRestaurantBlock("环翠区");

            restaurantOverViewList.add(restaurantOverView);
        }
        // Inflate the layout for this fragment
        /**
         * 设置RecycleView
         */
        View view = inflater.inflate(R.layout.fragment_restaurant_info_admin_, container, false);
        recyclerView = view.findViewById (R.id.restaurant_info_recycle_view);
        adminRestaurantInfoAdaptor = new AdminRestaurantInfoAdaptor (getActivity(), restaurantOverViewList);
        recyclerView.setLayoutManager (new LinearLayoutManager
                (getActivity (),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator (new DefaultItemAnimator());
        recyclerView.setAdapter (adminRestaurantInfoAdaptor);
        recyclerView.addItemDecoration (new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}