package com.example.foodcommentmp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.foodcommentmp.Config.ImageConfig;
import com.example.foodcommentmp.R;
import com.example.foodcommentmp.pojo.FoodOverView;


public class AdminFoodInfoUpdateActivity extends AppCompatActivity {

    private ImageButton deleteButton;
    private ImageButton updateButton;

    private EditText foodNameEditText;
    private EditText restaurantNameEditText;
    private EditText foodImageEditText;

    String oldFoodName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_info_update_activity);


        FoodOverView foodOverView = (FoodOverView) getIntent()
                .getParcelableExtra("招牌菜信息删除更新");

        oldFoodName = foodOverView.getFoodName();

        deleteButton = findViewById(R.id.delete_food_image_button);
        updateButton = findViewById(R.id.update_food_image_button);

        foodNameEditText = findViewById(R.id.food_name);
        restaurantNameEditText = findViewById(R.id.food_restaurant_name);
        foodImageEditText = findViewById(R.id.food_image);

        // 填写传递过来的信息
        foodNameEditText.setText(foodOverView.getFoodName());
        restaurantNameEditText.setText(foodOverView.getRestaurantName());
        foodImageEditText.setText(foodOverView.getFoodImage());

        // 删除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo 这里需要写网络接口，直接在数据库中进行删除
                Log.i("招牌菜更新信息", "删除");
            }
        });

        // 更新
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newFoodName = foodNameEditText.getText().toString();

                // 更新对象
                foodOverView.setFoodName(newFoodName);
                foodOverView.setRestaurantName(restaurantNameEditText.getText().toString());
                foodOverView.setFoodImage(foodImageEditText.getText().toString());

                // 如果招牌菜名称换了，说明换菜了，就清空点赞数
                if(!oldFoodName.equals(newFoodName)){
                    foodOverView.setFoodLikes(0);
                }

                // todo 这里需要写网络接口，直接更新到数据库中
                Log.i("招牌菜更新信息", "更新后对象:");
                Log.i("招牌菜更新信息", foodOverView.getFoodName());
                Log.i("招牌菜更新信息", String.valueOf(foodOverView.getFoodLikes()));
                Log.i("招牌菜更新信息", foodOverView.getRestaurantName());
                Log.i("招牌菜更新信息", foodOverView.getFoodImage());

            }
        });
    }
}