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
import com.example.foodcommentmp.Adapters.AdminLabelInfoAdapter;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.LabelInfoAdminViewModel;
import com.example.foodcommentmp.pojo.LabelOverView;
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

public class LabelInfoAdminFragment extends Fragment {

    private LabelInfoAdminViewModel labelInfoAdminViewModel;

    private RecyclerView recyclerView;
    private AdminLabelInfoAdapter adminLabelInfoAdapter;
    private List<LabelOverView> labelOverViewList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        labelInfoAdminViewModel = new ViewModelProvider(this)
                .get(LabelInfoAdminViewModel.class);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        AdminInfoService adminInfoService = retrofit.create(AdminInfoService.class);
        Call<ResponseBody> call = adminInfoService.getTotalLabelOverView();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    Boolean success = (Boolean) jsonObject.get("success");
                    Log.i("标签信息列表", String.valueOf(jsonObject));
                    if(success == true){
                        labelInfoAdminViewModel.getLabelOverViewMutableLiveData()
                                .setValue(JSON.parseArray(jsonObject.getString("data"), LabelOverView.class));

                        // 打印数据集
                        List<LabelOverView> temp = labelInfoAdminViewModel
                                .getLabelOverViewMutableLiveData().getValue();
                        Iterator<LabelOverView> iterator = temp.iterator();
                        while (iterator.hasNext()){
                            LabelOverView t = iterator.next();
                            Log.i("标签信息列表", "------------");
                            Log.i("标签信息列表", t.getLabelName());
                            Log.i("标签信息列表", t.getRestaurantName());
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
        View view = inflater.inflate(R.layout.fragment_label_info_admin, container, false);
        recyclerView = view.findViewById(R.id.label_info_recycle_view);
        adminLabelInfoAdapter = new AdminLabelInfoAdapter(getActivity(), labelOverViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adminLabelInfoAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));

        labelInfoAdminViewModel.getLabelOverViewMutableLiveData()
                .observe(getViewLifecycleOwner(), new Observer<List<LabelOverView>>() {
                    @Override
                    public void onChanged(List<LabelOverView> labelOverViewList) {
                        labelOverViewList = labelInfoAdminViewModel
                                .getLabelOverViewMutableLiveData().getValue();
                        adminLabelInfoAdapter.setLabelOverViewList(labelOverViewList);
                        adminLabelInfoAdapter.notifyDataSetChanged();
                    }
                });

        adminLabelInfoAdapter.setOnItemListener(new AdminLabelInfoAdapter.OnItemListener() {
            @Override
            public void onClick(View v, int position) {
                adminLabelInfoAdapter.setDefSelect(position);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}