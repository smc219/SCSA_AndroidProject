package edu.jaen.android.storage.notepad3;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Notepadv3 extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private NotesDbAdapter mDbHelper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        mDbHelper = new NotesDbAdapter(this); // 이쪽에서 자신 객체를 넘겨준다.
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView()); // 상속받은 애가 가지고 있는 ListView를 가져오는 것.
    }
    
    private void fillData() {
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor); // 액티비티와 커서의 라이프사이클 일치시키는 함수라고 함!
        
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{NotesDbAdapter.KEY_TITLE};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
        	    new SimpleCursorAdapter(this, 
        	    		R.layout.notes_row, notesCursor, from, to);
        setListAdapter(notes);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
    	case DELETE_ID:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        mDbHelper.deleteNote(info.id);
	        fillData();
	        return true;
		}
		return super.onContextItemSelected(item);
	}
	
    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
