package com.example.mb.and_foodtracking;

import android.animation.Animator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.mb.and_foodtracking.arrayadapters.FoodItemAdapter;
import com.example.mb.and_foodtracking.model.FoodDate;
import com.example.mb.and_foodtracking.model.FoodItem;
import com.example.mb.and_foodtracking.model.FoodType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private MainActivity context;
    private RelativeLayout frontPageLayout;
    private RelativeLayout foodStatusLayout;
    private Button showfoodStatusButton;
    private Button closeFoodStatusButton;
    private Spinner sortSpinner;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private ArrayList<FoodItem> foodItems;
    private ArrayList<FoodType> foodTypes;
    private FoodItemAdapter foodItemAdapter;

    private int sceneAnimationDuration;

    private NotificationCompat.Builder notification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        // Get MainActivity context
        context = (MainActivity) getActivity();
        // Get references to layouts
        frontPageLayout = (RelativeLayout)view.findViewById(R.id.frontpageLayout);
        foodStatusLayout = (RelativeLayout)view.findViewById(R.id.foodStatusLayout);

        showfoodStatusButton = (Button)view.findViewById(R.id.showFoodStatusButton);
        showfoodStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crossFadeLayouts(frontPageLayout, foodStatusLayout);
                //foodStatusLayout.setVisibility(View.VISIBLE);
            }
        });

        closeFoodStatusButton = (Button)view.findViewById(R.id.closeFoodStatusButton);
        closeFoodStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crossFadeLayouts(foodStatusLayout, frontPageLayout);
                //foodStatusLayout.setVisibility(View.GONE);
            }
        });

        // Add sort functionality
        sortSpinner = (Spinner)view.findViewById(R.id.sortSpinner);
        sortSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String text = (String) parent.getItemAtPosition(position);

                // Sort ListView items based on...
                switch (text) {
                    case "Expiry date":
                        Collections.sort(foodItems, new Comparator<FoodItem>() {
                            @Override
                            public int compare(FoodItem foodItem1, FoodItem foodItem2)
                            {
                                if (foodItem1.getExpiry().getYear() < foodItem2.getExpiry().getYear()) {
                                    return -1;
                                } else if (foodItem1.getExpiry().getYear() > foodItem2.getExpiry().getYear()) {
                                    return 1;
                                } else if (foodItem1.getExpiry().getMonth() < foodItem2.getExpiry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getExpiry().getMonth() > foodItem2.getExpiry().getMonth()) {
                                    return 1;
                                } else if (foodItem1.getExpiry().getMonth() < foodItem2.getExpiry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getExpiry().getDate() > foodItem2.getExpiry().getDate()) {
                                    return 1;
                                } else if (foodItem1.getExpiry().getDate() < foodItem2.getExpiry().getDate()) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case "Registry date":
                        Collections.sort(foodItems, new Comparator<FoodItem>() {
                            @Override
                            public int compare(FoodItem foodItem1, FoodItem foodItem2)
                            {
                                if (foodItem1.getRegistry().getYear() < foodItem2.getRegistry().getYear()) {
                                    return -1;
                                } else if (foodItem1.getRegistry().getYear() > foodItem2.getRegistry().getYear()) {
                                    return 1;
                                } else if (foodItem1.getRegistry().getMonth() < foodItem2.getRegistry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getRegistry().getMonth() > foodItem2.getRegistry().getMonth()) {
                                    return 1;
                                } else if (foodItem1.getRegistry().getMonth() < foodItem2.getRegistry().getMonth()) {
                                    return -1;
                                } else if (foodItem1.getRegistry().getDate() > foodItem2.getRegistry().getDate()) {
                                    return 1;
                                } else if (foodItem1.getRegistry().getDate() < foodItem2.getRegistry().getDate()) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                        break;
                    case "Food type":
                        Collections.sort(foodItems, new Comparator<FoodItem>() {
                            @Override
                            public int compare(FoodItem foodItem1, FoodItem foodItem2)
                            {
                               String foodName1 = foodTypes.get( foodItem1.getFoodid()).getName();
                               String foodName2 = foodTypes.get( foodItem2.getFoodid()).getName();
                               return foodName1.compareTo(foodName2);
                            }
                        });
                        break;
                    default:
                        break;
                }
                // Update the adapter
                foodItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Get reference to ListView
        ListView listView = (ListView)view.findViewById(R.id.foodListView);
        // FoodItem, ArrayAdapter etc
        foodItems = new ArrayList<FoodItem>();
        foodItemAdapter = new FoodItemAdapter(context, foodItems);
        listView.setAdapter(foodItemAdapter);

        // Database
        database = FirebaseDatabase.getInstance();

        foodTypes = new ArrayList<>();
        dbRef = database.getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                foodTypes.clear();
                foodItems.clear();

                DataSnapshot types = ds.child("FoodTypes");
                DataSnapshot storage = ds.child("Storage");

                // Retrieve and populate FoodTypes ArrayList
                for (DataSnapshot type : types.getChildren()) {
                    FoodType item = type.getValue(FoodType.class);
                    foodTypes.add(item);
                }
                // Retrieve and populate FoodItems ArrayList
                for (DataSnapshot foodItem : storage.getChildren()) {
                    FoodItem item = foodItem.getValue(FoodItem.class);
                    item.setTagId(foodItem.getKey());
                    item.setName( foodTypes.get( item.getFoodid()).getName());
                    foodItems.add(item);
                }

                // Notify users if food expire soon
                notifyExpiry();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        sceneAnimationDuration = context.getResources().getInteger(android.R.integer.config_longAnimTime);

        return view;
    }

    private void crossFadeLayouts(final RelativeLayout src, final RelativeLayout dest) {
        dest.setAlpha(0);
        dest.setVisibility(View.VISIBLE);

        dest.animate().alpha(1f).setDuration(sceneAnimationDuration).setListener(null);

        src.animate().alpha(0f).setDuration(sceneAnimationDuration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                src.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
    }

    private long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    private void notifyExpiry() {
        final Calendar now = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        for (FoodItem food : foodItems) {
            FoodDate exp = food.getExpiry();
            int expYear = exp.getYear();
            int expMonth = exp.getMonth();
            int expDate = exp.getDate();
            end.set(expYear, expMonth-1, expDate);

            long daysDiff = daysBetween(now, end);

            if (daysDiff >= 0 && daysDiff <= 1) {
                // Setup notifications
                notification = new NotificationCompat.Builder(context);
                notification.setAutoCancel(true);
                // Builds notifications and issues it
                NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                notification.setSmallIcon(R.drawable.cow);
                notification.setTicker("FoodTrack message");
                notification.setWhen(System.currentTimeMillis());


                if (daysDiff < 0) {
                    notification.setContentTitle("Alert:  " + food.getName()+ " has expired");
                } else if (daysDiff==0) {
                    notification.setContentTitle("Alert:  " + food.getName()+ " expire(s) today");
                } else if (daysDiff==1) {
                    notification.setContentTitle("Alert:  " + food.getName()+ " expire(s) tomorrow");
                }

                notification.setContentText("Tag ID: " + food.getTagId());

                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);

                int uniqueID = UUID.randomUUID().hashCode();
                nm.notify(uniqueID, notification.build());
            }
        }
    }
}
