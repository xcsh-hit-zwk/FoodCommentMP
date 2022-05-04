package com.example.foodcommentmp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAdminViewModel;

public class RestaurantInfoAdminFragment extends Fragment {

    private RestaurantInfoAdminViewModel restaurantInfoAdminViewModel;

    public static RestaurantInfoAdminFragment newInstance(){
        return new RestaurantInfoAdminFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_info_admin_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}