package com.example.foodcommentmp.fragments;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.Activitys.RestaurantSearchActivity;
import com.example.foodcommentmp.Adapters.AdminRestaurantInfoAdaptor;
import com.example.foodcommentmp.Adapters.RestaurantOverViewAdapter;
import com.example.foodcommentmp.Adapters.SpinnerAdaptor;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantOverViewViewModel;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.retrofit.RestaurantService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantOverViewFragment extends Fragment {

    private RestaurantOverViewViewModel mViewModel;

    private Spinner spinner;
    private LinearLayout linearLayout;
    private EditText editText;
    private RecyclerView recyclerView;
    private RestaurantOverViewAdapter restaurantOverViewAdapter;

    private String chosen;

    private List<RestaurantOverView> restaurantOverViewList = new ArrayList<>();

    private static final String[] searchMod = new String[]{"城市", "街区", "招牌菜", "餐厅类型"};

    public static RestaurantOverViewFragment newInstance() {
        return new RestaurantOverViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(RestaurantOverViewViewModel.class);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        RestaurantService restaurantService = retrofit.create(RestaurantService.class);

        Map<String, String> cityMap = new HashMap<>();
        cityMap.put("city", "威海");
        Call<ResponseBody> call = restaurantService.getCity(cityMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    Boolean success = (Boolean) jsonObject.get("success");
                    Log.i("用户餐厅信息列表", String.valueOf(jsonObject));
                    if(success == true){
                        mViewModel.getRestaurantOverViewLiveData()
                                .setValue(JSON.parseArray(jsonObject.getString("data"), RestaurantOverView.class));
                        // 打印数据集
                        List<RestaurantOverView> temp = mViewModel.getRestaurantOverViewLiveData().getValue();
                        Iterator<RestaurantOverView> iterator = temp.iterator();
                        while (iterator.hasNext()){
                            RestaurantOverView t = iterator.next();
                            Log.i("用户餐厅信息列表", "------------");
                            Log.i("用户餐厅信息列表", t.getRestaurantName());
                            Log.i("用户餐厅信息列表", String.valueOf(t.getLikes()));
                            Log.i("用户餐厅信息列表", t.getRestaurantTag());
                            Log.i("用户餐厅信息列表", t.getRestaurantPosition());
                            Log.i("用户餐厅信息列表", t.getRestaurantImage());
                            Log.i("用户餐厅信息列表", t.getRestaurantProvince());
                            Log.i("用户餐厅信息列表", t.getRestaurantCity());
                            Log.i("用户餐厅信息列表", t.getRestaurantBlock());
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

        return inflater.inflate(R.layout.restaurant_over_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = getView().findViewById(R.id.search_way_spinner);
        editText = getView().findViewById(R.id.search_edit_text);

        spinner.setAdapter(new SpinnerAdaptor(searchMod, getContext()));
        linearLayout = getView().findViewById(R.id.search_background);

        GradientDrawable background = new GradientDrawable();
        background.setColor(getContext().getResources().getColor(R.color.lightgray));
        background.setCornerRadius(20);
        linearLayout.setBackground(background);

        // 设置下拉框监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //parent就是父控件spinner
            //view就是spinner内填充的textview,id=@android:id/text1
            //position是值所在数组的位置
            //id是值所在行的位置，一般来说与position一致
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                chosen = searchMod[pos];
                Log.i("下拉框", "内容为: " + chosen);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen = searchMod[0];
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RestaurantSearchActivity.class));
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RestaurantSearchActivity.class));
            }
        });

        recyclerView = view.findViewById (R.id.restaurant_overview_recycle_view);
        recyclerView.setLayoutManager (new LinearLayoutManager
                (getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator (new DefaultItemAnimator());
        recyclerView.addItemDecoration (new DividerItemDecoration
                (getActivity(), DividerItemDecoration.VERTICAL));
        restaurantOverViewAdapter = new RestaurantOverViewAdapter(getActivity(), restaurantOverViewList);
        recyclerView.setAdapter (restaurantOverViewAdapter);

        mViewModel.getRestaurantOverViewLiveData()
                .observe(getViewLifecycleOwner(), new Observer<List<RestaurantOverView>>() {
                    @Override
                    public void onChanged(List<RestaurantOverView> restaurantOverViews) {
                        restaurantOverViewList = mViewModel
                                .getRestaurantOverViewLiveData().getValue();
                        restaurantOverViewAdapter.setRestaurantOverViewList(restaurantOverViewList);
                        restaurantOverViewAdapter.notifyDataSetChanged();
                    }
                });

        restaurantOverViewAdapter.setOnItemListener(new RestaurantOverViewAdapter.OnItemListener(){
            @Override
            public void onClick(View v, int position) {
                restaurantOverViewAdapter.setDefSelect(position);
            }
        });

    }

}