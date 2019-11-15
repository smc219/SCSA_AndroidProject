package edu.jaen.android.storage.cp;

import edu.jaen.android.storage.cp.Notes;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;

	CursorLoaderListFragment list;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentById(android.R.id.content) == null) {
			list = new CursorLoaderListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	public class CursorLoaderListFragment extends ListFragment implements
			LoaderManager.LoaderCallbacks<Cursor> {
		SimpleCursorAdapter mAdapter;
		String mCurFilter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			String[] from = new String[] { Notes.TITLE };
			int[] to = new int[] { R.id.text1 };
			mAdapter = new SimpleCursorAdapter(getActivity(),
					R.layout.notes_row, null, from, to, 0);
			setListAdapter(mAdapter);
			setListShown(false);
			getLoaderManager().initLoader(0, null, this);
			registerForContextMenu(getListView());
		}

		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			return new CursorLoader(getActivity(), Notes.CONTENT_URI, null,
					null, null, null);
		}

		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			mAdapter.swapCursor(data);
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		public void onLoaderReset(Loader<Cursor> loader) {
			mAdapter.swapCursor(null);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Intent i = new Intent(Notepadv4.this, NoteEdit.class);
			i.putExtra(Notes._ID, id);
			startActivityForResult(i, ACTIVITY_EDIT);
		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		}

		@Override
		public boolean onContextItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case DELETE_ID:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
						.getMenuInfo();
				String[] parms = new String[] { new Long(info.id).toString() };
				getContentResolver().delete(Notes.CONTENT_URI,
						Notes._ID + "=?", parms);
				getLoaderManager().restartLoader(0, null, list);
				return true;
			}
			return super.onContextItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createNote();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void createNote() {
		Intent i = new Intent(this, NoteEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
		getLoaderManager().restartLoader(0, null, list);
	}

}
