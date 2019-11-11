package edu.scsa.android.firstapp;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 여기서 on이 붙는건 안드로이드가 실행. 자바로 얘기하면 constructor.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 레이아웃에 있는 main을 초기화시키라는 것. 레이아웃이 자바에서의 클래스 이름. android는 함수 이름에 매핑됨.
    }

    public void a1Call(View view) {
        Toast.makeText(this, "a1 Call...", Toast.LENGTH_SHORT).show();
        //같은 프로세스(같은 앱)에서 다른 컴포넌트를 호출.
        Intent i = new Intent(this, A1_Activity.class);

        i.putExtra("path", "mySDcard/myData.dat");

        startActivity(i); // 원하는 특정 컴포넌트를 호출.
    }

    public void a2Call(View view) {
        // 다른 프로세스에 존재하는 컴포넌트를 묵시적으로 호출
        Intent i2 = new Intent("com.sm.action.EDIT");
        startActivity(i2);

    }

    public void a3Call(View view) {
        // 묵시적 방법 : 이미 정해져있는 액션 호출.
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.com"));
        Intent i2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:12345678"));
        Intent i3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 장비제어까지 가능.
        Intent i4 = new Intent(Intent.ACTION_PICK);

        startActivity(i3);

    }
}
