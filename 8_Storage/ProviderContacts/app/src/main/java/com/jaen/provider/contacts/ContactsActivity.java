package com.jaen.provider.contacts;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactsActivity extends ListActivity {
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CursorLoader cursor = new CursorLoader(this,
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		Cursor c = cursor.loadInBackground();
		// Cursor c = managedQuery(ContactsContract.Contacts.CONTENT_URI, null,
		// null, null, null);

		if (c != null)
			detailContacts(c);

		String[] from = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts._ID };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, c, from, to,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Uri uri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, id);
		Intent i = new Intent(Intent.ACTION_EDIT, uri);
		startActivity(i);

		// Uri
		// uri=ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
		// id);
		// Intent i=new Intent(Intent.ACTION_VIEW, uri);
		// startActivity(i);
	}

	private void detailContacts(Cursor c) {
		int col = c.getColumnCount();
		Log.e("loglog", "c.getColumnCount()=" + col);

		for (int i = 0; i < col; i++) {
			Log.e("loglog", i + " " + c.getColumnName(i));
		}

		int row = c.getCount();
		Log.e("loglog", ".");
		Log.e("loglog", "c.getCount()=" + row);
		StringBuilder sb = new StringBuilder();
		while (c.moveToNext()) {
			for (int i = 0; i < col; i++) {
				sb.append(c.getString(i) + "\t");
			}
			Log.e("loglog", sb.toString());
			sb.setLength(0);
		}
	}
}
