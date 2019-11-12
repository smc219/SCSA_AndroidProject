package edu.scsa.android.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class A1_Activity extends AppCompatActivity {
    TextView infoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate 안에는 화면 초기화와 객체 가져오기가 보통 일어남
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);
        // 어떤 id를 등록해놓으면 그 id를 키값으로 삼아서 키와 밸류를 등록해놓는다. UI로 만들어진 리소스는 R.java에 저장.
        infoTv = findViewById(R.id.infoTv); // 객체를 가져오는 것이다. 해당 xml을 파싱해서, 키와 밸류로 나눠서 찾는 것.
        // 일종의 객체 변수명이라고 생각하자.

        Intent recI = getIntent();
        String path = recI.getStringExtra("path");
        
        infoTv.setText("받은 데이터 : " + path);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "a1 onStart", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "a1 onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "a1 onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "a1 onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "a1 onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "a1 onDestroy", Toast.LENGTH_SHORT).show();
    }


    public void mainCall(View view) {
        Toast.makeText(this, "Move to Main...", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        finish();
    }
}
