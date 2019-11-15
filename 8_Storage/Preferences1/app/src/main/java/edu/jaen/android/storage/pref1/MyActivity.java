package edu.jaen.android.storage.pref1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.text1);

		//SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		// get
		// 저장된 정보를 읽어온다.
		SharedPreferences prefs1 = getSharedPreferences("myData", MODE_PRIVATE);
		// 데이터 공유를 하고 싶을때 여기저기서 읽고 싶다면 이런식으로 바꿔준면 된다
		int v = prefs1.getInt("key", 0);
		// 이 코드는 resume에 사실 많이 씀(위의 코드). key값을가져오고그 값이 없으면 defValue 가져옴

		// SharedPrefenrence 안애는 Editor가 있다. 그리고 그 editor를 통해서 값을 추가하고
		//.commit을 통해서 끝마친다.
 		SharedPreferences.Editor editor = prefs1.edit();
		editor.putInt("key", ++v);
		// editor.putInt("key", ++v);
		editor.commit();

		tv.setText("Preference Test: " + v);

		Button b = (Button) findViewById(R.id.next);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MyActivity.this, NextActivity.class);
				startActivity(i);
			}
		});
	}
}