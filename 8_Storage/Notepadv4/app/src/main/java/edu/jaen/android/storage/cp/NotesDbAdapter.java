package edu.jaen.android.storage.cp;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class NotesDbAdapter extends ContentProvider {

	private DatabaseHelper mOpenHelper;
	private static final String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, "
			+ "title text not null, body text not null);";
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "notes";
	private static final int DATABASE_VERSION = 2;
	private static HashMap<String, String> sNotesProjectionMap;

	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"
			+ Notes.AUTHORITY;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"
			+ Notes.AUTHORITY;
	private static final int NOTES = 1;
	private static final int NOTE_ID = 2;

	private static final UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Notes.AUTHORITY, "notes", NOTES);
		sUriMatcher.addURI(Notes.AUTHORITY, "notes/#", NOTE_ID);

		sNotesProjectionMap = new HashMap<String, String>();
		sNotesProjectionMap.put(Notes._ID, Notes._ID);
		sNotesProjectionMap.put(Notes.TITLE, Notes.TITLE);
		sNotesProjectionMap.put(Notes.BODY, Notes.BODY);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (sUriMatcher.match(uri) != NOTES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(DATABASE_TABLE, null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(Notes.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case NOTES:
			count = db.update(DATABASE_TABLE, values, where, whereArgs);
			break;

		case NOTE_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(DATABASE_TABLE, values,
					Notes._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case NOTES:
			count = db.delete(DATABASE_TABLE, where, whereArgs);
			break;

		case NOTE_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(DATABASE_TABLE,
					Notes._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case NOTES:
			qb.setTables(DATABASE_TABLE);
			qb.setProjectionMap(sNotesProjectionMap);
			break;

		case NOTE_ID:
			qb.setTables(DATABASE_TABLE);
			qb.setProjectionMap(sNotesProjectionMap);
			qb.appendWhere(Notes._ID + "=" + uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Notes.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;

	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case NOTES:
			return CONTENT_TYPE;

		case NOTE_ID:
			return CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("DB", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	public void close() {
		mOpenHelper.close();
	}

}
