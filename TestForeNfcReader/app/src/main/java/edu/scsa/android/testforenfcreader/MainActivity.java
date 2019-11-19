package edu.scsa.android.testforenfcreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pIndent;
    IntentFilter[] filters;
    TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this); // 본인이 장비를 제어하기 떄문
        infoTv = findViewById(R.id.infoTv);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC 지원 안함", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 1. 프레임워크에서 전달된 인텐트를 처리할 액티비티를 호출할 인텐트 생성
        Intent i = new Intent(this, this.getClass()); //getClass -> 자기자신을 호출하는 것. reflectional API 라고 한다고...
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 2. 인텐트를 처리할 펜딩인텐트 생성.
        pIndent = PendingIntent.getActivity(this, 0, i, 0);

        // 3. 인텐트 필터 생성
//        IntentFilter ndefFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        IntentFilter ndefFilter2 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        IntentFilter ndefFilter3 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        filters = new IntentFilter[]{ndefFilter, ndefFilter2, ndefFilter3};
//        try {
////            ndefFilter.addDataType("text/plain");
////            ndefFilter2.addDataScheme("https");
////            ndefFilter3.addDataScheme("http");
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }


        IntentFilter tagFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters = new IntentFilter[]{tagFilter};


    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pIndent, filters, null);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.i("INFO", "On New Intent Call...");
        String action = intent.getAction();
        processIntent(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
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
            infoTv.append(sDat);

        }
        else if (strT.equals("U")) {
            Log.i("INFO", "type for Uri : " + strT);
            Uri recUri = textR.toUri();
            //infoTv.append(recUri.toString());
            infoTv.setText(recUri.toString());
//            Intent i2 = new Intent(Intent.ACTION_VIEW, recUri);
//            startActivity(i2);
        }






    }
}
