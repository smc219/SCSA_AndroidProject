package edu.jaen.android.storage.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyFileOutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView t = (TextView) findViewById(R.id.text1);
		BufferedWriter br = null;
		try {
			//openFileOutput -> 안드로이드에서 나타나는 차이점.
			br = new BufferedWriter(new OutputStreamWriter(openFileOutput(
					"data.txt", MODE_PRIVATE)));
			br.append("안녕하세요.");
			br.append("반갑습니다."); // 그냥 문자열을 쓸 수 있다는게 buffered의 장점
			t.setText("파일이 정상적으로 생성되었습니다.");

			//여기서 그냥 flush하고 해도 됨.



		} catch (IOException e) {
			Log.e("IO", "File Output Error");
			t.setText("파일이 생성시 오류가 발생했습니다.");
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}




		}
		BufferedReader b = null;
		t.setText("");
		try {
			b = new BufferedReader(new InputStreamReader(openFileInput("data.txt")));
			String str = "";
			while ((str = b.readLine()) != null) t.append(str);
		} catch (Exception e) {
			e.printStackTrace();
		}



	}
}