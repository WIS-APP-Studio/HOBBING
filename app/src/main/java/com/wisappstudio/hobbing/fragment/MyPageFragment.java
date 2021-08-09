package com.wisappstudio.hobbing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.wisappstudio.hobbing.data.ServerData.IMAGE_DIRECTORY_URL;
import static com.wisappstudio.hobbing.data.ServerData.MY_PAGE_POST_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_READ_NICKNAME_URL;

public class MyPageFragment extends Fragment {
    private String userId;
    private View view;
    private String introduce;
    ArrayList<MyPagePostData> postDataList;
    private RequestQueue queue;


    public MyPageFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.activity_my_page, container, false);

        TextView tv_nickname = (TextView) view.findViewById(R.id.activity_my_page_nickname);
        TextView tv_introduce = (TextView) view.findViewById(R.id.activity_my_page_introduce);
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

        // 상단 마이 프로필 사진
        Glide.with(view.getContext())
                .load(IMAGE_DIRECTORY_URL+userId+".png") // 임시로 로드
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(profile_image);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setColor(view.getContext().getColor(R.color.signature));
        shapeDrawable.setShape(new OvalShape());

        profile_image.setBackground(shapeDrawable);
        profile_image.setClipToOutline(true);

        id.setText(userId);

        queue = Volley.newRequestQueue(view.getContext());
        StringRequest nicknameRequest = new StringRequest(Request.Method.POST, PROFILE_READ_NICKNAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String TAG_JSON = "프로필";
                    String NICKNAME = "닉네임";
                    String INTRODUCE = "자기소개";
                    String FOLLOWER = "팔로워";
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
        queue.add(nicknameRequest);
        queue.add(strRequest);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void InitializePostData(JSONObject jsonObject)
    {
        postDataList = new ArrayList<MyPagePostData>();
        String TAG_JSON = "게시물_정보";
        String NUMBER = "번호";
        String WRITER = "작성자";
        String DATE = "게시일자";
        String CATEGORY = "카테고리";
        String TITLE = "제목";
        String DESCRIPTION = "내용";
        String VIEWS = "뷰_수";
        String LIKES = "좋아요_수";
        String SHARES = "공유_수";
//        String PERMISSION_TO_COMMENT = "댓글_허용";
//        String PERMISSION_TO_SHARE = "공유_허용";
//        String TARGET = "공개_대상";

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
}