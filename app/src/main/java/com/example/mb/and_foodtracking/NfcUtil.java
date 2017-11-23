package com.example.mb.and_foodtracking;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class NfcUtil {
    private static final String EMPTY_TAG_STRING = "This is an empty food tag";
    private MainActivity context;

    public NfcUtil(MainActivity context) {
        this.context = context;
    }

    public void setNewDate(Tag tag, String tagid, int foodid, int regYear, int regMonth, int regDate, int expYear, int expMonth, int expDate) {
        try {
            // write or overwrite the content on the NFC tag
            writeToNFC(
                    "Tag ID: "+ tagid +
                            "\nFood ID: " + foodid +
                            "\nRegistry: " + regYear + "/" + regMonth + "/" + regDate +
                            "\nExpiry: " + expYear + "/" + expMonth + "/" + expDate, tag);
            Toast.makeText(context,"Successfully wrote tag", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"Failed to write tag", Toast.LENGTH_LONG).show();
        }
    }

    public boolean eraseTag(Tag tag) {
        try {
            // write or overwrite the content on the NFC tag
            writeToNFC(EMPTY_TAG_STRING, tag);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String readNFC(Intent intent) {
        StringBuilder res = new StringBuilder();
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
                            res.append(tagText);
                        } catch (UnsupportedEncodingException e) {
                            res.append("Error reading NFC...");
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            res.append("Empty NFC Tag discovered");
        }
        return res.toString();
    }

    private void writeToNFC(String text, Tag tag) throws IOException, FormatException {
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
