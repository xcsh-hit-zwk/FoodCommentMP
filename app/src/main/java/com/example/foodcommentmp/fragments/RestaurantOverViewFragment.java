package com.example.foodcommentmp.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.foodcommentmp.Adapters.RestaurantOverViewAdapter;
import com.example.foodcommentmp.Adapters.SpinnerAdaptor;
import com.example.foodcommentmp.Config.ServerConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantOverViewViewModel;
import com.example.foodcommentmp.pojo.RestaurantOverView;
import com.example.foodcommentmp.pojo.SearchInfo;
import com.example.foodcommentmp.retrofit.RestaurantService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    // 搜索框
    private EditText editText;
    private RecyclerView recyclerView;
    private RestaurantOverViewAdapter restaurantOverViewAdapter;
    private ImageView searchIcon;
    private ImageButton searchButton;

    private ListPopupWindow listPopupWindow;

    private String chosen;

    private List<RestaurantOverView> restaurantOverViewList = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final int SHOW_HISTORY_COUNTS = 5;
    private static final int MAX_HISTORY = 100;
    private String[] historyList = new String[1];

    private static final String[] searchMod = new String[]{"城市", "街区", "招牌菜", "餐厅类型"};

    public static RestaurantOverViewFragment newInstance() {
        return new RestaurantOverViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(RestaurantOverViewViewModel.class);

        sharedPreferences = getContext().getSharedPreferences("restaurant_search_history", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        RestaurantService restaurantService = retrofit.create(RestaurantService.class);

        Call<ResponseBody> call = restaurantService.getRestaurantOverView(new SearchInfo("城市", ""));
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
        searchIcon = getView().findViewById(R.id.search_icon);
        searchButton = getView().findViewById(R.id.search_image_button);

        GradientDrawable background = new GradientDrawable();
        background.setColor(getContext().getResources().getColor(R.color.lightgray));
        background.setCornerRadius(20);
        linearLayout.setBackground(background);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHistory();
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHistory();
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
            }
        });

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

        // 初始化RecycleView
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int historyCounts = sharedPreferences.getInt("history_counts", 0);
                String searchInfoStr = editText.getText().toString();
                if (!searchInfoStr.equals("")){
//                    editor.putString("Key"+String.valueOf(historyCounts+1), searchInfoStr);
//                    editor.remove("history_counts");
//                    editor.putInt("history_counts", historyCounts+1);
//                    editor.commit();
                    checkSearchInfo(searchInfoStr);
                }

                SearchInfo searchInfo = new SearchInfo(chosen, editText.getText().toString());
                getRestaurantOverView(searchInfo);
            }
        });

    }

    private void showHistory(){
        int historyCounts = sharedPreferences.getInt("history_counts", 0);
        if(historyCounts >= 5){
            historyList = new String[6];
            historyList[SHOW_HISTORY_COUNTS] = "清空历史记录";
        }
        else {
            historyList = new String[historyCounts+1];
            historyList[historyCounts] = "清空历史记录";
        }
        for (int i = 0; i < historyCounts; i++){
            historyList[i] = sharedPreferences.getString("Key"+String.valueOf(historyCounts-i), "");
        }
        listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setAdapter(new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, historyList));
        listPopupWindow.setAnchorView(editText);
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == historyList.length-1){
                    editor.clear();
                    editor.commit();
                    historyList = new String[1];
                    historyList[0] = "清空历史记录";
                }
                else {
                    editText.setText(historyList[i]);
                }

                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();

        mViewModel.getRestaurantOverViewLiveData()
                .observe(this, new Observer<List<RestaurantOverView>>() {
            @Override
            public void onChanged(List<RestaurantOverView> restaurantOverViews) {
                restaurantOverViewAdapter
                        .setRestaurantOverViewList(mViewModel.getRestaurantOverViewLiveData().getValue());
                restaurantOverViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getRestaurantOverView(SearchInfo searchInfo){
        RestaurantService restaurantService = setRetrofit();

        Call<ResponseBody> call = restaurantService.getRestaurantOverView(searchInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    Boolean success = (Boolean) jsonObject.get("success");
                    Log.i("用户餐厅信息列表", String.valueOf(jsonObject));
                    if (success == true){
                        mViewModel.getRestaurantOverViewLiveData()
                                .setValue( JSON.parseArray(jsonObject.getString("data"), RestaurantOverView.class));
                    }
                    else {
                        Log.i("用户餐厅信息列表", "获取用户餐厅列表失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private RestaurantService setRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerConfig.BASE_URL)
                .build();
        RestaurantService restaurantService = retrofit.create(RestaurantService.class);

        return restaurantService;
    }

    // 检查当前历史搜索记录是否在最新五条之内, 并且再检查一下是否超过历史记录最多承载量
    public void checkSearchInfo(String input){
        String[] checkList = null;
        int historyCounts = sharedPreferences.getInt("history_counts", 0);
        if (historyCounts >= 5){
            checkList = new String[5];
        }
        else {
            checkList = new String[historyCounts];
        }

        int position = -1;
        for (int i = 0; i < checkList.length; i++){
            checkList[i] = sharedPreferences.getString("Key"+String.valueOf(historyCounts-i), "");
            if(checkList[i].equals(input)){
                position = i;
            }
            editor.remove("Key"+String.valueOf(historyCounts-i));
            historyCounts--;
        }
        if (historyCounts + checkList.length > MAX_HISTORY){
            editor.clear();
            historyCounts = 0;
        }
        editor.commit();

        if(position != -1){
            for(int i = position; i < checkList.length-1; ++i){
                checkList[i] = checkList[i+1];
            }
            checkList[checkList.length-1] = input;

            for(int i = 0; i < checkList.length; ++i){
                editor.putString("Key"+String.valueOf(historyCounts+1), checkList[i]);
                historyCounts++;
            }
            editor.remove("history_counts");
            editor.putInt("history_counts", historyCounts);
        }
        else {
            for(int i = 0; i < checkList.length; ++i){
                editor.putString("Key"+String.valueOf(historyCounts+1), checkList[i]);
                historyCounts++;
            }
            editor.putString("Key"+String.valueOf(historyCounts+1), input);
            historyCounts++;
            editor.remove("history_counts");
            editor.putInt("history_counts", historyCounts);
        }
        editor.commit();
    }
}