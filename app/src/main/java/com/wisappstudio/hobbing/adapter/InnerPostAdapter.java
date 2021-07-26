package com.wisappstudio.hobbing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hobbing.R;
import com.wisappstudio.hobbing.data.InnerPostData;

import java.util.ArrayList;


public class InnerPostAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<InnerPostData> sample;

    public InnerPostAdapter(Context context, ArrayList<InnerPostData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public InnerPostData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_inner_post, null);
        // 내부 뷰 위치

        TextView title = view.findViewById(R.id.activity_inner_post_title);

        title.setText(sample.get(position).getTitle());

        //
        return view;
    }
}