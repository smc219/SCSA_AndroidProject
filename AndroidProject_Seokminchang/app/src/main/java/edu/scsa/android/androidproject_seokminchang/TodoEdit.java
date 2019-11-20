package edu.scsa.android.androidproject_seokminchang;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TodoEdit extends AppCompatActivity {

    EditText titleEt;
    EditText bodyEt;
    Button addBut;
    Button cancelBut;
    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private TodoDBAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new TodoDBAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.activity_todo_edit);
        // 타이틀과 바디를 찾아본다.
        titleEt = findViewById(R.id.titleEt);
        bodyEt = findViewById(R.id.bodyEt);
        addBut = findViewById(R.id.addBut);
        cancelBut = findViewById(R.id.cancelBut);

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(TodoDBAdapter.KEY_ROWID)
                : null;

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(TodoDBAdapter.KEY_ROWID)
                    : null;
        }


        populateField();

        addBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();;
            }
        });
        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();;
            }
        });


    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TodoDBAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }



    @Override
    protected void onResume() {
        super.onResume();
        populateField();
    }

    private void populateField() {
        if (mRowId != null) {
            Cursor tdCursor =mDbHelper.fetchNote(mRowId);
            titleEt.setText(tdCursor.getColumnIndexOrThrow(TodoDBAdapter.KEY_TITLE));
            bodyEt.setText(tdCursor.getColumnIndexOrThrow(TodoDBAdapter.KEY_BODY));

        }
    }

    private void saveState() {
        String title = titleEt.getText().toString();
        String body = bodyEt.getText().toString();




        if(mRowId == null) {
            long id = mDbHelper.createTodo(title, body);
            if (id > 0)mRowId = id;
        } else {
            Cursor tdCursor =mDbHelper.fetchNote(mRowId);
            int cIdx = tdCursor.getColumnIndex("finished");
            String fValue = tdCursor.getString(cIdx);
            Log.e("INFO", "finished value : " + fValue);
            mDbHelper.updateNote(mRowId, title, body, fValue);
        }
    }


}
