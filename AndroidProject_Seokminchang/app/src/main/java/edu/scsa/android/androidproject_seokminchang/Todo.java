package edu.scsa.android.androidproject_seokminchang;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM;

public class Todo extends AppCompatActivity{
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int FIN_ID = Menu.FIRST + 3;
    private TodoDBAdapter todoDBAdapter;
    private TabLayout tl;
    ListView lv;
    boolean filtered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lv = findViewById(R.id.todoLv);
        setTitle("To Do List");
        tl = findViewById(R.id.todoTab);
       // getActionBar().setIcon(null);
        todoDBAdapter = new TodoDBAdapter(this);
        todoDBAdapter.open();
        fillData();

        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(Todo.this, TodoEdit.class);
                i.putExtra(TodoDBAdapter.KEY_ROWID, id);
                startActivityForResult(i, ACTIVITY_EDIT);

            }
        });

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selected = tab.getPosition();
                switch (selected) {
                    case 0:
                        fillData();
                        break;
                    case 1:
                        fillData(0);
                        break;
                    case 2:
                        fillData(1);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    // 커스텀 레이아웃
    class noteAdapter extends SimpleCursorAdapter {
        private Context mContext;
        private int layout;
        private Cursor cr;
        private final LayoutInflater inflater;


        public noteAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
            this.layout=layout;
            this.mContext = context;
            this.inflater=LayoutInflater.from(context);
            this.cr=c;

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            View tv = inflater.inflate(layout, parent, false);

            return tv;
            //return super.newView(context, cursor, parent);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            Log.i("INFO", "fin"+cursor.getColumnIndex(TodoDBAdapter.KEY_TITLE));


            if (cursor != null) {

                //String strFV = cursor.getString(3);
                String Title = cursor.getString(cursor.getColumnIndex(TodoDBAdapter.KEY_TITLE));
                String strDate = cursor.getString(cursor.getColumnIndex(TodoDBAdapter.KEY_DATE));

                int fV = cursor.getInt(cursor.getColumnIndex(TodoDBAdapter.KEY_FINISHED));
                TextView tv = view.findViewById(R.id.textView);
                TextView tv2 = view.findViewById(R.id.textView2);
                tv.setText(Title);
                tv2.setText(strDate);
                if (fV > 0) {
                    tv.setTextColor(Color.GRAY);
                    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else tv.setTextColor(Color.BLACK);
                //super.bindView(view, context, cursor);
            }
        }
    }

    public void fillData() {
        Cursor todoCursor =todoDBAdapter.fetchAllNotes();
        startManagingCursor(todoCursor);
        String[] from = new String[]{TodoDBAdapter.KEY_TITLE, TodoDBAdapter.KEY_DATE};
        int[] to =new int[]{R.id.textView, R.id.textView2};

        noteAdapter todos;
        todos = new noteAdapter(this, R.layout.todo_row_ver2, todoCursor, from, to);

        lv.setAdapter(todos);
    }

    public void fillData(int fValue) {
        Cursor todoCursor =todoDBAdapter.fetchQueryNotes(fValue);
        startManagingCursor(todoCursor);
        String[] from = new String[]{TodoDBAdapter.KEY_TITLE, TodoDBAdapter.KEY_DATE};
        int[] to =new int[]{R.id.textView, R.id.textView2};

        noteAdapter todos;
        todos = new noteAdapter(this, R.layout.todo_row_ver2, todoCursor, from, to);

        lv.setAdapter(todos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu.add(0, INSERT_ID + 1, 0, "Filter")
          //      .setIcon(R.drawable.ic_filter_list_white_24dp).setShowAsActionFlags(SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, INSERT_ID, 0, "Add New To do").setIcon(R.drawable.ic_add_white_24dp).setShowAsActionFlags(SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, INSERT_ID + 2, 0, "Delete Finished");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
            case INSERT_ID + 1:
                return true;
            case INSERT_ID + 2:
                todoDBAdapter.deleteTodo();
                fillByTab();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, DELETE_ID, 0, "Delete");
        menu.add(2, FIN_ID, 0, "Change Status");

        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        switch (item.getItemId()) {
            case FIN_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Log.i("CMENUINFO", "CMENUINFO + " + info.id);
                int value = todoDBAdapter.finReturn(info.id);
                todoDBAdapter.updateNote(info.id, value ^ 1);
                fillByTab();
                break;
            case DELETE_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Log.i("CMENUINFO", "CMENUINFO + " + info.id);
                todoDBAdapter.deleteTodo(info.id);
                fillByTab();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void createNote() {
        Intent i = new Intent(this, TodoEdit.class);

        startActivityForResult(i, ACTIVITY_EDIT);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int selected = tab.getPosition();
                switch (selected) {
                    case 0:
                        fillData();

                        break;
                    case 1:
                        fillData(0);
                        break;
                    case 2:
                        fillData(1);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
      //  fillData();
    }

    public void fillByTab() {
        int selected = tl.getSelectedTabPosition();
        if (selected != 0) fillData(selected-1);
        else fillData();
    }


}
