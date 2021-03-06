package com.wisappstudio.hobbing.fragment;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.wisappstudio.hobbing.activity.InnerPostActivity;
import com.wisappstudio.hobbing.activity.ProfileSettingActivity;
import com.wisappstudio.hobbing.adapter.MyPagePostAdapter;
import com.wisappstudio.hobbing.data.MyPagePostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.MY_PAGE_POST_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_READ_NICKNAME_URL;

public class MyPageFragment extends Fragment {
    private String userId;
    private View view;
    private String introduce;
    ArrayList<MyPagePostData> postDataList;
    private RequestQueue queue;

    public static Fragment fragment;

    public MyPageFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.activity_my_page, container, false);

        fragment = MyPageFragment.this;

        TextView id = (TextView) view.findViewById(R.id.activity_my_page_id);
        ImageView profile_image = (ImageView) view.findViewById(R.id.activity_my_page_image);
        ImageView profile_setting = (ImageView) view.findViewById(R.id.activity_my_page_setting_profile);

        profile_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ProfileSettingActivity.class);
                intent.putExtra("user_id", userId);
                intent.putExtra("introduce", introduce);
                startActivity(intent);
            }
        });

        // ?????? ?????? ????????? ??????
        Glide.with(view.getContext())
                .load(PROFILE_IMAGE_DIRECTORY+userId+".png") // ????????? ??????
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(profile_image);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setColor(view.getContext().getColor(R.color.signature));
        shapeDrawable.setShape(new OvalShape());

        profile_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profile_image.setBackground(shapeDrawable);
        profile_image.setClipToOutline(true);

        id.setText(userId);

        queue = Volley.newRequestQueue(view.getContext());

        loadProfileDescription();
        loadPosts();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void InitializePostData(JSONObject jsonObject)
    {
        postDataList = new ArrayList<MyPagePostData>();
        String TAG_JSON = "?????????_??????";
        String NUMBER = "??????";
        String WRITER = "?????????";
        String DATE = "????????????";
        String CATEGORY = "????????????";
        String TITLE = "??????";
        String DESCRIPTION = "??????";
        String VIEWS = "???_???";
        String LIKES = "?????????_???";
        String SHARES = "??????_???";
//        String PERMISSION_TO_COMMENT = "??????_??????";
//        String PERMISSION_TO_SHARE = "??????_??????";
//        String TARGET = "??????_??????";

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String number = item.getString(NUMBER);
                String writer = item.getString(WRITER);
                String title = item.getString(TITLE);
                String description = item.getString(DESCRIPTION);
                String likes = item.getString(LIKES);
                String views = item.getString(VIEWS);
                String shares = item.getString(SHARES);
                String category = item.getString(CATEGORY);
                String date = item.getString(DATE);

                postDataList.add(new MyPagePostData(number, writer,title,description,likes, views, shares, category, date));
            }
        } catch (JSONException e) { }
    }

    public void loadPosts() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, MY_PAGE_POST_READ_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InitializePostData(jsonObject);

                    ListView listView = (ListView) view.findViewById(R.id.my_page_lv_post);
                    final MyPagePostAdapter postAdapter = new MyPagePostAdapter(view.getContext(), postDataList);

                    listView.setAdapter(postAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id){
                            Intent intent = new Intent(v.getContext(), InnerPostActivity.class);
                            intent.putExtra("number", postAdapter.getItem(position).getNumber());
                            intent.putExtra("owner", postAdapter.getItem(position).getWriter());
                            intent.putExtra("user_id", userId);
                            startActivity(intent);
                        }
                    });
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
                params.put("writer", userId);
                return params;
            }
        };
        queue.add(strRequest);
    }

    public void loadProfileDescription() {
        TextView tv_nickname = (TextView) view.findViewById(R.id.activity_my_page_nickname);
        TextView tv_introduce = (TextView) view.findViewById(R.id.activity_my_page_introduce);

        StringRequest nicknameRequest = new StringRequest(Request.Method.POST, PROFILE_READ_NICKNAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String TAG_JSON = "?????????";
                    String NICKNAME = "?????????";
                    String INTRODUCE = "????????????";
                    String FOLLOWER = "?????????";
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                        JSONObject item = jsonArray.getJSONObject(0);
                        String nickname = item.getString(NICKNAME);
                        introduce = item.getString(INTRODUCE);
                        String follower = item.getString(FOLLOWER);

                        tv_nickname.setText(nickname);
                        tv_introduce.setText(introduce);
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
                params.put("id", userId);
                return params;
            }
        };
        queue.add(nicknameRequest);
    }
}