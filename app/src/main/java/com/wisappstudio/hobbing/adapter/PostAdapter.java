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

import com.android.volley.AuthFailureError;
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
import com.wisappstudio.hobbing.data.PostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_LIKES_URL;
import static com.wisappstudio.hobbing.data.ServerData.POST_IMAGE_DIRECTORY;
import static com.wisappstudio.hobbing.data.ServerData.POST_READ_IMAGE_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_READ_NICKNAME_URL;

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
        TextView likes = (TextView)view.findViewById(R.id.list_post_likes);
        TextView views = (TextView)view.findViewById(R.id.list_post_views);
        TextView shares = (TextView)view.findViewById(R.id.list_post_shares);
        TextView category = (TextView)view.findViewById(R.id.list_post_category);
        TextView date = (TextView)view.findViewById(R.id.list_post_tv_date);

        ImageView profile_image = (ImageView)view.findViewById(R.id.list_post_profile);

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

        title.setText(sample.get(position).getTitle());
        description.setText(sample.get(position).getDescription());
        views.setText(sample.get(position).getViews());
        shares.setText(sample.get(position).getShares());
        category.setText(sample.get(position).getCategory());
        date.setText(sample.get(position).getDate());

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

        StringRequest imageRequest = new StringRequest(Request.Method.POST, POST_READ_IMAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InitializeImage(jsonObject);
                } catch (JSONException e) { e.printStackTrace(); }
            }

            private void InitializeImage(JSONObject jsonObject) {
                String TAG_JSON = "게시물_사진";
                ImageView image1 = (ImageView) view.findViewById(R.id.list_post_image1);
                ImageView image2 = (ImageView) view.findViewById(R.id.list_post_image2);
                ImageView image3 = (ImageView) view.findViewById(R.id.list_post_image3);
                ImageView[] imageViews = {image1, image2, image3};

                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String imageLink = item.get("사진").toString();

                        Glide.with(mContext)
                                .load(POST_IMAGE_DIRECTORY+sample.get(position).getNumber()+"/"+imageLink)
                                .into(imageViews[i]);
                    }
                    for(int i = 0; i < 3; i++) {
                        if(imageViews[i].getDrawable() == null) {
                            imageViews[i].getLayoutParams().width = 0;
                            imageViews[i].getLayoutParams().height = 0;
                        }
                    }
                } catch (JSONException e) { e.printStackTrace(); }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("number", sample.get(position).getNumber());
                return params;
            }
        };
        queue.add(imageRequest);

        StringRequest postLikesRequest = new StringRequest(Request.Method.POST, INNER_POST_LIKES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InitializePostLikes(jsonObject);
                } catch (JSONException e) { }
            }
            private void InitializePostLikes(JSONObject jsonObject) {
                String TAG_JSON = "좋아요_수";

                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    likes.setText(String.valueOf(jsonArray.length()));
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
                Map<String, String> params = new HashMap<String, String>();
                String number = sample.get(position).getNumber();
                params.put("number", number);
                return params;
            }
        };
        queue.add(postLikesRequest);

        profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        profile_image.setClipToOutline(true);

        return view;
    }
}
