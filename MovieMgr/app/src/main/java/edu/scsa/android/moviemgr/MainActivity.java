package edu.scsa.android.moviemgr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView mListV;
    LayoutInflater layinf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListV = findViewById(R.id.movieLView);
        MovieAdapter<Movie> movieA = new MovieAdapter<>(R.layout.row_layout, getAllInfo());
        mListV.setAdapter(movieA);

    }

    class MovieAdapter<M> extends BaseAdapter {
        private int layout;
        private ArrayList<Movie> movList;
        private LayoutInflater layinf;

        public MovieAdapter(int rowLayout, ArrayList<Movie> movList) {
            this.layout = rowLayout;
            this.movList = movList;
            layinf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);     // 방법 1
        }

        @Override
        public int getCount() { //데이터의 크기를 리턴해줘야 한다.
            return movList.size();
        }

        @Override
        public Object getItem(int position) { // Position 정보 리턴
            if (position <= 0 || position >= movList.size()) return null;
            return movList.get(position);
        }

        @Override
        public long getItemId(int position) { //db에서는 개념이 다른데, 여기서는 그냥 포지션 리턴
            if (position < 0 || position >= movList.size()) return -1;
            return position;
        }

        @Override
//        public View getView(int position, View convertView, ViewGroup parent) { //이걸 클릭하면 각각 로우 보임
//            Movie selMovie = (Movie) getItem(position); // 코드의 재사용에 대해서 계속 생각해볼것!
//            // 그 로우의 view를 만들어서 return하는 함수.
//            if (convertView == null) {
//                convertView = View.inflate(MainActivity.this, layout, null);
//            }
//
//            View movieV1 = LayoutInflater.from(MainActivity.this).inflate(layout, null);
//
//            // 정석적으로는 선언해야하지만 그게 아니라 상단처럼쓰거나, Activity;.getLayoutInflater 또는 LayoutInflater.from 등의 방법이 있다.
//            // 근데 사실 movieV1을 만들때 사용된 코드를 뜯어보면 결국 getSystemService 사용.
//            // 이렇게 사용자들이 사용하기 편하게 여러가지로 wrapping 해놓은 경우가 많다.
//            View rView = layinf.inflate(layout, null);
//
//            TextView titleV = rView.findViewById(R.id.mTitleV);
//            TextView contV = rView.findViewById(R.id.mConV);
//            ImageView imgV = rView.findViewById(R.id.mimageV);
//            titleV.setText(movList.get(position).getmTitle());
//            contV.setText(movList.get(position).getmContent());
//            imgV.setImageResource(movList.get(position).getmImg());
//            return rView;
//        }
        // using convertView
        public View getView(int position, View convertView, ViewGroup parent) { //이걸 클릭하면 각각 로우 보임
            Movie selMovie = (Movie) getItem(position); // 코드의 재사용에 대해서 계속 생각해볼것!
            ViewHolder viewHolder;
            // 그 로우의 view를 만들어서 return하는 함수.
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, layout, null);
                viewHolder = new ViewHolder();
                viewHolder.mTv = convertView.findViewById(R.id.mTitleV);
                viewHolder.mConT = convertView.findViewById(R.id.mConV);
                viewHolder.mImgV = convertView.findViewById(R.id.mimageV);
                convertView.setTag(viewHolder); // 레퍼런스만 넘겨주는 것이다.

            } else viewHolder = (ViewHolder) convertView.getTag();


            // 정석적으로는 선언해야하지만 그게 아니라 상단처럼쓰거나, Activity;.getLayoutInflater 또는 LayoutInflater.from 등의 방법이 있다.
            // 근데 사실 movieV1을 만들때 사용된 코드를 뜯어보면 결국 getSystemService 사용.
            // 이렇게 사용자들이 사용하기 편하게 여러가지로 wrapping 해놓은 경우가 많다.
            //convertView = layinf.inflate(layout, null);

//            TextView titleV = convertView.findViewById(R.id.mTitleV);
//            TextView contV = convertView.findViewById(R.id.mConV);
//            ImageView imgV = convertView.findViewById(R.id.mimageV);
            // 이제 이 위의 세줄도 최적화가 가능하다. 아래처럼.
            viewHolder.mTv.setText(movList.get(position).getmTitle());
            viewHolder.mConT.setText(movList.get(position).getmContent());
            viewHolder.mImgV.setImageResource(movList.get(position).getmImg());
            return convertView;
        }
    }

    class ViewHolder {

        TextView mTv;
        TextView mConT;
        ImageView mImgV;

    }

    public ArrayList<Movie> getAllInfo() {
        ArrayList<Movie> tempL = new ArrayList<>();

        tempL.add(new Movie("조커", "~~~~~~~~~~", R.drawable.j));
        tempL.add(new Movie("해리포터", "~~~~~~~~~~", R.drawable.h));
        tempL.add(new Movie("엑시트", "~~~~~~~~~~", R.drawable.ex));
        tempL.add(new Movie("인셉션", "~~~~~~~~~~", R.drawable.in));
        tempL.add(new Movie("미스 슬로운", "~~~~~~~~~~", R.drawable.m));
        tempL.add(new Movie("조커", "~~~~~~~~~~", R.drawable.j));
        tempL.add(new Movie("해리포터", "~~~~~~~~~~", R.drawable.h));
        tempL.add(new Movie("엑시트", "~~~~~~~~~~", R.drawable.ex));
        tempL.add(new Movie("인셉션", "~~~~~~~~~~", R.drawable.in));
        tempL.add(new Movie("미스 슬로운", "~~~~~~~~~~", R.drawable.m));
        tempL.add(new Movie("조커", "~~~~~~~~~~", R.drawable.j));

        return tempL;
    }
}
