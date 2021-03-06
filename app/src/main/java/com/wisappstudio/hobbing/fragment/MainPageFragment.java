package com.wisappstudio.hobbing.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.wisappstudio.hobbing.activity.InnerPostActivity;
import com.wisappstudio.hobbing.activity.WritePostActivity;
import com.wisappstudio.hobbing.adapter.PostAdapter;
import com.wisappstudio.hobbing.data.PostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.wisappstudio.hobbing.data.ServerData.POST_READ_URL;

public class MainPageFragment extends Fragment {
    private RequestQueue queue;
    private View view;
    ArrayList<PostData> postDataList;
    private String userId;
    public static Fragment fragment;

    public MainPageFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.activity_main_page, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fragment = MainPageFragment.this;

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WritePostActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });
        this.loadPosts();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void loadPosts() {
        queue = Volley.newRequestQueue(view.getContext());
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, POST_READ_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                InitializePostData(response);

                ListView listView = (ListView) view.findViewById(R.id.main_page_lv_post);
                final PostAdapter postAdapter = new PostAdapter(view.getContext(), postDataList);

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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LoadERR", error.getMessage());
            }
        });
        jsonRequest.setTag("LoadERR");
        queue.add(jsonRequest);
    }

    public void InitializePostData(JSONObject jsonObject)
    {
        postDataList = new ArrayList<PostData>();
        String TAG_JSON = "?????????_??????";
        String NUMBER = "??????";
        String WRITER = "?????????";
        String CATEGORY = "????????????";
        String TITLE = "??????";
        String DESCRIPTION = "??????";
        String VIEWS = "???_???";
        String LIKES = "?????????_???";
        String SHARES = "??????_???";
        String DATE = "????????????";
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

                postDataList.add(new PostData(number, writer,title,description,likes, views, shares,category, date));
            }
        } catch (JSONException e) {
            Log.d("LoadERR", e.toString());
        };
    }
}