package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_IS_LIKE_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_LIKES_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_LIKE_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_NOT_LIKE_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_SEND_COMMENT_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_IMAGE_DIRECTORY;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_READ_NICKNAME_URL;

public class InnerPostActivity extends AppCompatActivity {
    ArrayList<InnerPostData> innerPostDataList;
    ArrayList<CommentData> commentDataList;
    String userId;
    String postNumber;

    RequestQueue queue;
    boolean isLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_post);

        userId = getIntent().getStringExtra("user_id");
        postNumber = getIntent().getStringExtra("number");

        queue = Volley.newRequestQueue(this);

        clickLikeButton(); // ????????? ?????? ??????
        clickSendComment(); // ?????? ?????? ?????? ??????
        loadCommentUserProfile(); // ?????? ????????? ??????

        queue.add(loadPostContent()); // ????????? ?????? ??????
        queue.add(loadPostImages()); // ????????? ????????? ??????
        queue.add(loadPostNumberOfLikes()); // ????????? ????????? ??? ??????

        queue.add(loadComments()); // ?????? ????????? ??????
        queue.add(loadUserLikes()); // ????????? ????????? ????????? ?????? ?????? ??????
    }

    private void clickSendComment() {
        // ?????? ??????
        ImageView sendComment = (ImageView) findViewById(R.id.activity_inner_post_send_comment);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String userId = intent.getStringExtra("user_id");
                String postNumber = intent.getStringExtra("number");

                EditText et_comment = (EditText) findViewById(R.id.activity_inner_post_comment_description);
                String commentDescription = et_comment.getText().toString();

                if(commentDescription.length() == 0) {
                    Toast.makeText(InnerPostActivity.this, "?????? ????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                    return;
                }
                et_comment.setText("");

                StringRequest sendCommentRequest = new StringRequest(Request.Method.POST, INNER_POST_SEND_COMMENT_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(InnerPostActivity.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        queue.add(loadComments());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InnerPostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    private void clickLikeButton() {
        // ????????? ????????? ??????
        Button likeButton = (Button) findViewById(R.id.activity_inner_post_like_button);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLike) {
                    StringRequest isNotLikeRequest = new StringRequest(Request.Method.POST, INNER_POST_NOT_LIKE_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(InnerPostActivity.this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            ImageView isLikeImage = findViewById(R.id.activity_inner_post_like);
                            Button isLikeButton = findViewById(R.id.activity_inner_post_like_button);
                            TextView likes = findViewById(R.id.activity_inner_post_likes);

                            int temp = Integer.valueOf(likes.getText().toString());
                            likes.setText(String.valueOf(--temp));

                            isLikeButton.setText("?????????");
                            isLikeImage.setImageResource(R.drawable.like);
                            isLike = false;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_id", getIntent().getStringExtra("user_id"));
                            params.put("number", getIntent().getStringExtra("number"));
                            return params;
                        }
                    };
                    queue.add(isNotLikeRequest);
                }
                else {
                    StringRequest isLikeRequest = new StringRequest(Request.Method.POST, INNER_POST_LIKE_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ImageView isLikeImage = findViewById(R.id.activity_inner_post_like);
                            Button isLikeButton = findViewById(R.id.activity_inner_post_like_button);
                            TextView likes = findViewById(R.id.activity_inner_post_likes);

                            int temp = Integer.valueOf(likes.getText().toString());
                            likes.setText(String.valueOf(++temp));

                            isLikeButton.setText("????????? ??????");

                            isLikeImage.setImageResource(R.drawable.like2);
                            isLike = true;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_id", getIntent().getStringExtra("user_id"));
                            params.put("number", getIntent().getStringExtra("number"));
                            return params;
                        }
                    };
                    queue.add(isLikeRequest);
                }
            }
        });
    }

    private void loadCommentUserProfile() {
        ImageView userProfile = (ImageView) findViewById(R.id.list_inner_post_user_profile);
        Glide.with(getApplicationContext())
                .load(PROFILE_IMAGE_DIRECTORY +userId+".png") // ????????? ??????
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(userProfile);
        userProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
        userProfile.setBackground(new ShapeDrawable(new OvalShape()));
        userProfile.setClipToOutline(true);
    }

    private StringRequest loadPostContent() {
        // ????????? ?????? ??????
        StringRequest postContentRequest = new StringRequest(Request.Method.POST, INNER_POST_READ_URL, new Response.Listener<String>() {
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
        return postContentRequest;
    }

    private StringRequest loadPostImages() {
        StringRequest postImagesRequest = new StringRequest(Request.Method.POST, INNER_POST_IMAGE_READ_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    RecyclerView recyclerView = findViewById(R.id.activity_inner_post_image);

                    InitializePostImage(jsonObject);
                    InnerPostAdapter postAdapter = new InnerPostAdapter(innerPostDataList);

                    recyclerView.setAdapter(postAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                } catch (JSONException e) { }
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
        return postImagesRequest;
    }

    private StringRequest loadPostNumberOfLikes() {
        // ????????? ????????? ??????
        StringRequest postNumberOfLikesRequest = new StringRequest(Request.Method.POST, INNER_POST_LIKES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InitializePostLikes(jsonObject);
                } catch (JSONException e) { }
            }
            private void InitializePostLikes(JSONObject jsonObject) {
                String TAG_JSON = "?????????_???";
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    TextView likes = (TextView) findViewById(R.id.activity_inner_post_likes);
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
                String number = getIntent().getStringExtra("number");
                params.put("number", number);
                return params;
            }
        };
        return postNumberOfLikesRequest;
    }

    private StringRequest loadComments() {
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
        return commentRequest;
    }

    private StringRequest loadUserLikes() {
        // ????????? ????????? ?????? ??????
        StringRequest isLikePostRequest = new StringRequest(Request.Method.POST, INNER_POST_IS_LIKE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InitializePostLike(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            private void InitializePostLike(JSONObject jsonObject) {
                String TAG_JSON = "?????????";
                String USER = "?????????";
                String POST = "?????????";
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String user = item.getString(USER);
                    String post = item.getString(POST);

                    if(userId.equals(user) && postNumber.equals(post)) {
                        ImageView isLikeImage = findViewById(R.id.activity_inner_post_like);
                        Button isLikeButton = findViewById(R.id.activity_inner_post_like_button);

                        isLikeButton.setText("????????? ??????");
                        isLikeImage.setImageResource(R.drawable.like2);
                        isLike = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", getIntent().getStringExtra("user_id"));
                params.put("number", getIntent().getStringExtra("number"));
                return params;
            }
        };
        return isLikePostRequest;
    }


    public void InitializeInnerPostData(JSONObject jsonObject) {
        String TAG_JSON = "??????_?????????_??????";
//        String NUM = "??????";
        String WRITER = "?????????";
//        String CATEGORY = "????????????";
        String TITLE = "??????";
        String DESCRIPTION = "??????";
        String VIEWS = "???_???";
        String LIKES = "?????????_???";
        String SHARES = "??????_???";
//        String PERMISSION_TO_COMMENT = "??????_??????";
//        String PERMISSION_TO_SHARE = "??????_??????";
//        String DATE = "????????????";
//        String TARGET = "??????_??????";

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

            ImageView ownerProfile = (ImageView) findViewById(R.id.activity_inner_post_owner_profile);
            Glide.with(getApplicationContext())
                    .load(PROFILE_IMAGE_DIRECTORY +writer+".png") // ????????? ??????
                    .apply(new RequestOptions()
                            .signature(new ObjectKey("signature string"))
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    )
                    .into(ownerProfile);
            ownerProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ownerProfile.setBackground(new ShapeDrawable(new OvalShape()));
            ownerProfile.setClipToOutline(true);

            ImageView select = (ImageView) findViewById(R.id.activity_inner_post_select);
            if(writer.equals(userId)) {
                select.setImageResource(R.drawable.edit);
                select.setPadding(5, 5,5, 5);

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(InnerPostActivity.this, UpdatePostActivity.class);
                        intent.putExtra("post_number",postNumber);
                        startActivity(intent);
                    }
                });
            } else {
                select.setImageResource(R.drawable.alert);

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(InnerPostActivity.this, "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest nicknameRequest = new StringRequest(Request.Method.POST, PROFILE_READ_NICKNAME_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String TAG_JSON = "?????????";
                        String NICKNAME = "?????????";
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                            JSONObject item = jsonArray.getJSONObject(0);
                            String nickname = item.getString(NICKNAME);
                            ownerView.setText(nickname+"?????? ?????????");

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
                    params.put("id", writer);
                    return params;
                }
            };
            queue.add(nicknameRequest);

            descriptionView.setText(description);
            titleView.setText(title);

        } catch (JSONException e) {
        }
    }

    public void InitializePostImage(JSONObject jsonObject) {
        innerPostDataList = new ArrayList<InnerPostData>();
        String TAG_JSON = "??????_?????????_??????";
        String NUMBER = "??????";
        String IMAGE = "??????";

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
        String TAG_JSON = "??????_?????????_??????";
        String POST_NUMBER = "??????";
        String COMMENT_NUMBER = "??????_??????";
        String WRITER = "?????????";
        String DESCRIPTION = "??????";
        String DATE ="????????????";

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
