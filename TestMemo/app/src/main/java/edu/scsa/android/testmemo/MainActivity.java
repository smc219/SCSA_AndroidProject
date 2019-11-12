package edu.scsa.android.testmemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MemoManager manager;
    TextView t1;
    TextView t2;
    Button addMemoBut;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView4);
        addMemoBut = findViewById(R.id.addMemoBut);
        manager = new MemoManager();
        i = new Intent(this, EditMemoActivity.class);



        addMemoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(i, 2);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK) {
            int idx;

            String[] strs = data.getStringArrayExtra("ok");

            manager.addMemo(new Memo(strs[0], strs[1], strs[2]));
            idx = manager.getAllMemo().size() - 1;

            t1.setText(manager.getAllMemo().get(idx).getTitle());
            if (idx >= 1) t2.setText(manager.getAllMemo().get(idx-1).getTitle());

        }

    }
}
