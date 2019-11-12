package edu.scsa.android.testreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    IntentFilter filter;
    MyReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("edu.scsa.action.Connection_Fail");
        myReceiver = new MyReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // BR을 등록하는 코드
        registerReceiver(myReceiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // BR을 해지하는 코드
        unregisterReceiver(myReceiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 단순히 코드의 길이가 줄어드는 것뿐 아니라 Outer 클래스의 리소스를 쓰는 것이 가능.
            // startActivity(i);
            String action = intent.getAction();
            if (action.equals("edu.scsa.action.Connection_Fail")) {

            } else {

            }
        }
    };
}
