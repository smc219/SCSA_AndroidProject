package edu.scsa.android.androidproject_seokminchang;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Button mbtn;
    Button tdbutton;
    Button newsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbtn = findViewById(R.id.mouse);
        tdbutton = findViewById(R.id.todo);
        newsButton = findViewById(R.id.news);
        setTitle("자기관리는 알아서!");
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MouseCatch.class);
                startActivity(i);
            }
        });
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewsTest.class);
                startActivity(i);
            }
        });
        tdbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Todo.class);
                startActivity(i);
            }
        });
    }

    public void moveToNews(View view) {
        Intent i = new Intent(MainActivity.this, NewsTest.class);
        startActivity(i);
    }
}
