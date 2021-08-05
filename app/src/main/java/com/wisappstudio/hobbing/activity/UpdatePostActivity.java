package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.hobbing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.POST_IMAGE_DIRECTORY;

public class UpdatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        Intent intent = getIntent();
        String postNumber = intent.getStringExtra("post_number");

        ImageView image1 = (ImageView) findViewById(R.id.activity_update_post_image1);
        ImageView image2 = (ImageView) findViewById(R.id.activity_update_post_image2);
        ImageView image3 = (ImageView) findViewById(R.id.activity_update_post_image3);

        Glide.with(getApplicationContext())
                .load(POST_IMAGE_DIRECTORY+postNumber+"/1.jpeg")
                .into(image1);

        Glide.with(getApplicationContext())
                .load(POST_IMAGE_DIRECTORY+postNumber+"/2.jpeg")
                .into(image2);

        Glide.with(this)
                .load(POST_IMAGE_DIRECTORY+postNumber+"/3.jpeg")
                .into(image3);

        // 게시물 내부 내용 로드
        StringRequest postRequest = new StringRequest(Request.Method.POST, INNER_POST_READ_URL, new Response.Listener<String>() {
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
                String number = getIntent().getStringExtra("post_number");
                params.put("number", number);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
    }

    public void InitializeInnerPostData(JSONObject jsonObject) {
        String TAG_JSON = "내부_게시물_정보";
//        String WRITER = "작성자";
        String CATEGORY = "카테고리";
        String TITLE = "제목";
        String DESCRIPTION = "내용";
//        String PERMISSION_TO_COMMENT = "댓글_허용";
//        String PERMISSION_TO_SHARE = "공유_허용";
//        String DATE = "게시일자";
//        String TARGET = "공개_대상";

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            JSONObject item = jsonArray.getJSONObject(0);
            String title = item.getString(TITLE);
            String description = item.getString(DESCRIPTION);
            String category = item.getString(CATEGORY);

            EditText et_title = (EditText) findViewById(R.id.activity_update_post_title);
            EditText et_description = (EditText) findViewById(R.id.activity_update_post_description);
            EditText et_category = (EditText) findViewById(R.id.activity_update_post_category);
            EditText et_hashtag = (EditText) findViewById(R.id.activity_update_post_hashtag);

            et_title.setText(title);
            et_description.setText(description);
            et_category.setText(category);

        } catch (JSONException e) { }
    }
}
