package edu.scsa.android.testmemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    MemoManager manager;
    ListView memolV;
    Button addMemoBut;
    String[] strs;
    ArrayAdapter<Memo> memoA;
    public static final int ADD_MODE = 2;
    public static final int UPDATE_MODE = 3;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memolV = findViewById(R.id.memolV);
        addMemoBut = findViewById(R.id.addMemoBut);
        manager = MemoManager.getInstance();
        i = new Intent(this, EditMemoActivity.class);
        addFakeData();
        memoA =new ArrayAdapter<Memo>(this, android.R.layout.simple_list_item_1, manager.getAllMemo());
        memolV.setAdapter(memoA);

        memolV.setTranscriptMode(2);


        memolV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strs = new String[]{manager.getAllMemo().get(position).getTitle(), manager.getAllMemo().get(position).getContent(), manager.getAllMemo().get(position).getDate()};
                i.putExtra("edited", strs);
                i.putExtra("requestCode", UPDATE_MODE);
                startActivityForResult(i, UPDATE_MODE);
            }
        });

        addMemoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("add", "update");
                i.putExtra("requestCode", ADD_MODE);
                startActivityForResult(i, ADD_MODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int idx;
        String[] strs;
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {

            if (requestCode == ADD_MODE) {

                strs = data.getStringArrayExtra("ok");
                Log.i("INFO", strs[0]);

                if (manager.addMemo(new Memo(strs[0], strs[1], strs[2])) == -1) {
                    Toast.makeText(this, "이미 존재하는 메모입니다", Toast.LENGTH_SHORT).show();
                };
                Log.i("INFO", manager.getAllMemo().get(0).toString());

            }
            else if (requestCode == UPDATE_MODE) {

                strs = data.getStringArrayExtra("ok");
                manager.updateMemo(strs[0], new Memo(strs[0], strs[1], strs[2]));

            }

            memoA =new ArrayAdapter<Memo>(this, android.R.layout.simple_list_item_1, manager.getAllMemo());
            memolV.setAdapter(memoA);


        }

    }

    void addFakeData() {
        for (int i = 0; i <= 30; i++) {
            Memo m = new Memo("string" + i, "s" + i, "2012-10-21");
            manager.addMemo(m);
        }
    }
}