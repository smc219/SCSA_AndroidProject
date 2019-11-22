package edu.scsa.android.androidproject_seokminchang;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TodoEdit extends AppCompatActivity {

    EditText titleEt;
    EditText bodyEt;
    Button addBut;
    Button cancelBut;
    EditText dateEt;
    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private TodoDBAdapter mDbHelper;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new TodoDBAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.activity_todo_edit);
        // 타이틀과 바디를 찾아본다.
        setTitle("To do List");
        titleEt = findViewById(R.id.titleEt);
        bodyEt = findViewById(R.id.bodyEt);
        dateEt = findViewById(R.id.dateEt);
        addBut = findViewById(R.id.addBut);
        cancelBut = findViewById(R.id.cancelBut);
        cal = Calendar.getInstance();

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(TodoDBAdapter.KEY_ROWID)
                : null;

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(TodoDBAdapter.KEY_ROWID)
                    : null;
        }

        Log.i("mRowId", "mrow id : " + mRowId);
        populateField();

        addBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEt.getText().toString().trim().length() <= 0 ) {
                    Toast.makeText(TodoEdit.this, "제목을 입력해주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dateEt.getText().toString().trim().length() <= 0) {
                    Toast.makeText(TodoEdit.this, "날짜를 입력해주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                setResult(RESULT_OK);
                saveState();
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
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TodoEdit.this, dateD, cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    DatePickerDialog.OnDateSetListener dateD = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
            dateEt.setText(sdf.format(cal.getTime()));
        }

    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TodoDBAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }



    @Override
    protected void onResume() {
        super.onResume();
       // populateField();
    }

    private void populateField() {
        if (mRowId != null) {
            Cursor tdCursor =mDbHelper.fetchNote(mRowId);
            titleEt.setText(tdCursor.getString(tdCursor.getColumnIndexOrThrow(TodoDBAdapter.KEY_TITLE)));
            bodyEt.setText(tdCursor.getString(tdCursor.getColumnIndexOrThrow(TodoDBAdapter.KEY_BODY)));
            dateEt.setText(tdCursor.getString(tdCursor.getColumnIndexOrThrow(TodoDBAdapter.KEY_DATE)));
        }
    }

    private void saveState() {
        String title = titleEt.getText().toString();
        String body = bodyEt.getText().toString();
        String todoDate = dateEt.getText().toString();



        if(mRowId == null) {
            long id = mDbHelper.createTodo(title, body, todoDate);
            if (id > 0)mRowId = id;
        } else {
            Cursor tdCursor =mDbHelper.fetchNote(mRowId);
            int cIdx = tdCursor.getColumnIndex("finished");
            int fValue = tdCursor.getInt(cIdx);
            String updateDate = tdCursor.getString(tdCursor.getColumnIndexOrThrow(TodoDBAdapter.KEY_DATE));
            Log.e("INFO", "finished value : " + fValue);
            mDbHelper.updateNote(mRowId, title, body, fValue, updateDate);
        }
    }

}
