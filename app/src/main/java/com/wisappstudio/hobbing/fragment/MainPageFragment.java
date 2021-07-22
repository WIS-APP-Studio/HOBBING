package com.wisappstudio.hobbing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wisappstudio.hobbing.activity.WritePostActivity;
import com.wisappstudio.hobbing.adapter.WritePostAdapter;
import com.wisappstudio.hobbing.data.WritePostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.wisappstudio.hobbing.data.ServerData.POST_READ_URL;

public class MainPageFragment extends Fragment {
    private RequestQueue queue;
    private View view;
    ArrayList<WritePostData> postDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.activity_main_page, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(view.getContext(), WritePostActivity.class);
                startActivity(intent);
            }
        });

        queue = Volley.newRequestQueue(view.getContext());
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, POST_READ_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                InitializePostData(response);

                ListView listView = (ListView) view.findViewById(R.id.main_page_lv_post);
                final WritePostAdapter postAdapter = new WritePostAdapter(view.getContext(), postDataList);

                listView.setAdapter(postAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id){
                        Toast.makeText(v.getContext(),
                                postAdapter.getItem(position).getTitle(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LoadERR", error.getMessage());
            }
        });
        jsonRequest.setTag("LoadERR");
        queue.add(jsonRequest);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void InitializePostData(JSONObject jsonObject)
    {
        postDataList = new ArrayList<WritePostData>();
        String TAG_JSON = "게시물_정보";
//        String NUM = "번호";
        String WRITER = "작성자";
//        String CATEGORY = "카테고리";
        String TITLE = "제목";
        String DESCRIPTION = "내용";
//        String COUNT_OF_VIEW = "뷰_수";
//        String LIKE = "좋아요";
//        String PERMISSION_TO_COMMENT = "댓글_허용";
//        String PERMISSION_TO_SHARE = "공유_허용";
//        String DATE = "게시일자";
//        String TARGET = "공개_대상";

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String writer = item.getString(WRITER);
                String title = item.getString(TITLE);
                String description = item.getString(DESCRIPTION);

                postDataList.add(new WritePostData(writer,title,description));
            }
        } catch (JSONException e) {
            Log.d("LoadERR", e.toString());
        };
    }
}