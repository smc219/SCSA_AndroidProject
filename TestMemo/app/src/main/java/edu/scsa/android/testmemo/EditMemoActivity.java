package edu.scsa.android.testmemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.text.DateFormat.getDateInstance;

public class EditMemoActivity extends AppCompatActivity  implements View.OnClickListener {

    EditText nameEdit;
    EditText contentEdit;
    EditText dateEdit;
    Button okBut;
    Button cancelBut;
    Intent i;
    Memo m;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        nameEdit = findViewById(R.id.nameEdit);
        contentEdit = findViewById(R.id.contentEdit);
        dateEdit = findViewById(R.id.dateEdit);
        okBut = findViewById(R.id.okBut);
        cancelBut = findViewById(R.id.cancelBut);
        getDateInstance();
//        dateEdit.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dateEdit.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(new Date()));
        okBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if(v==okBut) {
            String[] date = dateEdit.getText().toString().split("-");

            if (date.length < 3 || date.length > 4|| (date[0]+date[1]+date[2]).matches("[0-9]+") == false || date[0].length() != 4 || date[1].length() != 2 || date[2].length() != 2) {

                    Toast.makeText(this, "날짜를 올바른 양식으로 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
            }


            if(nameEdit.getText().toString().split(" ").length <= 0 || nameEdit.getText().toString().length() <= 0) {
                Toast.makeText(this, "공백이 아닌 이름을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (contentEdit.getText().toString() != null) {
                i = new Intent();
                String[] str = {nameEdit.getText().toString(), contentEdit.getText().toString(), dateEdit.getText().toString()};
                i.putExtra("ok", str);
                setResult(RESULT_OK, i);
                finish();
            }


        }
        else if(v == cancelBut) {
            setResult(RESULT_CANCELED);
            finish();
        }

    }
}
