package com.example.foodcommentmp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.foodcommentmp.Activitys.AdminFoodInfoAddActivity;
import com.example.foodcommentmp.Activitys.AdminLoginActivity;
import com.example.foodcommentmp.Activitys.AdminRestaurantInfoAddActivity;
import com.example.foodcommentmp.Adapters.AdminFoodInfoAdapter;
import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.FoodInfoAdminViewModel;
import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodInfoAdminFragment extends Fragment {

    private FoodInfoAdminViewModel foodInfoAdminViewModel;

    private RecyclerView recyclerView;
    private AdminFoodInfoAdapter adminFoodInfoAdapter;
    private List<FoodOverView> foodOverViewList = new ArrayList<>();

    private ImageButton addButton;
    private ImageButton exitButton;
    private ImageView background;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        foodInfoAdminViewModel = new ViewModelProvider(this)
                .get(FoodInfoAdminViewModel.class);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();

        AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);
        Call<ResponseBody> call = adminInfoService.getTotalFoodOverView();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    Boolean success = (Boolean) jsonObject.get("success");
                    Log.i("?????????????????????", String.valueOf(jsonObject));
                    if(success == true){
                        foodInfoAdminViewModel.getFoodOverViewMutableLiveData()
                                .setValue(JSON.parseArray(jsonObject.getString("data"), FoodOverView.class));

                        // ???????????????
                        List<FoodOverView> temp = foodInfoAdminViewModel
                                .getFoodOverViewMutableLiveData().getValue();
                        Iterator<FoodOverView> iterator = temp.iterator();
                        while (iterator.hasNext()){
                            FoodOverView t = iterator.next();
                            Log.i("?????????????????????", "------------");
                            Log.i("?????????????????????", t.getFoodName());
                            Log.i("?????????????????????", String.valueOf(t.getFoodLikes()));
                            Log.i("?????????????????????", t.getFoodImage());
                            Log.i("?????????????????????", t.getRestaurantName());
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        /**
         * ??????RecycleView
         */
        View view = inflater.inflate(R.layout.fragment_food_info_admin_, container, false);

        sharedPreferences = view.getContext().getSharedPreferences("admin_account", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        recyclerView = view.findViewById(R.id.food_info_recycle_view);
        adminFoodInfoAdapter = new AdminFoodInfoAdapter(getActivity(), foodOverViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adminFoodInfoAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

        foodInfoAdminViewModel.getFoodOverViewMutableLiveData()
                .observe(getViewLifecycleOwner(), new Observer<List<FoodOverView>>() {
                    @Override
                    public void onChanged(List<FoodOverView> foodOverViewList) {
                        foodOverViewList = foodInfoAdminViewModel
                                .getFoodOverViewMutableLiveData().getValue();
                        adminFoodInfoAdapter.setFoodOverViewList(foodOverViewList);
                        adminFoodInfoAdapter.notifyDataSetChanged();
                    }
                });

        background = view.findViewById(R.id.admin_food_background);
        exitButton = view.findViewById(R.id.admin_food_exit_button);

        File file = null;
        try {
            file = new File(ImageConfig.DIR + "/background/user_background.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
        Glide.with(view.getContext())
                .load(file)
                .centerCrop()
                .into(background);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                startActivity(new Intent(getContext(), AdminLoginActivity.class));
            }
        });

        adminFoodInfoAdapter.setOnItemListener(new AdminFoodInfoAdapter.OnItemListener() {
            @Override
            public void onClick(View v, int position) {
                adminFoodInfoAdapter.setDefSelect(position);
            }
        });

        // ?????????????????????????????????
        addButton = view.findViewById(R.id.admin_food_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AdminFoodInfoAddActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}