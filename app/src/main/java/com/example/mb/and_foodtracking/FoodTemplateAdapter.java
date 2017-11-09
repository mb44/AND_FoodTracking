package com.example.mb.and_foodtracking;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.app.Activity;
        import android.support.annotation.NonNull;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;


/**
 * Created by Mb on 07-11-2017.
 */

public class FoodTemplateAdapter extends ArrayAdapter<FoodTemplate>
{
    public FoodTemplateAdapter(Activity context, ArrayList<FoodTemplate>food) {
        super(context, 0, food);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView = convertView;

        if (gridItemView == null) {
            gridItemView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_item, parent, false);
        }

        FoodTemplate currentFood = getItem(position);

        TextView textView = (TextView)gridItemView.findViewById(R.id.item_text);
        textView.setText(currentFood.getName() + "\n");

        int imgResourceId = currentFood.getImgResourceId();

        if (imgResourceId != -1) {
            ImageView imgView = (ImageView)gridItemView.findViewById(R.id.item_icon);
            imgView.setImageResource(currentFood.getImgResourceId());
        }

        return gridItemView;
    }
}
