package com.wisappstudio.hobbing.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.data.MyPagePostData;

import java.util.ArrayList;

import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;

public class MyPagePostAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<MyPagePostData> sample;

    public MyPagePostAdapter(Context context, ArrayList<MyPagePostData> data) {
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
    public MyPagePostData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_my_page_post, null);

        TextView writer = (TextView)view.findViewById(R.id.list_my_page_writer);
        TextView title = (TextView)view.findViewById(R.id.list_my_page_title);
        TextView description = (TextView)view.findViewById(R.id.list_my_page_tv_description);
        ImageView profile_image = (ImageView)view.findViewById(R.id.list_my_page_profile);
        TextView likes = (TextView)view.findViewById(R.id.list_my_page_likes);
        TextView views = (TextView)view.findViewById(R.id.list_my_page_views);
        TextView shares = (TextView)view.findViewById(R.id.list_my_page_shares);

        writer.setText(sample.get(position).getWriter());
        title.setText(sample.get(position).getTitle());
        description.setText(sample.get(position).getDescription());
        likes.setText(sample.get(position).getLikes());
        views.setText(sample.get(position).getViews());
        shares.setText(sample.get(position).getShares());

        Log.d("MyPagePostImage", PROFILE_IMAGE_DIRECTORY +sample.get(position).getWriter()+".png");
        Glide.with(mContext)
                .load(PROFILE_IMAGE_DIRECTORY +sample.get(position).getWriter()+".png") // 임시로 로드
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(profile_image);

        profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        profile_image.setClipToOutline(true);

        return view;
    }
}