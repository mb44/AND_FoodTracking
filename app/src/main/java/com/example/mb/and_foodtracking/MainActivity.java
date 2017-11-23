package com.example.mb.and_foodtracking;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mb.and_foodtracking.model.FoodDate;
import com.example.mb.and_foodtracking.model.FoodItem;
import com.example.mb.and_foodtracking.model.FoodType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private NfcUtil nfcUtil;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListArray;
    private boolean isWriting;
    private boolean isClearing;
    private TextView statusTextView;
    private static final String EMPTY_TAG_STRING = "This is an empty food tag";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private FirebaseDatabase database;
    private DatabaseReference dbRefStorage;

    private ValueEventListener storageEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Starting (thisisdebug)");
        setContentView(R.layout.activity_main);
        initUserInterface();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        settings = getSharedPreferences(getString(R.string.settings_filename), MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(getString(R.string.settings_isWriting), false);
        editor.putBoolean(getString(R.string.settings_isClearing), false);
        editor.commit();

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            ndef.addDataType("*/*");
            //    ndef2.addDataType("MIME_TEXT_PLAIN");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef, /*ndef2*/ };
        techListArray = new String[][] {new String[]{MifareUltralight.class.getName()}};
    }

    private void  setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter.addFragment(new Tab1Fragment(), "Menu");
        mSectionsPageAdapter.addFragment(new Tab2Fragment(), "Register food");
        mSectionsPageAdapter.addFragment(new Tab3Fragment(), "Unregister food");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    private void initUserInterface() {
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // set up the ViewPager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        statusTextView = (TextView)findViewById(R.id.statusTextView);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab.getPosition()==2) {
                    editor.putBoolean(getString(R.string.settings_isClearing), true);
                }
                editor.commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"state: onStart (thisisdebug)");
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"state: onResume (thisisdebug)");
        nfcUtil = new NfcUtil(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListArray);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"state: onNewIntent (thisisdebug)");
        initUserInterface();

        // Get the tag
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        SharedPreferences settings = getSharedPreferences(getString(R.string.settings_filename), MODE_PRIVATE);
        isWriting = settings.getBoolean(getString(R.string.settings_isWriting), false);
        isClearing = settings.getBoolean(getString(R.string.settings_isClearing), false);

        if(isWriting && tag != null) {
            // Retrieve user's input
            int foodId = settings.getInt(getString(R.string.settings_foodid), 0);
            int regYear = settings.getInt(getString(R.string.settings_regYear), 1970);
            int regMonth = settings.getInt(getString(R.string.settings_regMonth), 1);
            int regDate = settings.getInt(getString(R.string.settings_regDate), 1);
            int expYear = settings.getInt(getString(R.string.settings_expYear), 1970);
            int expMonth = settings.getInt(getString(R.string.settings_expMonth), 1);
            int expDate = settings.getInt(getString(R.string.settings_expDate), 1);

            // Send to Firebase
            database = FirebaseDatabase.getInstance();
            dbRefStorage = database.getReference().child("Storage");
            FoodItem foodItem = new FoodItem(foodId, new FoodDate(regYear, regMonth, regDate), new FoodDate(expYear, expMonth, expDate));

            // Create new push key (unique id) and set foodItem as its value
            DatabaseReference tagId = dbRefStorage.push();
            tagId.setValue(foodItem);

            // Update Tag (including the tagId retrieved from Firebase)
            nfcUtil.setNewTagId(tag, tagId.getKey());

            // Update status TextView
            statusTextView.setText("Food id: " + foodId +
                    "\nRegistry date: " + regYear+"/"+regMonth+"/"+regDate+
                    "\nExpiry date: " + expYear+"/"+expMonth+"/"+expDate);
        } else if(isClearing && tag != null) {
            String tagText = nfcUtil.readNFC(intent);
            statusTextView.setText(tagText);
            int tagStart = tagText.indexOf("Tag ID: ") + 8;
            // The tag id ends just before a newline

            String tagString = null;
            // Remove foodItem from Firebase
            if (tagStart > 0) {
                tagString = tagText.substring(tagStart);

                dbRefStorage.child(tagString).removeValue();
            }
            // Erase the tag
            boolean success = nfcUtil.eraseTag(tag);
            if (success) {
                statusTextView.setText("");
                Toast.makeText(this,"Successfully erased tag", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"Failed to erase tag", Toast.LENGTH_LONG).show();
            }
        } else {
            final String tagId = nfcUtil.readNFC(intent).substring(8);

            database = FirebaseDatabase.getInstance();
            dbRefStorage = database.getReference().child("Storage");
            storageEventListener =
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot ds) {
                            // Retrieve and search FoodItems
                            for (DataSnapshot foodItem : ds.getChildren()) {
                                // We found it!
                                if (foodItem.getKey().trim().equals(tagId.trim())) {
                                    FoodItem item = foodItem.getValue(FoodItem.class);
                                    FoodDate registry = item.getRegistry();
                                    FoodDate expiry = item.getExpiry();

                                    statusTextView.setText("Food id: " + item.getFoodid() +
                                            "\nRegistry date: " + registry.getYear() + "/" + registry.getMonth() + "/" + registry.getDate() +
                                            "\nExpiry date: " + expiry.getYear() + "/" + expiry.getMonth() + "/" + expiry.getDate());
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };

            dbRefStorage.addValueEventListener(storageEventListener);
        }
        // Clear settings
        editor = settings.edit();
        editor.putBoolean(getString(R.string.settings_isWriting), false);
        editor.putBoolean(getString(R.string.settings_isClearing), false);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"state: onPause (thisisdebug)");
        if (storageEventListener != null) {
            dbRefStorage.removeEventListener(storageEventListener);
        }
        nfcAdapter.disableForegroundDispatch(this);
    }
}
