package edu.jaen.android.storage.cr;

import edu.jaen.android.storage.cr.R;
import edu.jaen.android.storage.cp.Notes;
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

public class Notepadv4 extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        fillData();
        registerForContextMenu(getListView());
    }
    Cursor notesCursor =null;
    private void fillData() {
    	notesCursor= managedQuery(Notes.CONTENT_URI,
				new String[]{Notes._ID,Notes.TITLE,Notes.BODY}, null, null, null);
        
        String[] from = new String[]{Notes.TITLE};
        int[] to = new int[]{R.id.text1};
        SimpleCursorAdapter notes = 
        	    new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to, 1);
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
	        String[] parms=new String[]{new Long(info.id).toString()};
			getContentResolver().delete(Notes.CONTENT_URI,Notes._ID+"=?", parms);
			notesCursor.requery();
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
        i.putExtra(Notes._ID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
