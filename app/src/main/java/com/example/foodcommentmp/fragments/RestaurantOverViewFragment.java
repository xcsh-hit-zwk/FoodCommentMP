package com.example.foodcommentmp.fragments;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.foodcommentmp.Activitys.RestaurantSearchActivity;
import com.example.foodcommentmp.Adapters.SpinnerAdaptor;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.ViewModel.RestaurantOverViewViewModel;

import javax.xml.transform.Result;

public class RestaurantOverViewFragment extends Fragment {

    private RestaurantOverViewViewModel mViewModel;

    private Spinner spinner;
    private LinearLayout linearLayout;
    private EditText editText;

    private String chosen;

    private static final String[] searchMod = new String[]{"城市", "街区", "招牌菜", "餐厅类型"};

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

    }

}