package edu.scsa.android.testnetasyncapp;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText urlEt;
    TextView disTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlEt = findViewById(R.id.urlEt);
        disTv = findViewById(R.id.displayTv);
    }

    public void search(View view) {
        String url = urlEt.getText().toString();
        urlEt.setText("");
       new DataDownTask().execute(url);
        Toast.makeText(this, url + " search start...", Toast.LENGTH_SHORT).show();

    }

    class DataDownTask extends AsyncTask <String, String, String>{

        InputStream inputS;

        @Override
        protected String doInBackground(String... url) {
            StringBuilder sb = new StringBuilder();
            String str;
            try {
                URL myUrl = new URL(url[0]);
                HttpURLConnection httpUrlCon = (HttpURLConnection) myUrl.openConnection();
               inputS = httpUrlCon.getInputStream();
               BufferedReader br = new BufferedReader(new InputStreamReader(inputS));
               while ((str = br.readLine()) != null){
//                   publishProgress(str);
                   sb.append(str);
               };
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString(); // 메모리 상에서 작업이 일어나기 떄문에 성능적인 측면에서 훨씬 좋음.
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
                disTv.append(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            disTv.setText(s);
            Toast.makeText(MainActivity.this, "upload Fin", Toast.LENGTH_SHORT).show();
        }
    }

}
