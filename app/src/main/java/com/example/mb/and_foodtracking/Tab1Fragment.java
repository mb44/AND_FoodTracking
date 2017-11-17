package com.example.mb.and_foodtracking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mb.and_foodtracking.model.FoodItem;
import com.example.mb.and_foodtracking.model.FoodType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private MainActivity context;
    private RelativeLayout frontPageLayout;
    private RelativeLayout foodStatusLayout;
    private Button showfoodStatusButton;
    private Button closeFoodStatusButton;
    private Spinner sortSpinner;

    private FirebaseDatabase database;
    private DatabaseReference dbRefFoodTypes;
    private DatabaseReference dbRefStorage;

    private ArrayList<FoodItem> foodItems;
    private ArrayList<FoodType> foodTypes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        context = (MainActivity) getActivity();

        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Get the reference
        final DatabaseReference dbRef = database.getReference("messages");
        dbRef.push().setValue("Hello FoodTrack :)");
        */

        frontPageLayout = (RelativeLayout)view.findViewById(R.id.frontpageLayout);
        foodStatusLayout = (RelativeLayout)view.findViewById(R.id.foodStatusLayout);

        showfoodStatusButton = (Button)view.findViewById(R.id.showFoodStatusButton);
        showfoodStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodStatusLayout.setVisibility(View.VISIBLE);
            }
        });

        closeFoodStatusButton = (Button)view.findViewById(R.id.closeFoodStatusButton);
        closeFoodStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodStatusLayout.setVisibility(View.GONE);
            }
        });

        sortSpinner = (Spinner)view.findViewById(R.id.sortSpinner);
        sortSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String text = (String) parent.getItemAtPosition(position);
                //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ListView listView = (ListView)view.findViewById(R.id.foodListView);
        // FoodItem, ArrayAdapter etc
        foodItems = new ArrayList<FoodItem>();
        FoodItemAdapter foodItemAdapter = new FoodItemAdapter(context, foodItems);
        listView.setAdapter(foodItemAdapter);
        // Database
        database = FirebaseDatabase.getInstance();

        // Database: FoodTypes
        foodTypes = new ArrayList<>();
        dbRefFoodTypes = database.getReference("FoodType");
        //dbRef.setValue("Hello, World!");
        dbRefFoodTypes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                foodItems.clear();

                for (DataSnapshot foodType : ds.getChildren()) {
                    FoodType item = foodType.getValue(FoodType.class);
                    foodTypes.add(item);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // Database: Storage
        dbRefStorage = database.getReference("Storage");
        //dbRef.setValue("Hello, World!");
        dbRefStorage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                foodItems.clear();
                for (DataSnapshot foodItem : ds.getChildren()) {
                    FoodItem item = foodItem.getValue(FoodItem.class);
                    String foodName = foodTypes.get(item.getFoodid()).getName();
                    item.setName(foodName);
                    foodItems.add(item);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }
}
