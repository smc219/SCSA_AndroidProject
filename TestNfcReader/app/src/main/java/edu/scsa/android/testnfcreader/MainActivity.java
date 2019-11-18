package edu.scsa.android.testnfcreader;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView infoTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTv = findViewById(R.id.infoTv);
        Intent recI = getIntent();

        String action = recI.getAction();
        //infoTv.setText("action : " + action);
        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) processIntent(recI);
    }

    private void processIntent(Intent i) {

        Parcelable[] rawData = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage ndefM = (NdefMessage) rawData[0];
        NdefRecord[] recArr = ndefM.getRecords();
        NdefRecord textR = recArr[0];
        byte[] recT = textR.getType();
        String strT = new String(recT);

        if (strT.equals("T")) {
            Log.i("INFO", "type : " + strT);
            byte[] bDat = textR.getPayload();
            String sDat = new String(bDat, 3, bDat.length - 3); // encoding의 찌꺼기 값을 빼기 위함
            infoTv.setText(sDat);

        }
        else if (strT.equals("U")) {
            Log.i("INFO", "type for Uri : " + strT);
            Uri recUri = textR.toUri();
            infoTv.setText(recUri.toString());
            Intent i2 = new Intent(Intent.ACTION_VIEW, recUri);
            startActivity(i2);
        }






    }
}
