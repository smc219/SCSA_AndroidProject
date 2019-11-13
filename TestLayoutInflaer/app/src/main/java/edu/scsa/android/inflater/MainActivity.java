package edu.scsa.android.inflater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LayoutInflater layoutInf; // 이거 말고 메뉴도 xml 객체화 가능
    LinearLayout myLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutInf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myLinear = findViewById(R.id.myLinear);
    }

    public void showAtype(View view) {
        View aView = layoutInf.inflate(R.layout.a_layout, null); // View를 리턴.
        myLinear.removeAllViews(); // add는 계속 쌓기 떄문!
        myLinear.addView(aView);
    }

    public void showBtype(View view) {
        // 오늘 점심은 마라탕이라는 내용을 반영해볼것
        View bView = layoutInf.inflate(R.layout.b_layout, null); // View를 리턴.
        TextView tv  = bView.findViewById(R.id.titleTv);
        tv.setText("마라탕이 점심메뉴");
        myLinear.removeAllViews(); // add는 계속 쌓기 떄문!
        myLinear.addView(bView);
    }
}
