package com.wisappstudio.hobbing.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.data.ServerData;
import com.wisappstudio.hobbing.data.WritePostData;

import java.util.ArrayList;

import static com.wisappstudio.hobbing.data.ServerData.IMAGE_DIRECTORY_URL;

public class WritePostAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<WritePostData> sample;

    public WritePostAdapter(Context context, ArrayList<WritePostData> data) {
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
    public WritePostData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_post, null);

        TextView writer = (TextView)view.findViewById(R.id.list_post_writer);
        TextView title = (TextView)view.findViewById(R.id.list_post_title);
        TextView description = (TextView)view.findViewById(R.id.list_post_tv_description);
        ImageView profile_image = (ImageView)view.findViewById(R.id.list_post_profile);

        writer.setText(sample.get(position).getWriter());
        title.setText(sample.get(position).getTitle());
        description.setText(sample.get(position).getDescription());

        Glide.with(mContext)
                .load(IMAGE_DIRECTORY_URL+sample.get(position).getWriter()+".png")
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
