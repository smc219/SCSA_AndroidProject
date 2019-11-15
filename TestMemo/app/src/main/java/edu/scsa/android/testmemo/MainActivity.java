package edu.scsa.android.testmemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
        //addMemoBut = findViewById(R.id.addMemoBut);
        manager = MemoManager.getInstance();
        i = new Intent(this, EditMemoActivity.class);
        addFakeData();

        memoA =new ArrayAdapter<Memo>(this, android.R.layout.simple_list_item_1, manager.getAllMemo());
        memolV.setAdapter(memoA);
        registerForContextMenu(memolV);
        //memolV.setTranscriptMode(2);


        memolV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strs = new String[]{manager.getAllMemo().get(position).getTitle(), manager.getAllMemo().get(position).getContent(), manager.getAllMemo().get(position).getDate()};
                i.putExtra("edited", strs);
                i.putExtra("requestCode", UPDATE_MODE);
                startActivityForResult(i, UPDATE_MODE);
            }
        });



//        addMemoBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i.putExtra("add", "update");
//                i.putExtra("requestCode", ADD_MODE);
//                startActivityForResult(i, ADD_MODE);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 1, Menu.NONE, R.string.addBut).setIcon(android.R.drawable.ic_input_add)
               .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 2, Menu.NONE, R.string.versionBut);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 3, 0,  "DELETE");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 3) {
            AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            manager.deleteMemo(manager.getAllMemo().get(mi.position).getTitle());
            memoA.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder b;
        switch (item.getItemId()) {
            case 1 :
                i.putExtra("add", "update");
                i.putExtra("requestCode", ADD_MODE);
                startActivityForResult(i, ADD_MODE);
                break;
            case 2:
                b = new AlertDialog.Builder(MainActivity.this).setMessage(R.string.version).setTitle(R.string.versionTitle).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                b.create().show();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
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


           memoA.notifyDataSetChanged();


        }

    }

    void addFakeData() {
        for (int i = 0; i <= 30; i++) {
            Memo m = new Memo("string" + i, "s" + i, "2012-10-21");
            manager.addMemo(m);
        }
    }
}
