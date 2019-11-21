package edu.scsa.android.androidproject_seokminchang;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsTest extends AppCompatActivity {

    private TextView mTextMessage;
    private String[] myDataset = {"hello", "test"};
    String[] newsComs = {
            "http://myhome.chosun.com/rss/www_section_rss.xml",
            "http://www.khan.co.kr/rss/rssdata/total_news.xml",
            "https://rss.joins.com/joins_homenews_list.xml",
            "http://www.hani.co.kr/rss/lead/"
    };
    private ArrayList<News> newsDataset = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    BottomNavigationView bnv;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
// 추후에 다시 살려주기!

                case R.id.chosun:
                    new NewsParser().execute(newsComs[0]);
                    return true;
                case R.id.kyunghyang:
                    new NewsParser().execute(newsComs[1]);
                    return true;
                case R.id.joongang:
                    new NewsParser().execute(newsComs[2]);
                    return true;
                case R.id.hankyeorae:
                    new NewsParser().execute(newsComs[3]);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_test);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        //mAdapter = new NewsAdapter(newsDataset);
        //recyclerView.setAdapter(mAdapter);
        new NewsParser().execute(newsComs[0]);

    }

    class NewsParser extends AsyncTask<String, String, ArrayList<News>> {
        XmlPullParser parser = Xml.newPullParser();
        ArrayList<News> newsList = new ArrayList<>();
        @Override
        protected ArrayList<News> doInBackground(String... strings) {
            try {
                int itemflag = 0;
                parser.setInput(new URL(strings[0]).openConnection().getInputStream(), null);
                int eventType = parser.getEventType();
                News newN = null;
                while(eventType != XmlPullParser.END_DOCUMENT) {

                    String name;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT :
                            break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();


                            //Log.i("INFO", parser.nextText() + "<=name's text");
                            if (name.equalsIgnoreCase("item")) {
                                itemflag = 1;
                                newN = new News();
                            }
                            else if(itemflag == 1){
                                if (name.equalsIgnoreCase("title")) {
                                    String title = parser.nextText();
                                    title.replaceAll("<[^>]*>", "");
                                    newN.setNewsTitle(title);
                                }
                                else if(name.equalsIgnoreCase("description")) {
                                    String desc = parser.nextText();
                                    desc = desc.replaceAll("<[^>]*>", "");

                                    newN.setNewsDesc(desc);
                                }
                                else if(name.equalsIgnoreCase("link")) {
                                    newN.setNewsLink(parser.nextText());
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:

                            name = parser.getName();
                            if (name.equalsIgnoreCase("item") && newN != null) {
                                newsList.add(newN);
                            }
                            break;
                    }
                        eventType = parser.next();

                    }

                }catch (Exception e) {
                    e.printStackTrace();
            }
                return newsList;
            }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);

            mAdapter = new NewsAdapter(news);
            recyclerView.setAdapter(mAdapter);
        }

    }
}


