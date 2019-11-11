package edu.scsa.android.testforresult;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView2;
    public static final int SEARCH_MODE = 1;
    public static final int EDIT_MODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         textView2 = findViewById(R.id.textView2);

    }


    public void edit(View view) {
        Intent i = new Intent("com.sm.android.action.EDIT");
        String str = textView2.getText().toString();
        i.putExtra("data", str);
        startActivityForResult(i, EDIT_MODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {
            String str = data.getStringExtra("edited");

            textView2.setText(str);

        }

    }
}
