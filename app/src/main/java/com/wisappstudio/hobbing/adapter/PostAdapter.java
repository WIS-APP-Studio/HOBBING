package com.wisappstudio.hobbing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hobbing.R;
import com.wisappstudio.hobbing.data.PostData;

import java.util.ArrayList;

public class PostAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<PostData> sample;

    public PostAdapter(Context context, ArrayList<PostData> data) {
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
    public PostData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_post, null);

        TextView writer = (TextView)view.findViewById(R.id.list_post_writer);
        TextView title = (TextView)view.findViewById(R.id.list_post_title);
        TextView description = (TextView)view.findViewById(R.id.list_post_tv_description);

        writer.setText(sample.get(position).getWriter());
        title.setText(sample.get(position).getTitle());
        description.setText(sample.get(position).getDescription());

        return view;
    }
}
