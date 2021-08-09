package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
import com.wisappstudio.hobbing.dialog.ChangeProfileDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.IMAGE_DIRECTORY_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_READ_NICKNAME_URL;

public class ProfileSettingActivity extends Activity implements AdapterView.OnItemClickListener {

    static final String[] LIST_MENU = {"계정 정보 수정", "알림 설정", "활동 기록", "로그아웃", "탈퇴"} ;

    Activity myInfoActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        // 받을 내용
        Intent intent = getIntent();
        String USER_ID = intent.getStringExtra("user_id");
        String USER_NAME = intent.getStringExtra("user_id");
        // 받을 내용

        TextView userId = (TextView) findViewById(R.id.activity_profile_setting_userid);
        TextView userName = (TextView) findViewById(R.id.activity_profile_setting_username);
        ImageView userImage = (ImageView) findViewById(R.id.activity_profile_setting_image);
        TextView back = (TextView) findViewById(R.id.activity_profile_setting_cancel);
        ImageView changeNickName = (ImageView) findViewById(R.id.activity_profile_setting_change_profile);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changeNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeProfileDialog customDialog = new ChangeProfileDialog(ProfileSettingActivity.this, USER_ID);
                customDialog.callFunction(userName);
            }
        });

        userId.setText(USER_ID);


        userName.setText(USER_NAME);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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

                        userName.setText(nickname);
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
                params.put("id", USER_ID);
                return params;
            }
        };

        queue.add(nicknameRequest);

        // 상단 마이 프로필 사진
        Glide.with(getApplicationContext())
                .load(IMAGE_DIRECTORY_URL+USER_ID+".png") // 임시로 로드
                .apply(new RequestOptions()
                        .signature(new ObjectKey("signature string"))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                )
                .into(userImage);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setColor(getApplicationContext().getColor(R.color.signature)); // 이미지 백그라운드 컬러
        shapeDrawable.setShape(new OvalShape());

        userImage.setBackground(shapeDrawable);
        userImage.setClipToOutline(true);
        // 상단 마이 프로필 사진

        ArrayAdapter Adapter = new ArrayAdapter(ProfileSettingActivity.this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.listview1) ;
        // 리스트 뷰 OnItemClickListener 관련 자료 : https://newgenerationkorea.wordpress.com/2015/07/12/listview-%EA%B5%AC%EC%84%B1%ED%95%98%EA%B8%B0-1-arrayadapter-%EC%9D%B4%EC%9A%A9%ED%95%98%EA%B8%B0/
        listview.setAdapter(Adapter) ;
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0 : {
                Intent intent = new Intent(this, InfoChangeActivity.class);
                startActivity(intent);
                break;
            }
            case 2 : {
                Intent intent = new Intent(this, UserLogActivity.class);
                startActivity(intent);
                break;
            }
            case 3 : {
                MainActivity mainActivity = (MainActivity) MainActivity.activity;
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
                finish();
                mainActivity.finish();
                break;
            }
            case 4 : {
                Intent intent = new Intent(this, DeleteAccountActivity.class);
                String USER_ID = getIntent().getStringExtra("user_id");
                intent.putExtra("user_id", USER_ID);
                startActivity(intent);
                finish();
            }
        }
    }
}
