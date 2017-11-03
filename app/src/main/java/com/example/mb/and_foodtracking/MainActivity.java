package com.example.mb.and_foodtracking;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static android.R.attr.tag;
import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    TextView mainTextView;
    Tag detectedTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTextView =(TextView) findViewById(R.id.mainFoodTackTextView);
        detectedTag = getIntent().getParcelableExtra((NfcAdapter.EXTRA_TAG));
    }

    @Override
    protected void onStart() {
        super.onStart();
        readNFC(getIntent());                    // read the content on the NFC tag
        try {
            writeToNFC("Ny abe", detectedTag);  // whrite or overwrite the content on the NFC tag
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readNFC(Intent intent) {

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
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

    public void writeToNFC(String text, Tag tag) throws IOException, FormatException{
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

}
