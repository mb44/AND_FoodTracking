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
import java.util.Collections;
import java.util.Comparator;

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private MainActivity context;
    private RelativeLayout frontPageLayout;
    private RelativeLayout foodStatusLayout;
    private Button showfoodStatusButton;
    private Button closeFoodStatusButton;
    private Spinner sortSpinner;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private ArrayList<FoodItem> foodItems;
    private ArrayList<FoodType> foodTypes;
    private FoodItemAdapter foodItemAdapter;

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

                switch (text) {
                    case "Expiry date":
                        //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        Collections.sort(foodItems, new Comparator<FoodItem>() {
                            @Override
                            public int compare(FoodItem foodItem1, FoodItem foodItem2)
                            {
                                if (foodItem1.getExpiry().getYear() < foodItem2.getExpiry().getYear()) {
                                    return -1;
                                } else if (foodItem1.getExpiry().getYear() > foodItem2.getExpiry().getYear()) {
                                    return 1;
                                } else if (foodItem1.getExpiry().getMonth() < foodItem2.getExpiry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getExpiry().getMonth() > foodItem2.getExpiry().getMonth()) {
                                    return 1;
                                } else if (foodItem1.getExpiry().getMonth() < foodItem2.getExpiry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getExpiry().getDate() > foodItem2.getExpiry().getDate()) {
                                    return 1;
                                } else if (foodItem1.getExpiry().getDate() < foodItem2.getExpiry().getDate()) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case "Registry date":
                        Collections.sort(foodItems, new Comparator<FoodItem>() {
                            @Override
                            public int compare(FoodItem foodItem1, FoodItem foodItem2)
                            {
                                if (foodItem1.getRegistry().getYear() < foodItem2.getRegistry().getYear()) {
                                    return -1;
                                } else if (foodItem1.getRegistry().getYear() > foodItem2.getRegistry().getYear()) {
                                    return 1;
                                } else if (foodItem1.getRegistry().getMonth() < foodItem2.getRegistry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getRegistry().getMonth() > foodItem2.getRegistry().getMonth()) {
                                    return 1;
                                } else if (foodItem1.getRegistry().getMonth() < foodItem2.getRegistry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getRegistry().getDate() > foodItem2.getRegistry().getDate()) {
                                    return 1;
                                } else if (foodItem1.getRegistry().getDate() < foodItem2.getRegistry().getDate()) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case "Food type":
                        Collections.sort(foodItems, new Comparator<FoodItem>() {
                            @Override
                            public int compare(FoodItem foodItem1, FoodItem foodItem2)
                            {
                               String foodName1 = foodTypes.get( foodItem1.getFoodid()).getName();
                               String foodName2 = foodTypes.get( foodItem2.getFoodid()).getName();
                               return foodName1.compareTo(foodName2);
                            }
                        });
                        break;
                    default:
                        break;
                }
                foodItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ListView listView = (ListView)view.findViewById(R.id.foodListView);
        // FoodItem, ArrayAdapter etc
        foodItems = new ArrayList<FoodItem>();
        foodItemAdapter = new FoodItemAdapter(context, foodItems);
        listView.setAdapter(foodItemAdapter);
        // Database
        database = FirebaseDatabase.getInstance();

        // Database: FoodTypes
        foodTypes = new ArrayList<>();
        dbRef = database.getReference();
        //dbRef.setValue("Hello, World!");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                foodTypes.clear();
                foodItems.clear();

                DataSnapshot types = ds.child("FoodTypes");
                DataSnapshot storage = ds.child("Storage");

                for (DataSnapshot type : types.getChildren()) {
                    FoodType item = type.getValue(FoodType.class);
                    foodTypes.add(item);
                }

                for (DataSnapshot foodItem : storage.getChildren()) {
                    FoodItem item = foodItem.getValue(FoodItem.class);
                    item.setTagId(foodItem.getKey());
                    item.setName( foodTypes.get( item.getFoodid()).getName());
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
