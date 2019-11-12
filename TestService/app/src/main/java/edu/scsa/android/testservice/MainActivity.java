package edu.scsa.android.testservice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSev(View view) {
        i = new Intent(this, MyService.class);
        i.putExtra("url", "https://www.daum.net");
        startService(i);
    }

    public void stopSev(View view) {

        if (i != null) {
            stopService(i);
        }
    }
}
