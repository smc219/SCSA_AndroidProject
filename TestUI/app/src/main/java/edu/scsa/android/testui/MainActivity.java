package edu.scsa.android.testui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button thisB, nestB, annoB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisB = findViewById(R.id.addBut);
        nestB = findViewById(R.id.nestBut);
        annoB = findViewById(R.id.annoBut);

        thisB.setOnClickListener(this); // 자기 자신이 핸들러가 되기 때문. 버튼 객체가 많을때는 가독성때문에 이걸 많이씀.
        nestB.setOnClickListener(new MyCLickHandler());
        annoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "anno로 구현...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyCLickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // 잘 안씀. 익명을 설명하기 위해서 사용
            Toast.makeText(MainActivity.this, "nested로 구현", Toast.LENGTH_SHORT).show(); // Outer class.this
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "this로 구현...", Toast.LENGTH_SHORT).show();
    }
}
