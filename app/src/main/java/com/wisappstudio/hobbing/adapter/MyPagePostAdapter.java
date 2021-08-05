package com.wisappstudio.hobbing.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.data.MyPagePostData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.POST_DELETE_URL;
import static com.wisappstudio.hobbing.data.ServerData.POST_IMAGE_DIRECTORY;
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
        TextView category = (TextView)view.findViewById(R.id.list_my_page_post_category);
        TextView date = (TextView)view.findViewById(R.id.list_my_page_post_date);

        ImageView delete = view.findViewById(R.id.list_my_page_post_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("게시물 삭제");
                builder.setMessage("게시물을 삭제할까요?");
                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                StringRequest deletePostRequest = new StringRequest(Request.Method.POST, POST_DELETE_URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(mContext, "게시물을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("MyPageErr", error.getMessage());
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String ,String>();
                                        params.put("number", sample.get(position).getNumber());
                                        return params;
                                    }
                                };

                                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                                queue.add(deletePostRequest);
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) { }
                        });

                builder.show();
            }
        });

        writer.setText(sample.get(position).getWriter());
        title.setText(sample.get(position).getTitle());
        description.setText(sample.get(position).getDescription());
        likes.setText(sample.get(position).getLikes());
        views.setText(sample.get(position).getViews());
        shares.setText(sample.get(position).getShares());
        category.setText("@"+sample.get(position).getCategory());
        date.setText(sample.get(position).getDate());

        ImageView image1 = (ImageView) view.findViewById(R.id.list_my_page_post_image1);
        ImageView image2 = (ImageView) view.findViewById(R.id.list_my_page_post_image2);
        ImageView image3 = (ImageView) view.findViewById(R.id.list_my_page_post_image3);

        Glide.with(mContext)
                .load(POST_IMAGE_DIRECTORY+sample.get(position).getNumber()+"/1.jpeg")
                .into(image1);

        Glide.with(mContext)
                .load(POST_IMAGE_DIRECTORY+sample.get(position).getNumber()+"/2.jpeg")
                .into(image2);

        Glide.with(mContext)
                .load(POST_IMAGE_DIRECTORY+sample.get(position).getNumber()+"/3.jpeg")
                .into(image3);

        if(image1.getDrawable() == null) {
            image1.getLayoutParams().width = 0;
            image1.getLayoutParams().height = 0;
        }

        if(image2.getDrawable() == null) {
            image2.getLayoutParams().width = 0;
            image2.getLayoutParams().height = 0;
        }

        if(image3.getDrawable() == null) {
            image3.getLayoutParams().width = 0;
            image3.getLayoutParams().height = 0;
        }

        Glide.with(mContext)
                .load(PROFILE_IMAGE_DIRECTORY +sample.get(position).getWriter()+".png") // 임시로 로드
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