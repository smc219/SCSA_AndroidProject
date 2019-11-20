package edu.scsa.android.androidproject_seokminchang;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toolbar;

public class Todo extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int FIN_ID = Menu.FIRST + 3;
    private TodoDBAdapter todoDBAdapter;
    public ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_todo);
        getActionBar().setTitle("To Do List");
        getActionBar().setIcon(null);
        todoDBAdapter = new TodoDBAdapter(this);
        todoDBAdapter.open();
        fillData();

        registerForContextMenu(getListView());


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void fillData() {
        Cursor todoCursor =todoDBAdapter.fetchAllNotes();
        startManagingCursor(todoCursor);
        String[] from = new String[]{TodoDBAdapter.KEY_TITLE};
        int[] to =new int[]{R.id.item};

        SimpleCursorAdapter todos =new SimpleCursorAdapter(this, R.layout.todo_row, todoCursor, from, to);
        setListAdapter(todos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, INSERT_ID, 0, "Add New To do");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, DELETE_ID, 0, "Delete");
        menu.add(0, EDIT_ID, 0, "Edit");
        menu.add(0, FIN_ID, 0, "Finish");

        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                todoDBAdapter.updateNote(info.id, "true");

                break;
        }
        return super.onContextItemSelected(item);
    }

    public void createNote() {
        Intent i = new Intent(this, TodoEdit.class);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    public void changeFstatus() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }

}
