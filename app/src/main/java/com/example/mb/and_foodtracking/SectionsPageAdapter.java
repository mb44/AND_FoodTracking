package com.example.mb.and_foodtracking;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class SectionsPageAdapter extends FragmentPagerAdapter {
    //private Activity context;
    private GridView gridView;
    private ArrayList<FoodTemplate> foodTemplates;
    private ArrayAdapter<FoodTemplate> arrayAdapter;


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm)
    {
        super(fm);
    }
    /*
    public SectionsPageAdapter(AppCompatActivity context, FragmentManager fm)
    {
        super(fm);
        this.context = context;
    }
    */

    public void addFragment(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
                    /*
        switch (position) {
            case 1:
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

                FoodTemplateAdapter itemsAdapter = new FoodTemplateAdapter(context, foodTemplates);
                gridView = (GridView)context.findViewById(R.id.food_template_view);
                gridView.setNumColumns(2);
                gridView.setAdapter(itemsAdapter);

        }
*/
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}