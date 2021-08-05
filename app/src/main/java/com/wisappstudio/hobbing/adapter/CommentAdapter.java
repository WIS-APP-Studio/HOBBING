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
import com.wisappstudio.hobbing.data.CommentData;

import java.util.ArrayList;

import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;

public class CommentAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CommentData> sample;

    public CommentAdapter(Context context, ArrayList<CommentData> data) {
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
    public CommentData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_comment, null);

        TextView writer = (TextView) view.findViewById(R.id.list_comment_writer);
        TextView description = (TextView) view.findViewById(R.id.list_comment_description);
        ImageView profile_image = (ImageView) view.findViewById(R.id.list_comment_profile_image);

        writer.setText(sample.get(position).getWriter());
        description.setText(sample.get(position).getDescription());

        Glide.with(mContext)
                .load(PROFILE_IMAGE_DIRECTORY + sample.get(position).getWriter() + ".png") // 임시로 로드
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
