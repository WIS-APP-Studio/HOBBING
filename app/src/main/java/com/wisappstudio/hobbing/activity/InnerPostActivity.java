package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.adapter.InnerPostAdapter;
import com.wisappstudio.hobbing.data.InnerPostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_READ_URL;

public class InnerPostActivity extends AppCompatActivity {
    ArrayList<InnerPostData> innerPostDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_post);

        RequestQueue queue = Volley.newRequestQueue(this);

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
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
        queue.add(strRequest);
    }

    public void InitializeInnerPostData(JSONObject jsonObject) {
        innerPostDataList = new ArrayList<InnerPostData>();
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

            TextView ownerView = findViewById(R.id.activity_inner_post_owner);
            TextView descriptionView = findViewById(R.id.activity_inner_post_description);
            TextView titleView = findViewById(R.id.activity_inner_post_title);

            ownerView.setText(writer + "님의 게시물");
            descriptionView.setText(description);
            titleView.setText(description);

        } catch (JSONException e) {
            Log.d("LoadERR-JSONException", e.toString());
        }
        ;
    }
}
