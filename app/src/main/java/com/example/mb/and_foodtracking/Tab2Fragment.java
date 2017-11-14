package com.example.mb.and_foodtracking;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private Button btnTEST;

    private ArrayList<FoodTemplate>foodTemplates;
    private FoodTemplateAdapter arrayAdapter;
    private GridView gridView;

    private Activity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);

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

       // MainActivity main = (MainActivity) getActivity();
        MainActivity context = (MainActivity) getActivity();
        arrayAdapter = new FoodTemplateAdapter(context, foodTemplates);

        gridView = (GridView)view.findViewById(R.id.foodTemplateView);
        gridView.setNumColumns(2);
        gridView.setAdapter(arrayAdapter);
        return view;
    }
}
