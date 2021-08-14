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
import com.wisappstudio.hobbing.data.CommentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_READ_NICKNAME_URL;

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

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        StringRequest nicknameRequest = new StringRequest(Request.Method.POST, PROFILE_READ_NICKNAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String TAG_JSON = "프로필";
                    String NICKNAME = "닉네임";
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                        JSONObject item = jsonArray.getJSONObject(0);
                        String nickname = item.getString(NICKNAME);
                        writer.setText(nickname);

                    } catch (JSONException e) { }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String ,String>();
                params.put("id", sample.get(position).getWriter());
                return params;
            }
        };
        queue.add(nicknameRequest);

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

        profile_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        profile_image.setClipToOutline(true);

        return view;
    }
}
