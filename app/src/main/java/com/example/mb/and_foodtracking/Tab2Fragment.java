package com.example.mb.and_foodtracking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.example.mb.and_foodtracking.arrayadapters.FoodTemplateAdapter;
import com.example.mb.and_foodtracking.model.FoodTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class Tab2Fragment extends Fragment {
    private static String TAG = "Tab2Fragment";

    private MainActivity context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    // Layout
    private RelativeLayout setDateLayout;
    private RelativeLayout approachNFCLayout;

    private GridView gridView;
    private Button okButton;
    private Button cancelButton;
    private Button cancelNFCButton;

    // List and adapter
    private ArrayList<FoodTemplate>foodTemplates;
    private FoodTemplateAdapter arrayAdapter;

    private int expYear;
    private int expMonth;
    private int expDate;

    private int selectedFoodId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);
        FoodTemplate food1 = new FoodTemplate(2,"Cashew Nuts", "2 week", R.drawable.cashew);
        FoodTemplate food2 = new FoodTemplate(1, "Cow", "2 week", R.drawable.cow);
        FoodTemplate food3 = new FoodTemplate(3, "Pig", "3 week", R.drawable.pig);
        FoodTemplate food4 = new FoodTemplate(4, "Vegetables", "1 week", R.drawable.vegetables);
        FoodTemplate food5 = new FoodTemplate(5, "Milk", "1 week", R.drawable.milk);
        FoodTemplate food6 = new FoodTemplate(0,"Sugar", "60 week", R.drawable.sugar);

        foodTemplates = new ArrayList<>();
        foodTemplates.add(food1);
        foodTemplates.add(food2);
        foodTemplates.add(food3);
        foodTemplates.add(food4);
        foodTemplates.add(food5);
        foodTemplates.add(food6);

        context = (MainActivity) getActivity();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        settings = context.getSharedPreferences(getString(R.string.settings_filename), MODE_PRIVATE);

        arrayAdapter = new FoodTemplateAdapter(context, foodTemplates);

        gridView = (GridView)view.findViewById(R.id.foodTemplateView);
        gridView.setNumColumns(2);
        gridView.setAdapter(arrayAdapter);

        setDateLayout = (RelativeLayout)view.findViewById(R.id.setExpDateLayout);
        approachNFCLayout = (RelativeLayout)view.findViewById(R.id.approachNFCLayout);

        final DatePicker datePicker = (DatePicker)view.findViewById(R.id.datePicker);

        final Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),
            new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int date) {
                    expYear = year;
                    expMonth = month+1;
                    expDate = date;
                }
        });

        // Set listener on GridView
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodTemplate ft = (FoodTemplate)parent.getItemAtPosition(position);
                selectedFoodId = ft.getId();
                gridView.setAlpha(0.3f);
                setDateLayout.setVisibility(View.VISIBLE);
            }
        });

        // Set Listener on Write NFC Tag Button
        okButton = (Button)view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int regYear = c.get(Calendar.YEAR);
                int regMonth = c.get(Calendar.MONTH);
                int regDate = c.get(Calendar.DAY_OF_WEEK);

                editor = settings.edit();

                editor.putInt(getString(R.string.settings_foodid), selectedFoodId);
                editor.putInt(getString(R.string.settings_regYear), regYear);
                editor.putInt(getString(R.string.settings_regMonth), regMonth);
                editor.putInt(getString(R.string.settings_regDate), regDate);

                editor.putInt(getString(R.string.settings_expYear), expYear);
                editor.putInt(getString(R.string.settings_expMonth), expMonth);
                editor.putInt(getString(R.string.settings_expDate), expDate);
                editor.putBoolean(getString(R.string.settings_isWriting), true);
                editor.commit();
                setDateLayout.setAlpha(0.3f);
                approachNFCLayout.setVisibility(View.VISIBLE);
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateLayout.setVisibility(View.GONE);
                gridView.setAlpha(1);
            }
        });

        cancelNFCButton = (Button)view.findViewById(R.id.cancelNFCButton);
        cancelNFCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = settings.edit();
                editor.putBoolean("isWriting", false);
                editor.commit();

                approachNFCLayout.setVisibility(View.GONE);
                setDateLayout.setVisibility(View.GONE);
                setDateLayout.setAlpha(1);
                gridView.setAlpha(1);
            }
        });

        return view;
    }
}
