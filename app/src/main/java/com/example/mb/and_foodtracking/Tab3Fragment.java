package com.example.mb.and_foodtracking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private Button approachNFCTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment,container,false);

        /*
        context = (MainActivity) getActivity();
        settings = context.getSharedPreferences("pref_file", MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(getString(R.string.settings_isWriting), false);
        editor.putBoolean(getString(R.string.settings_isClearing), true);

        editor.commit();
        */
        /*
        approachNFCTextView = (Button) view.findViewById(R.id.cancelEraseNFCButton);

        approachNFCTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 3",Toast.LENGTH_SHORT).show();
            }
        });
        */


        return view;
    }
}
