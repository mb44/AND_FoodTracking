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
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static android.R.attr.tag;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.nfc.NfcAdapter.ACTION_TECH_DISCOVERED;
import static android.os.Build.VERSION_CODES.N;
import static com.example.mb.and_foodtracking.R.id.writeTagTextView;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListArray;
    private Boolean isWriting = false;
    private Boolean isClearing = false;

    private Intent intent;
    private TextView mainTextView;
    private Tag detectedTag;
    private static final String TAG = "This_is_debug";
    private static final String EMPTY_TAG_STRING = "This is an empty food tag";
    private String  uniqueID;
    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        //IntentFilter ndef2 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        //nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef, /*ndef2*/ };
        techListArray = new String[][] {new String[]{MifareUltralight.class.getName()}};

        detectedTag = getIntent().getParcelableExtra((NfcAdapter.EXTRA_TAG));

        final Button registerFoodButton = (Button)findViewById(R.id.registerFoodButton);
        registerFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerFoodIntent = new Intent(MainActivity.this, RegisterFoodActivity.class);
                startActivity(registerFoodIntent);
            }
        });

        Button unregisterFoodButton = (Button)findViewById(R.id.unregisterFoodButton);
        unregisterFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent unregisterFoodIntent = new Intent(MainActivity.this, UnregisterFoodActivity.class);

                startActivity(unregisterFoodIntent);
            }
        });

        Button statusButton = (Button)findViewById(R.id.statusButton);
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statusIntent = new Intent(MainActivity.this, StatusActivity.class);

                startActivity(statusIntent);
            }
        });

        SharedPreferences settings = getSharedPreferences("pref_file", MODE_PRIVATE);
        isWriting = settings.getBoolean("isWriting", false);
        if (isWriting) {
            setNewDate();
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean("isWriting", false);
            // Commit the edits!
            editor.commit();
            Toast.makeText(MainActivity.this,"Wrote tag!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"This is the on onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"This is the on Resume");

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListArray);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"This is the on Pause");

        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"This is the onNewIntent");

        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        /*
        if(isWriting && intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) != null) {
            setNewDate();
        } else if(isClearing && intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) != null) {
            eraseTag();
        } else {
            readNFC(intent);
        }
        */
    }

    private void setNewDate()
    {
        try {
            writeToNFC("The current date and time is:\n " +currentDateString()+ "\n unique ID: "+generateUniqueId()+"\n");  // whrite or overwrite the content on the NFC tag
            Toast.makeText(MainActivity.this,"write successful", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Fail to write", Toast.LENGTH_LONG).show();
        }
        isWriting = false;
    }

    private void eraseTag()
    {
        try {
            writeToNFC(EMPTY_TAG_STRING);  // whrite or overwrite the content on the NFC tag
            Toast.makeText(MainActivity.this,"erase successful", Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Fail to erase", Toast.LENGTH_LONG).show();
        }
        isClearing = false;
    }

    public void readNFC(Intent intent) {

        if (ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            mainTextView.setText("Empty NFCtag is discovered");
        }

        else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

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
                            mainTextView.setText(tagText);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void writeToNFC(String text) throws IOException, FormatException{
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

    public void addDate (View v)
    {
        isWriting = true;
        isClearing = false;
        Toast.makeText(MainActivity.this,"The add date tag is clicked",Toast.LENGTH_LONG).show();
        Log.d(TAG, currentDateString());
    }
}
