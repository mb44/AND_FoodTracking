package com.example.mb.and_foodtracking;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import static android.graphics.PorterDuff.Mode.CLEAR;
import static android.os.Build.VERSION_CODES.M;
import static com.example.mb.and_foodtracking.R.id.statusTextView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListArray;

    private boolean isWriting;
    private boolean isClearing;

    private TextView statusTextView;
    private Tag detectedTag;
    private static final String EMPTY_TAG_STRING = "This is an empty food tag";
    private String uniqueID;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Starting.");
        setContentView(R.layout.activity_main);
        initUserInterface();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        settings = getSharedPreferences(getString(R.string.settings_filename), MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(getString(R.string.settings_isWriting), false);
        editor.putBoolean(getString(R.string.settings_isReading), false);
        editor.putBoolean(getString(R.string.settings_isClearing), false);
        editor.commit();

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        detectedTag = getIntent().getParcelableExtra((NfcAdapter.EXTRA_TAG));

        try {
            ndef.addDataType("*/*");
            //    ndef2.addDataType("MIME_TEXT_PLAIN");
        }catch (IntentFilter.MalformedMimeTypeException e){
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef, /*ndef2*/ };
        techListArray = new String[][] {new String[]{MifareUltralight.class.getName()}};

        detectedTag = getIntent().getParcelableExtra((NfcAdapter.EXTRA_TAG));
    }

    private void  setupViewPager(ViewPager viewPager)
    {
        mSectionsPageAdapter.addFragment(new Tab1Fragment(), "Menu");
        mSectionsPageAdapter.addFragment(new Tab2Fragment(), "Register food");
        mSectionsPageAdapter.addFragment(new Tab3Fragment(), "Unregister food");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    private void initUserInterface()
    {
        //mainTextView = (TextView) findViewById(R.id.textTab1);
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
                } /*else {
                    editor.putBoolean(getString(R.string.settings_isClearing), false);
                }*/
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
        Log.d(TAG,"state: onStart");
        //readNFC(getIntent());                    // read the content on the NFC tag
        //readNFC(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"state: onPause");
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"state: onResume");
        //Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListArray);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"state: onNewIntent");
        initUserInterface();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Toast.makeText(this,"On new intent", Toast.LENGTH_SHORT).show();

        SharedPreferences settings = getSharedPreferences(getString(R.string.settings_filename), MODE_PRIVATE);
        isWriting = settings.getBoolean(getString(R.string.settings_isWriting), false);
        isClearing = settings.getBoolean(getString(R.string.settings_isClearing), false);

        //Toast.makeText(this,"isClearing: " + isClearing, Toast.LENGTH_SHORT).show();

        if(isWriting && intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) != null) {
            int year = settings.getInt(getString(R.string.settings_expYear), 1970);
            int month = settings.getInt(getString(R.string.settings_expMonth), 1);
            int date = settings.getInt(getString(R.string.settings_expDate), 1);
            //Toast.makeText(MainActivity.this, "" +date, Toast.LENGTH_LONG).show();
            setNewDate(tag, year, month, date);
        } else if(isClearing && intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) != null) {
            eraseTag(tag);
        }
        readNFC(intent);
        editor = settings.edit();
        editor.putBoolean(getString(R.string.settings_isWriting), false);
        editor.putBoolean(getString(R.string.settings_isReading), false);
        editor.putBoolean(getString(R.string.settings_isClearing), false);
        editor.commit();
    }

    private void setNewDate(Tag tag, int year, int month, int date) {
        try {
            // write or overwrite the content on the NFC tag
            writeToNFC("Expiry date: " + year + "/" + month + "/" + date + "\nTag ID: "+generateUniqueId()+"\n", tag);
            Toast.makeText(MainActivity.this,"Successfully wrote tag", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Failed to write tag", Toast.LENGTH_LONG).show();
        }
    }

    private void eraseTag(Tag tag)
    {
        try {
            // write or overwrite the content on the NFC tag
            writeToNFC(EMPTY_TAG_STRING, tag);
            Toast.makeText(MainActivity.this,"Successfully erased tag", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Failed to erase tag", Toast.LENGTH_LONG).show();
        }
    }

    public void readNFC(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            for (Parcelable rawMessage : rawMessages) {
                NdefMessage message = (NdefMessage) rawMessage;
                NdefRecord[] records = message.getRecords();

                for (NdefRecord record : records) {
                    if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
                        byte[] payload = record.getPayload();
                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                        int languageCodeLength = payload[0] & 0063;
                        try {
                            String tagText = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                            statusTextView.setText(tagText);
                            //Toast.makeText(this, tagText, Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            //mainTextView.setText("Empty NFCtag is discovered");
            statusTextView.setText("Empty NFC Tag discovered");
        }
    }

    public void writeToNFC(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }

    private NdefRecord createRecord(String text)throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        System.arraycopy(langBytes,0,payload,1,langLength);
        System.arraycopy(textBytes,0,payload,1 + langLength, textLength);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT,new byte[0],payload);
    }

    private String currentDateString(){
        String currentDate = Calendar.getInstance().getTime().toString();
        return currentDate;
    }

    private String generateUniqueId()
    {
        return uniqueID = UUID.randomUUID().toString();// unique id generator
    }

    public void clearTag (View v)
    {
        isClearing = true;
        isWriting = false;
        Toast.makeText(MainActivity.this,"The clear tag is clicked",Toast.LENGTH_LONG).show();

        Log.d(TAG,"This is the clear tag");
    }

    /*
    public void addDate (View v)
    {
        isWriting = true;
        isClearing = false;
        Toast.makeText(MainActivity.this,"The add date tag is clicked",Toast.LENGTH_LONG).show();
        Log.d(TAG, currentDateString());
    }
    */
}
