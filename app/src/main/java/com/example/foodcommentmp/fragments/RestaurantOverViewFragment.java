package com.example.foodcommentmp.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantOverViewViewModel;

public class RestaurantOverViewFragment extends Fragment {

    private RestaurantOverViewViewModel mViewModel;

    public static RestaurantOverViewFragment newInstance() {
        return new RestaurantOverViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(RestaurantOverViewViewModel.class);

        return inflater.inflate(R.layout.restaurant_over_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}