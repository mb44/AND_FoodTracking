package com.example.mb.and_foodtracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class RegisterFoodActivity extends AppCompatActivity {
    private ArrayList<FoodTemplate>foodTemplates;
    private ArrayAdapter<FoodTemplate> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_food);

        FoodTemplate food1 = new FoodTemplate("Cow", "2 week", R.drawable.cow);
        FoodTemplate food2 = new FoodTemplate("Pig", "3 week", R.drawable.pig);
        FoodTemplate food3 = new FoodTemplate("Vegetables", "1 week", R.drawable.vegetables);

        FoodTemplate food4 = new FoodTemplate("Milk", "1 week", -1);
        FoodTemplate food5 = new FoodTemplate("Cashew Nuts", "2 week", -1);
        FoodTemplate food6 = new FoodTemplate("Sugar", "60 week", -1);

        foodTemplates = new ArrayList<>();
        foodTemplates.add(food1);
        foodTemplates.add(food2);
        foodTemplates.add(food3);
        foodTemplates.add(food4);
        foodTemplates.add(food5);
        foodTemplates.add(food6);


        FoodTemplateAdapter itemsAdapter = new FoodTemplateAdapter(this, foodTemplates);

        GridView gridView = (GridView)findViewById(R.id.foodTemplateView);
        gridView.setNumColumns(2);
        gridView.setAdapter(itemsAdapter);
    }
}
