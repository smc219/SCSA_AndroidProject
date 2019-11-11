package edu.scsa.android.testforresult;

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
        String recStr = i.getStringExtra("data");
        editTv.setText(recStr);
    }

    public void cancel(View view) {
        //작업 취소
        setResult(RESULT_CANCELED);
        finish();
    }

    public void save(View view) {
        // 수정된 값을 꺼내서 인텐트에 담아 다시 Main으로 전달
        Intent i = new Intent();
        String str = String.valueOf(editTv.getText());
        i.putExtra("edited", str);
        setResult(RESULT_OK, i);
        finish();

    }
}
