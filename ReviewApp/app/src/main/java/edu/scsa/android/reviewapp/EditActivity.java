package edu.scsa.android.reviewapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText editTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editTv = findViewById(R.id.editTv);
        Intent i = getIntent();
        String str = i.getStringExtra("name");
        editTv.setText(str);
    }

    public void save(View view) {
        Intent i = new Intent();
        String str = editTv.getText().toString();
        i.putExtra("name", str);
        setResult(RESULT_OK, i);
        finish();

    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
