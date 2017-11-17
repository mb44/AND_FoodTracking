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
    public FoodItemAdapter(Activity context, ArrayList<FoodItem>foodItems) {
        super(context, 0, foodItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }

        FoodItem currentFoodItem = getItem(position);

        TextView textViewId = (TextView)listItemView.findViewById(R.id.tag_id);
        textViewId.setText("Tag id:\n" +currentFoodItem.getTagId());

        TextView textViewName = (TextView)listItemView.findViewById(R.id.food_name);
        textViewName.setText("Name:\n" + currentFoodItem.getName());

        TextView textViewReg = (TextView)listItemView.findViewById(R.id.food_regdate);
        FoodDate regDate = currentFoodItem.getRegistry();
        textViewReg.setText("Registry:\n" + regDate.getYear()+"/"+regDate.getMonth()+"/"+regDate.getDate() );

        TextView textViewExp= (TextView)listItemView.findViewById(R.id.food_expdate);
        FoodDate expDate = currentFoodItem.getExpiry();
        textViewExp.setText("Expiry: " + expDate.getYear()+"/"+expDate.getMonth()+"/"+expDate.getDate() );

        /*
        int imgResourceId = currentFoodItem.getImgResourceId();

        if (imgResourceId != -1) {
            ImageView imgView = (ImageView)listItemView.findViewById(R.id.item_icon);
            imgView.setImageResource(currentFoodItem.getImgResourceId());
        }
*/
        return listItemView;
    }
}
