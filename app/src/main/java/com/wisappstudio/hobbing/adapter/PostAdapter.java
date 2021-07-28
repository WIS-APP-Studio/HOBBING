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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.data.PostData;

import java.util.ArrayList;

import static com.wisappstudio.hobbing.data.ServerData.IMAGE_DIRECTORY_URL;

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
        ImageView profile_image = (ImageView)view.findViewById(R.id.list_post_profile);
        TextView likes = (TextView)view.findViewById(R.id.list_post_likes);
        TextView views = (TextView)view.findViewById(R.id.list_post_views);
        TextView shares = (TextView)view.findViewById(R.id.list_post_shares);
        TextView category = (TextView)view.findViewById(R.id.list_post_category);
        TextView date = (TextView)view.findViewById(R.id.list_post_tv_date);

        writer.setText(sample.get(position).getWriter());
        title.setText(sample.get(position).getTitle());
        description.setText(sample.get(position).getDescription());
        likes.setText(sample.get(position).getLikes());
        views.setText(sample.get(position).getViews());
        shares.setText(sample.get(position).getShares());
        category.setText("@"+sample.get(position).getCategory());
        date.setText(sample.get(position).getDate());

        Glide.with(mContext)
                .load(IMAGE_DIRECTORY_URL+sample.get(position).getWriter()+".png") // 임시로 로드
                .apply(new RequestOptions()
                    .signature(new ObjectKey("signature string"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(profile_image);

        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setColor(mContext.getColor(R.color.signature));
        shapeDrawable.setShape(new OvalShape());

        profile_image.setBackground(shapeDrawable);
        profile_image.setClipToOutline(true);

        return view;
    }
}
