package com.example.foodcommentmp.fragments;

import android.content.Context;
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
import com.example.foodcommentmp.Adapters.AdminRestaurantInfoAdaptor;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantInfoAdminViewModel;
import com.example.foodcommentmp.pojo.RestaurantOverView;
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

        restaurantInfoAdminViewModel = new ViewModelProvider(this)
                .get(RestaurantInfoAdminViewModel.class);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);
        Call<ResponseBody> call = adminInfoService.getTotalRestaurantOverView();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    Boolean success = (Boolean) jsonObject.get("success");
                    Log.i("餐厅信息列表", String.valueOf(jsonObject));
                    if(success == true){
                        restaurantInfoAdminViewModel.getRestaurantOverViewMutableLiveData()
                                .setValue( JSON.parseArray(jsonObject.getString("data"), RestaurantOverView.class));

                        // 打印数据集
                        List<RestaurantOverView> temp = restaurantInfoAdminViewModel.getRestaurantOverViewMutableLiveData().getValue();
                        Iterator<RestaurantOverView> iterator = temp.iterator();
                        while (iterator.hasNext()){
                            RestaurantOverView t = iterator.next();
                            Log.i("餐厅信息列表", "------------");
                            Log.i("餐厅信息列表", t.getRestaurantName());
                            Log.i("餐厅信息列表", String.valueOf(t.getLikes()));
                            Log.i("餐厅信息列表", t.getRestaurantTag());
                            Log.i("餐厅信息列表", t.getRestaurantPosition());
                            Log.i("餐厅信息列表", t.getRestaurantImage());
                            Log.i("餐厅信息列表", t.getRestaurantProvince());
                            Log.i("餐厅信息列表", t.getRestaurantCity());
                            Log.i("餐厅信息列表", t.getRestaurantBlock());
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
        View view = inflater.inflate(R.layout.fragment_restaurant_info_admin_, container, false);
        recyclerView = view.findViewById (R.id.restaurant_info_recycle_view);
        recyclerView.setLayoutManager (new LinearLayoutManager
                (getActivity (),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator (new DefaultItemAnimator());
        recyclerView.addItemDecoration (new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));
        adminRestaurantInfoAdaptor = new AdminRestaurantInfoAdaptor (getActivity(), restaurantOverViewList);
        recyclerView.setAdapter (adminRestaurantInfoAdaptor);

        restaurantInfoAdminViewModel.getRestaurantOverViewMutableLiveData()
                .observe(getViewLifecycleOwner(), new Observer<List<RestaurantOverView>>() {
                    @Override
                    public void onChanged(List<RestaurantOverView> restaurantOverViews) {
                        restaurantOverViewList = restaurantInfoAdminViewModel
                                .getRestaurantOverViewMutableLiveData().getValue();
                        adminRestaurantInfoAdaptor.setRestaurantOverViewList(restaurantOverViews);
                        adminRestaurantInfoAdaptor.notifyDataSetChanged();
                    }
                });
        // Inflate the layout for this fragment


        adminRestaurantInfoAdaptor.setOnItemListener(new AdminRestaurantInfoAdaptor.OnItemListener() {
            @Override
            public void onClick(View v, int position) {
                adminRestaurantInfoAdaptor.setDefSelect(position);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}