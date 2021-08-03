package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.wisappstudio.hobbing.adapter.CommentAdapter;
import com.wisappstudio.hobbing.adapter.InnerPostAdapter;
import com.wisappstudio.hobbing.data.CommentData;
import com.wisappstudio.hobbing.data.InnerPostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_COMMENT_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_IMAGE_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_SEND_COMMENT_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;

public class InnerPostActivity extends AppCompatActivity {
    ArrayList<InnerPostData> innerPostDataList;
    ArrayList<CommentData> commentDataList;
    String userId;
    String postNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_post);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("user_id");
        String postNumber = intent.getStringExtra("number");
        this.userId = userId;
        this.postNumber = postNumber;

        // 댓글 사용자 프로필 사진
        ImageView userProfile = (ImageView) findViewById(R.id.list_inner_post_user_profile);
        Glide.with(getApplicationContext())
                .load(PROFILE_IMAGE_DIRECTORY +userId+".png") // 임시로 로드
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(userProfile);

        RequestQueue queue = Volley.newRequestQueue(this);


        // 게시물 내부 내용 로드
        StringRequest strRequest = new StringRequest(Request.Method.POST, INNER_POST_READ_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InitializeInnerPostData(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String number = getIntent().getStringExtra("number");
                params.put("number", number);
                return params;
            }
        };
        // 게시물 내부 사진 로드
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INNER_POST_IMAGE_READ_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    RecyclerView recyclerView = findViewById(R.id.activity_inner_post_image);

                    InitializePostImage(jsonObject);
                    InnerPostAdapter postAdapter = new InnerPostAdapter(innerPostDataList);

                    recyclerView.setAdapter(postAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

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
                String number = getIntent().getStringExtra("number");
                params.put("number", number);
                return params;
            }
        };
        // 게시물 댓글 로드
        StringRequest commentRequest = new StringRequest(Request.Method.POST, INNER_POST_COMMENT_READ_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InitializeCommentData(jsonObject);
                    ListView commentListView = (ListView) findViewById(R.id.activity_inner_post_comment);

                    final CommentAdapter adapter = new CommentAdapter(getApplicationContext(), commentDataList);

                    commentListView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(commentListView);
                    //
                } catch (JSONException e) {
                    Log.d("COMMENTERR", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String number = getIntent().getStringExtra("number");
                params.put("number", number);
                return params;
            }
        };

        // 댓글 작성 및 전송
        ImageView sendComment = (ImageView) findViewById(R.id.activity_inner_post_send_comment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String userId = intent.getStringExtra("user_id");
                String postNumber = intent.getStringExtra("number");

                EditText writtenComment = (EditText) findViewById(R.id.activity_inner_post_comment_description);
                String commentDescription = writtenComment.getText().toString();

                if(commentDescription.length() == 0) {
                    Toast.makeText(InnerPostActivity.this, "먼저 댓글을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                writtenComment.setText("");

                StringRequest sendCommentRequest = new StringRequest(Request.Method.POST, INNER_POST_SEND_COMMENT_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(InnerPostActivity.this, "댓글을 작성했습니다.", Toast.LENGTH_SHORT).show();
                        queue.add(commentRequest);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InnerPostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        // 댓글 작성한 내용 전송 중 오류로 서비스가 되지 않은 경우를 보여줘야함 Exception Process
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", userId);
                        params.put("post_number", postNumber);
                        params.put("comment", commentDescription);
                        return params;
                    }
                };
                queue.add(sendCommentRequest);
            }
        });

        queue.add(strRequest);
        queue.add(stringRequest);
        queue.add(commentRequest);
    }

    public void InitializeInnerPostData(JSONObject jsonObject) {
        String TAG_JSON = "내부_게시물_정보";
//        String NUM = "번호";
        String WRITER = "작성자";
//        String CATEGORY = "카테고리";
        String TITLE = "제목";
        String DESCRIPTION = "내용";
        String VIEWS = "뷰_수";
        String LIKES = "좋아요_수";
        String SHARES = "공유_수";
//        String PERMISSION_TO_COMMENT = "댓글_허용";
//        String PERMISSION_TO_SHARE = "공유_허용";
//        String DATE = "게시일자";
//        String TARGET = "공개_대상";

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            JSONObject item = jsonArray.getJSONObject(0);
            String writer = item.getString(WRITER);
            String title = item.getString(TITLE);
            String description = item.getString(DESCRIPTION);
            String likes = item.getString(LIKES);
            String views = item.getString(VIEWS);
            String shares = item.getString(SHARES);

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_inner_post_image);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);

            TextView ownerView = findViewById(R.id.activity_inner_post_owner);
            TextView descriptionView = findViewById(R.id.activity_inner_post_description);
            TextView titleView = findViewById(R.id.activity_inner_post_title);
            ImageView select = (ImageView) findViewById(R.id.activity_inner_post_select);

            ownerView.setText(writer + "님의 게시물");
            descriptionView.setText(description);
            titleView.setText(title);

        } catch (JSONException e) {
        }
    }

    public void InitializePostImage(JSONObject jsonObject) {
        innerPostDataList = new ArrayList<InnerPostData>();
        String TAG_JSON = "내부_게시물_사진";
        String NUMBER = "번호";
        String IMAGE = "사진";

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String number = item.getString(NUMBER);
                String image = item.getString(IMAGE);

                innerPostDataList.add(new InnerPostData(number, image));
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    public void InitializeCommentData(JSONObject jsonObject) {
        commentDataList = new ArrayList<CommentData>();
        String TAG_JSON = "내부_게시물_댓글";
        String POST_NUMBER = "번호";
        String COMMENT_NUMBER = "댓글_번호";
        String WRITER = "작성자";
        String DESCRIPTION = "내용";
        String DATE ="작성일자";

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String postNumber = item.getString(POST_NUMBER);
                String commentNumber = item.getString(COMMENT_NUMBER);
                String writer = item.getString(WRITER);
                String description = item.getString(DESCRIPTION);
                String date = item.getString(DATE);

                commentDataList.add(new CommentData(postNumber, commentNumber, writer, description, date));
            }

        } catch (JSONException e) { e.printStackTrace(); }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
