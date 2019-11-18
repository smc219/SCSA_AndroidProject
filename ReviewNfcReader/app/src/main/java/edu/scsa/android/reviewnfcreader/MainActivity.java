package edu.scsa.android.reviewnfcreader;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView infoTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = findViewById(R.id.infoTv);
        Intent i = getIntent();
        String action = i.getAction();
        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) processInt(i);

    }

    private void processInt (Intent i) {
        Parcelable[] rawData = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawData[0];
        NdefRecord[] rcds = msg.getRecords();
        NdefRecord rcd = rcds[0];
        byte[] recT = rcd.getType();
        String strT = new String(recT);
        if (strT.equals("T")) {
            String setT = new String(rcd.getPayload(), 3, rcd.getPayload().length - 3);
            infoTv.setText(setT);
        }
        else if (strT.equals("U")) {
            Uri u = rcd.toUri();
            infoTv.setText(u.toString());

        }



    }
}

