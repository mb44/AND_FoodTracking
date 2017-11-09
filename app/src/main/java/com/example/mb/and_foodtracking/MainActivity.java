package com.example.mb.and_foodtracking;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.mb.and_foodtracking.R.id.food_template_view;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");

        //mSectionsPageAdapter = new SectionsPageAdapter(this, getSupportFragmentManager());
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // set up the ViewPager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void  setupViewPager(ViewPager viewPager)
    {
        //SectionsPageAdapter adapter = new SectionsPageAdapter(this, getSupportFragmentManager());
        mSectionsPageAdapter.addFragment(new Tab1Fragment(), "Menu");
        mSectionsPageAdapter.addFragment(new Tab2Fragment(), "Register food");
        mSectionsPageAdapter.addFragment(new Tab3Fragment(), "Unregister food");
        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
