package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.RestaurantOverView;

public class AdminRestaurantInfoUpdateActivity extends AppCompatActivity {

    private EditText restaurantNameEditText;
    private EditText restaurantTagEditText;
    private EditText restaurantPositionEditText;
    private EditText restaurantImageEditText;
    private EditText restaurantProvinceEditText;
    private EditText restaurantCityEditText;
    private EditText restaurantBlockEditText;

    private ImageButton deleteButton;
    private ImageButton updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_restaurant_info_update);

        RestaurantOverView restaurantOverView = (RestaurantOverView) getIntent()
                .getParcelableExtra("餐厅信息删除更新");

        deleteButton = findViewById(R.id.delete_restaurant_image_button);
        updateButton = findViewById(R.id.update_restaurant_image_button);

        restaurantNameEditText = findViewById(R.id.restaurant_name);
        restaurantTagEditText = findViewById(R.id.restaurant_tag);
        restaurantPositionEditText = findViewById(R.id.restaurant_position);
        restaurantImageEditText = findViewById(R.id.restaurant_Image);
        restaurantProvinceEditText = findViewById(R.id.restaurant_Province);
        restaurantCityEditText = findViewById(R.id.restaurant_city);
        restaurantBlockEditText = findViewById(R.id.restaurant_block);

        // 填写传递过来的信息
        restaurantNameEditText.setText(restaurantOverView.getRestaurantName());
        restaurantTagEditText.setText(restaurantOverView.getRestaurantTag());
        restaurantPositionEditText.setText(restaurantOverView.getRestaurantPosition());
        restaurantImageEditText.setText(restaurantOverView.getRestaurantImage());
        restaurantProvinceEditText.setText(restaurantOverView.getRestaurantProvince());
        restaurantCityEditText.setText(restaurantOverView.getRestaurantCity());
        restaurantBlockEditText.setText(restaurantOverView.getRestaurantBlock());

        // 删除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo 这里需要写网络接口，直接在数据库中进行删除
                Log.i("餐厅更新信息", "删除");
            }
        });

        // 更新
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 更新对象
                restaurantOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                restaurantOverView.setRestaurantTag(restaurantTagEditText.getText().toString());
                restaurantOverView.setRestaurantPosition(restaurantPositionEditText.getText().toString());
                restaurantOverView.setRestaurantImage(restaurantImageEditText.getText().toString());
                restaurantOverView.setRestaurantProvince(restaurantProvinceEditText.getText().toString());
                restaurantOverView.setRestaurantCity(restaurantCityEditText.getText().toString());
                restaurantOverView.setRestaurantBlock(restaurantBlockEditText.getText().toString());

                // todo 这里需要写网络接口，直接更新到数据库中
                Log.i("餐厅更新信息", "更新后对象:");
                Log.i("餐厅更新信息", restaurantOverView.getRestaurantName());
                Log.i("餐厅更新信息", restaurantOverView.getRestaurantTag());
                Log.i("餐厅更新信息", restaurantOverView.getRestaurantPosition());
                Log.i("餐厅更新信息", restaurantOverView.getRestaurantImage());
                Log.i("餐厅更新信息", restaurantOverView.getRestaurantProvince());
                Log.i("餐厅更新信息", restaurantOverView.getRestaurantCity());
                Log.i("餐厅更新信息", restaurantOverView.getRestaurantBlock());
            }
        });
    }
}