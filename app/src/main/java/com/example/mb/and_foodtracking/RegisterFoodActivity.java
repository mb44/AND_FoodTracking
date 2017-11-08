package com.example.mb.and_foodtracking;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.id;
import static com.example.mb.and_foodtracking.R.id.foodTemplateView;

public class RegisterFoodActivity extends AppCompatActivity {
    private ArrayList<FoodTemplate>foodTemplates;
    private ArrayAdapter<FoodTemplate> arrayAdapter;
    private GridView gridView;
    private TextView writeTagTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_food);

        writeTagTextView = (TextView)findViewById(R.id.writeTagTextView);
        FoodTemplate food1 = new FoodTemplate("Cow", "2 week", R.drawable.cow);
        FoodTemplate food2 = new FoodTemplate("Pig", "3 week", R.drawable.pig);
        FoodTemplate food3 = new FoodTemplate("Vegetables", "1 week", R.drawable.vegetables);

        FoodTemplate food4 = new FoodTemplate("Milk", "1 week", R.drawable.milk);
        FoodTemplate food5 = new FoodTemplate("Cashew Nuts", "2 week", R.drawable.cashew);
        FoodTemplate food6 = new FoodTemplate("Sugar", "60 week", R.drawable.sugar);

        foodTemplates = new ArrayList<>();
        foodTemplates.add(food1);
        foodTemplates.add(food2);
        foodTemplates.add(food3);
        foodTemplates.add(food4);
        foodTemplates.add(food5);
        foodTemplates.add(food6);


        FoodTemplateAdapter itemsAdapter = new FoodTemplateAdapter(this, foodTemplates);

        gridView = (GridView)findViewById(foodTemplateView);
        gridView.setNumColumns(2);
        gridView.setAdapter(itemsAdapter);

        // 4. Set listeners
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long postition) {
                FoodTemplate ft = (FoodTemplate)parent.getItemAtPosition(position);
                //Toast.makeText(RegisterFoodActivity.this, "Food name: " + ft.getName(), Toast.LENGTH_SHORT).show();
                writeTagTextView.setText("Write " + ft.getName() + " tag...");
                writeTagTextView.setVisibility(View.VISIBLE);
            }
        } );
    }
}
