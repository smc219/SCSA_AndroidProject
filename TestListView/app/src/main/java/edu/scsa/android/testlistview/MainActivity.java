package edu.scsa.android.testlistview;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
//public class MainActivity extends AppCompatActivity
public class MainActivity extends ListActivity { // List에 특화

    String[] foodArr = {"마라탕", "탕수육", "초밥", "양꼬치", "떡볶이"};
    ListView myListV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        // 1. 사용할 ListView 객체를 가져온다\
       // myListV = findViewById(R.id.myListView);
        // 2. 어댑터뷰에 설정할 어댑터를 만들고
        final ArrayAdapter<String> foodA = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foodArr);
        //final ArrayAdapter<String> foodA = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.titleTv, foodArr);
        setListAdapter(foodA);
        // 3. 어댑터뷰에 어댑터를 설정한다
        //myListV.setAdapter(foodA);
        // 4. 이벤트
        //myListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // 내 코드 //Toast.makeText(MainActivity.this, (foodArr[position]+"이/가 선택되었습니다."), Toast.LENGTH_SHORT).show();
//                // 선생님 코드
//                String selItem = (String)parent.getAdapter().getItem(position);
//                Toast.makeText(MainActivity.this,  selItem + " 선택됨" , Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String selItem = (String)l.getAdapter().getItem(position);
        Toast.makeText(this, selItem + " 선택됨", Toast.LENGTH_SHORT).show();

    }
}
