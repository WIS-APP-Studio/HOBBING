package com.wisappstudio.hobbing.fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.activity.InnerPostActivity;
import com.wisappstudio.hobbing.adapter.MyPagePostAdapter;
import com.wisappstudio.hobbing.adapter.PostAdapter;
import com.wisappstudio.hobbing.data.MyPagePostData;
import com.wisappstudio.hobbing.data.PostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.IMAGE_DIRECTORY_URL;
import static com.wisappstudio.hobbing.data.ServerData.MY_PAGE_POST_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.POST_READ_URL;

public class MyPageFragment extends Fragment {
    private String userId;
    private View view;
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

        TextView nickname = (TextView) view.findViewById(R.id.activity_my_page_nickname);
        TextView id = (TextView) view.findViewById(R.id.activity_my_page_id);
        ImageView profile_image = (ImageView) view.findViewById(R.id.activity_my_page_image);

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
        OvalShape ovalShape = new OvalShape();
        shapeDrawable.setShape(new OvalShape());

        profile_image.setBackground(shapeDrawable);
        profile_image.setClipToOutline(true);

        // 상단 아이디 및 닉네임 (닉네임은 추후 작업)
        nickname.setText(userId);
        id.setText("@"+userId);

        queue = Volley.newRequestQueue(view.getContext());

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
        } catch (JSONException e) {
            Log.d("LoadERR", e.toString());
        };
    }

}