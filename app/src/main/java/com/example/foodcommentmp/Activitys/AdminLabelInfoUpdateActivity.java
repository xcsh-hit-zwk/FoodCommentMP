package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.LabelOverView;

public class AdminLabelInfoUpdateActivity extends AppCompatActivity {

    private ImageButton deleteButton;
    private ImageButton updateButton;

    private EditText labelNameEditText;
    private EditText restaurantNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_label_info_update);

        LabelOverView labelOverView = (LabelOverView) getIntent()
                .getParcelableExtra("标签信息删除更新");

        deleteButton = findViewById(R.id.delete_label_image_button);
        updateButton = findViewById(R.id.update_label_image_button);

        labelNameEditText = findViewById(R.id.label_name);
        restaurantNameEditText = findViewById(R.id.label_restaurant_name);

        // 填写传递过来的信息
        labelNameEditText.setText(labelOverView.getLabelName());
        restaurantNameEditText.setText(labelOverView.getRestaurantName());

        // 删除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo 这里需要写网络接口，直接在数据库中进行删除
                Log.i("标签更新信息", "删除");
            }
        });

        // 更新
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 更新对象
                labelOverView.setLabelName(labelNameEditText.getText().toString());
                labelOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                // todo 这里需要写网络接口，直接更新到数据库中
                Log.i("标签更新信息", "更新后对象:");
                Log.i("标签更新信息", labelOverView.getLabelName());
                Log.i("标签更新信息", labelOverView.getRestaurantName());
            }
        });

    }
}