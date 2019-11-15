package edu.scsa.android.firstapp;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int curPage;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) { // 여기서 on이 붙는건 안드로이드가 실행. 자바로 얘기하면 constructor.
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main); // 레이아웃에 있는 main을 초기화시키라는 것. 레이아웃이 자바에서의 클래스 이름. android는 함수 이름에 매핑됨.
//        Toast.makeText(this, "Main onCreate", Toast.LENGTH_SHORT).show();
//        Log.e("INFO", "Main onCreate");
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        Toast.makeText(this, "Main onStart", Toast.LENGTH_SHORT).show();
//        Log.e("INFO", "Main Start");
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "Main onResume", Toast.LENGTH_SHORT).show();
        Log.e("INFO", "Main Resume");
        curPage = getSharedPreferences("myData", MODE_PRIVATE).getInt("page", 0);
        Toast.makeText(this, "curPage : " + curPage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences("myData", MODE_PRIVATE).edit().putInt("page", ++curPage).commit();
        Toast.makeText(this, "Page 정보를 저장했습니다...", Toast.LENGTH_SHORT).show();
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
////        Toast.makeText(this, "Main onStop", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Toast.makeText(this, "Main onCreate", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(this, "Main onDestroy", Toast.LENGTH_SHORT).show();
//    }
//
    public void a1Call(View view) {
        Toast.makeText(this, "a1 Call...", Toast.LENGTH_SHORT).show();
        //같은 프로세스(같은 앱)에서 다른 컴포넌트를 호출.
        Intent i = new Intent(this, A1_Activity.class);

        i.putExtra("path", "mySDcard/myData.dat");

        startActivity(i); // 원하는 특정 컴포넌트를 호출.
    }

    public void a2Call(View view) {
        // 다른 프로세스에 존재하는 컴포넌트를 묵시적으로 호출
        //Intent i2 = new Intent("com.sm.action.EDIT");
        // 다른 프로세스에 존재하는 컴포넌트를 명시적으로 호출
        ComponentName cpName = new ComponentName("edu.scsa.android.secondapp", "edu.scsa.android.secondapp.TestActivity");
        //  second app쪽에서
        Intent i2 = new Intent();
        i2.setComponent(cpName);
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
