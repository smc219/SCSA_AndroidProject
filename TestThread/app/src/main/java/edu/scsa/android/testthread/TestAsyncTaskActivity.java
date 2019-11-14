package edu.scsa.android.testthread;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class TestAsyncTaskActivity extends AppCompatActivity {
    TextView backTv;
    TextView frontTv;
    int num = 1;
    int btnumMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backTv = findViewById(R.id.backTv);
        backTv.setText("back : " + num);
        frontTv = findViewById(R.id.mainTv);
        new MyCountAsyncTask().execute(15);



    }
    public void addNum(View view) {
        frontTv.setText("Main : " + (++btnumMain));

    }

    class MyCountAsyncTask extends AsyncTask<Integer, Integer, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            Log.i("INFO", "doin Back ~ " + Thread.currentThread().getName());
            while(num < integers[0]) {
                try {
                    sleep(1000);

                    publishProgress(++num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 넘기는 값이라고 생각하면 위의 진행보고 부분을 이해하기 편함!
            }
            return num;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            backTv.setText("back : "+ values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            backTv.setText("back finish : "+ integer);
            Log.i("INFO", "on Post ~ " + Thread.currentThread().getName());
            Toast.makeText(TestAsyncTaskActivity.this, "작업완료", Toast.LENGTH_SHORT).show();
        }
    }
}
