package com.example.mb.and_foodtracking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";

    private MainActivity context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private Button cancelNFCButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment,container,false);

        context = (MainActivity) getActivity();
        cancelNFCButton = (Button)context.findViewById(R.id.cancelEraseNFCButton);

        /*
        cancelNFCButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TabLayout tabLayout = (TabLayout) context.findViewById(R.id.tabs);
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                /tab.select();
            }
        });
        */

        return view;
    }
}
