package edu.jaen.android.storage.cp;

import edu.jaen.android.storage.cp.Notes;
import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends Activity {

	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit);

		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		Button confirmButton = (Button) findViewById(R.id.confirm);

		mRowId = savedInstanceState != null ? savedInstanceState.getLong("_id")
				: null;
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(Notes._ID) : null;
		}
		populateFields();
		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();
			}

		});
	}

	private void populateFields() {
		if (mRowId != null) {
			CursorLoader cursor = new CursorLoader(this, Notes.CONTENT_URI,
					new String[] { Notes._ID, Notes.TITLE, Notes.BODY },
					Notes._ID + "=" + mRowId, null, null);
			Cursor noteCursor = cursor.loadInBackground();
			// managedQuery(Notes.CONTENT_URI,new String[]{
			// Notes._ID,Notes.TITLE,Notes.BODY}, Notes._ID+"="+mRowId, null,
			// null);
			noteCursor.moveToFirst();
			mTitleText.setText(noteCursor.getString(1));
			mBodyText.setText(noteCursor.getString(2));

		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(Notes._ID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		ContentValues values = new ContentValues();
		values.put(Notes.TITLE, mTitleText.getText().toString());
		values.put(Notes.BODY, mBodyText.getText().toString());
		if (mRowId == null)
			getContentResolver().insert(Notes.CONTENT_URI, values);
		else
			getContentResolver().update(Notes.CONTENT_URI, values,
					Notes._ID + "=" + mRowId, null);

	}

}
