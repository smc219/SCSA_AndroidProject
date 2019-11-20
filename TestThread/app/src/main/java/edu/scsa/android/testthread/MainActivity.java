package edu.scsa.android.testthread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView t1;
    TextView t2;
    int btnumMain = 1;
    int btnumBack = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = findViewById(R.id.mainTv);
        t2 = findViewById(R.id.backTv);

        t2.setText("Back : " + btnumBack);
        t1.setText("Main : " + btnumMain);

        new MyNetworkThread().start();

    }

    public void addNum(View view) {
        t1.setText("Main : " + (++btnumMain));

    }

    class MyNetworkThread extends Thread {
        // 1부터 10까지 1초마다 +1씩 증가시키면서 해당 값을 텍스트 뷰에 반영




        @Override
        public void run() {

            super.run();
            while(btnumBack < 12220) {
                final String sendData = "backT : " + ++btnumBack;
                Message msg = myH.obtainMessage(1, sendData); // what은 request Code같은 것
                myH.sendMessage(msg);
//                myH.post(new Runnable() {
//                    final String data = sendData;
//                    @Override
//                    public void run() { // 이렇게 실행하면 Main Thread에서 수행
//                        Log.i("INFO", "in run" + Thread.currentThread());
//                        t2.setText(data);
//                    }
//                });
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        t2.setText(sendData);
//                    }
//                });
                try {
                    sleep(600);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler myH = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String sendD = msg.obj.toString();
            t2.setText(sendD);
        }
    };




}
