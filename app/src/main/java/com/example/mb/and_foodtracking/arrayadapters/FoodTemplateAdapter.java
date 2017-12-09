package com.example.mb.and_foodtracking.arrayadapters;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mb.and_foodtracking.model.FoodTemplate;
import com.example.mb.and_foodtracking.R;

import java.util.ArrayList;

public class FoodTemplateAdapter extends ArrayAdapter<FoodTemplate> {
    static class ViewHolder {
        TextView foodNameTextView;
        ImageView foodImageView;
    }

    public FoodTemplateAdapter(Activity context, ArrayList<FoodTemplate> food) {
        super(context, 0, food);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_item, parent, false);
            // Instantiate ViewHolder
            viewHolder = new FoodTemplateAdapter.ViewHolder();
            // Set tag to use viewHolder later
            convertView.setTag(viewHolder);

            viewHolder.foodNameTextView = (TextView)convertView.findViewById(R.id.item_text);
            viewHolder.foodImageView = (ImageView)convertView.findViewById(R.id.item_icon);
        } else {
            // Use the instantiated ViewHolder if the View is already inflatedd
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FoodTemplate currentItem = getItem(position);

        if (currentItem != null) {
            viewHolder.foodNameTextView.setText(currentItem.getName());

            int imgResourceId = currentItem.getImgResourceId();
            if (imgResourceId != -1) {
                ImageView imgView = (ImageView)convertView.findViewById(R.id.item_icon);
                viewHolder.foodImageView.setImageResource(currentItem.getImgResourceId());
            }
        }

        return convertView;
    }
}
