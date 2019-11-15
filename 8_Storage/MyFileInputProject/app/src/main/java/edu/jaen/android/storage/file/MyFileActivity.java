package edu.jaen.android.storage.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MyFileActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView t = (TextView) findViewById(R.id.text1);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					openFileInput("data.txt")));
			String msg = br.readLine();
			while (msg != null) {
				t.append(msg + "\n");
				msg = br.readLine();
			}
		} catch (IOException e) {
			Log.e("IO", "File Input Error");
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}