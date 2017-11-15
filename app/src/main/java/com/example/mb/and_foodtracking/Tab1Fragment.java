package com.example.mb.and_foodtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private MainActivity context;
    private RelativeLayout frontPageLayout;
    private RelativeLayout foodStatusLayout;

    private Button showfoodStatusButton;
    private Button closeFoodStatusButton;

    private Spinner sortSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        context = (MainActivity) getActivity();

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
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }
}
