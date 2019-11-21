package edu.scsa.android.androidproject_seokminchang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDBAdapter {
    private final Context mCtx;
    private static final String DATABASE_NAME = "data228";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
            "create table notes (_id integer primary key autoincrement, "+ "title text not null, body text not null, finished integer not null, date text not null);";
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_FINISHED = "finished";
    public static final String KEY_DATE = "date";
    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

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
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes"); // 위에 final로 선언된 version의 값이 바뀌면 그때 이 함수가 수행되는 것.
            onCreate(db);

        }



    }
    // 콘텍스트를 받아서 생성
    public TodoDBAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }
    // 읽고 쓸 수 있는 데이터베이스를 확보
    public TodoDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long createTodo(String title, String body, String todoDate) {
        ContentValues cv =new ContentValues();
        cv.put("title", title);
        cv.put("body", body);
        cv.put("finished", 0);
        cv.put("date", todoDate);
        return mDb.insert(DATABASE_TABLE, null, cv);
    }
    public boolean deleteTodo(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllNotes() { //Cursor -> 가리키는 것.

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_FINISHED, KEY_DATE}, null, null, null, null, KEY_DATE + " ASC");
    }

    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_FINISHED, KEY_DATE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; //KEY_ROWID ->

    }


    public boolean updateNote(long rowId, String title, String body, int finished, String todoDate) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_FINISHED, finished);
        args.put(KEY_DATE, todoDate);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateNote(long rowId, int fstatus) {
        ContentValues args = new ContentValues();
        Log.i("INFO", "rowId" + rowId);
//        Cursor updateC = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
//                                KEY_TITLE, KEY_BODY, KEY_FINISHED}, KEY_ROWID + "=" + rowId, null,
//                        null, null, null, null);
        Cursor updateC = fetchNote(rowId);
        String title = updateC.getString(1);
        String body = updateC.getString(updateC.getColumnIndex(KEY_BODY));
        Log.i("updateCheck", title + body);
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put("finished", fstatus);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }



}
