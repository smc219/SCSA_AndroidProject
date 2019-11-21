package edu.scsa.android.androidproject_seokminchang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
//    private String[] mDataset;
    private ArrayList<News> mDataset;
    int[] newsCom = {R.drawable.chosun, R.drawable.kh, R.drawable.hani, R.drawable.ja};

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // news_row의 뷰 설정해주기

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        ImageView newsIv;
        TextView titleTv;
        TextView descTv;
        String link;
        String newsCom;

        public MyViewHolder(LinearLayout v) {
            super(v);
            //newsIv = v.findViewById(R.id.newsImage);

            titleTv = v.findViewById(R.id.newsTitle);
            descTv = v.findViewById(R.id.newsBody);
            titleTv.setOnClickListener(this);
            descTv.setOnClickListener(this);
            newsIv = v.findViewById(R.id.imageView3);


        }

        @Override
        public void onClick(View v) {
            Log.i("INFO","hello! now it works!");
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            v.getContext().startActivity(i);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(ArrayList<News> myDataset) {
        mDataset = myDataset;
        // 뷰를 할당해준다.
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
//         create a new view
        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_row, parent, false);

        MyViewHolder vh = new MyViewHolder(ll);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleTv.setText(mDataset.get(position).getNewsTitle());
        holder.descTv.setText(mDataset.get(position).getNewsDesc());
        holder.link = mDataset.get(position).getNewsLink();
        if (holder.link.indexOf("chosun") != -1) holder.newsIv.setImageResource(newsCom[0]);
        else if (holder.link.indexOf("khan") != -1) holder.newsIv.setImageResource(newsCom[1]);
        else if (holder.link.indexOf("hani") != -1) holder.newsIv.setImageResource(newsCom[2]);
        else if (holder.link.indexOf("joins") != -1) holder.newsIv.setImageResource(newsCom[3]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
