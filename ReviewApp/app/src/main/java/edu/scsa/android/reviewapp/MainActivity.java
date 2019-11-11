package edu.scsa.android.reviewapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView nameView;
    public static final int EDIT_MODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameView = findViewById(R.id.nameView);

    }

    public void send(View view) {
        Intent i = new Intent("com.scsa.android.receive");
        String str = nameView.getText().toString() + " check";
        i.putExtra("sent", str);
        startActivity(i);
    }

    public void edit(View view) {
        Intent i = new Intent(this, EditActivity.class);
        i.putExtra("name", nameView.getText().toString());
        startActivityForResult(i,EDIT_MODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_MODE && resultCode == RESULT_OK) {
            nameView.setText(data.getStringExtra("name"));
        }
    }
}
