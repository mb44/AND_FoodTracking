package com.example.mb.and_foodtracking;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static android.R.attr.id;
import static com.example.mb.and_foodtracking.R.id.foodTemplateView;

public class RegisterFoodActivity extends AppCompatActivity {
    private ArrayList<FoodTemplate>foodTemplates;
    private ArrayAdapter<FoodTemplate> arrayAdapter;
    private GridView gridView;
    private TextView writeTagTextView;
    private String  uniqueID;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_food);

        writeTagTextView = (TextView)findViewById(R.id.writeTagTextView);
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


        FoodTemplateAdapter itemsAdapter = new FoodTemplateAdapter(this, foodTemplates);

        gridView = (GridView)findViewById(foodTemplateView);
        gridView.setNumColumns(2);
        gridView.setAdapter(itemsAdapter);

        // 4. Set listeners
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long postition) {
                FoodTemplate ft = (FoodTemplate)parent.getItemAtPosition(position);
                //Toast.makeText(RegisterFoodActivity.this, "Food name: " + ft.getName(), Toast.LENGTH_SHORT).show();
                writeTagTextView.setText("Write " + ft.getName() + " tag...");
                writeTagTextView.setVisibility(View.VISIBLE);

                // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
                SharedPreferences settings = getSharedPreferences("pref_file", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                writeTagTextView.setText("Approach NFC");
                editor.putBoolean("isWriting", true);
                // Commit the edits!
                editor.commit();
            }
        } );
    }

    private void setNewDate(Tag tag)
    {
        try {
            writeToNFC("The current date and time is:\n " + currentDateString()+ "\n unique ID: "+generateUniqueId()+"\n", tag);  // whrite or overwrite the content on the NFC tag
            Toast.makeText(RegisterFoodActivity.this,"write successful", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RegisterFoodActivity.this,"Fail to write", Toast.LENGTH_LONG).show();
        }
        //isWriting = false;
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
}
