package com.example.foodcommentmp.fragments;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.Adapters.AdminFoodInfoAdapter;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.FoodInfoAdminViewModel;
import com.example.foodcommentmp.pojo.FoodOverView;
import com.example.foodcommentmp.retrofit.AdminInfoService;

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
                    Log.i("招牌菜信息列表", String.valueOf(jsonObject));
                    if(success == true){
                        foodInfoAdminViewModel.getFoodOverViewMutableLiveData()
                                .setValue(JSON.parseArray(jsonObject.getString("data"), FoodOverView.class));

                        // 打印数据集
                        List<FoodOverView> temp = foodInfoAdminViewModel
                                .getFoodOverViewMutableLiveData().getValue();
                        Iterator<FoodOverView> iterator = temp.iterator();
                        while (iterator.hasNext()){
                            FoodOverView t = iterator.next();
                            Log.i("招牌菜信息列表", "------------");
                            Log.i("招牌菜信息列表", t.getFoodName());
                            Log.i("招牌菜信息列表", String.valueOf(t.getFoodLikes()));
                            Log.i("招牌菜信息列表", t.getFoodImage());
                            Log.i("招牌菜信息列表", t.getRestaurantName());
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

        adminFoodInfoAdapter.setOnItemListener(new AdminFoodInfoAdapter.OnItemListener() {
            @Override
            public void onClick(View v, int position) {
                adminFoodInfoAdapter.setDefSelect(position);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}