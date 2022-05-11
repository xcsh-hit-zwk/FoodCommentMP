package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.foodcommentmp.Adapters.SpinnerAdaptor;
import com.example.foodcommentmp.R;

public class RestaurantSearchActivity extends AppCompatActivity {

    private Spinner spinner;
    private LinearLayout linearLayout;

    private String chosen;

    private static final String[] searchMod = new String[]{"城市", "街区", "招牌菜", "餐厅类型"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        spinner = findViewById(R.id.detail_search_way_spinner);
        linearLayout = findViewById(R.id.detail_search_background);

        spinner.setAdapter(new SpinnerAdaptor(searchMod, this));

        GradientDrawable background = new GradientDrawable();
        background.setColor(this.getResources().getColor(R.color.lightgray));
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

    }
}