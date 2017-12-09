package com.example.mb.and_foodtracking.arrayadapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mb.and_foodtracking.R;
import com.example.mb.and_foodtracking.model.FoodDate;
import com.example.mb.and_foodtracking.model.FoodItem;

import java.util.ArrayList;

public class FoodItemAdapter extends ArrayAdapter<FoodItem> {
    static class ViewHolder {
        TextView tagIdTextView;
        TextView nameTextView;
        TextView regTextView;
        TextView expTextView;
    }

    public FoodItemAdapter(Activity context, ArrayList<FoodItem>foodItems) {
        super(context, 0, foodItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
            // Instantiate ViewHolder
            viewHolder = new ViewHolder();
            // Set tag to use viewHolder later
            convertView.setTag(viewHolder);

            viewHolder.nameTextView = convertView.findViewById(R.id.food_name);
            viewHolder.regTextView = convertView.findViewById(R.id.food_regdate);
            viewHolder.expTextView =convertView.findViewById(R.id.food_expdate);
        } else {
            // Use the instantiated ViewHolder if the View is already inflatedd
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }

        FoodItem currentItem = getItem(position);

        if (currentItem != null) {
            viewHolder.nameTextView.setText("Name:\n" + currentItem.getName());

            FoodDate regDate = currentItem.getRegistry();
            viewHolder.regTextView.setText("Registry:\n" + regDate.getYear()+"/"+regDate.getMonth()+"/"+regDate.getDate() );

            FoodDate expDate = currentItem.getExpiry();
            viewHolder.expTextView.setText("Expiry:\n" + expDate.getYear()+"/"+expDate.getMonth()+"/"+expDate.getDate() );
        }
        return convertView;
    }
}
