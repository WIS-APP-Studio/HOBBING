package com.wisappstudio.hobbing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class PostNotificationSettingActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"게시글 댓글 알림", "좋아요 알림", "공유 알림"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_notification_setting);

        Switch commentSwitch = (Switch) findViewById(R.id.activity_comment_switch);
        Switch likeSwitch = (Switch) findViewById(R.id.activity_like_switch);
        Switch shareSwitch = (Switch) findViewById(R.id.activity_share_switch);

        TextView back = (TextView) findViewById(R.id.activity_my_info_cancel);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commentSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 어... 제가 찾아보니까 푸시 알림를 하기 위해서는
            }
        });

        likeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notification Builder 기능이 구현이 되어있어야 하더라구요..
            }
        });

        shareSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 저희 만든거 보면 아직 Notification Builder 기능이 들어가지 않아서... 찡긋(๑˃̵ᴗ˂̵)
            }
        });

        ArrayAdapter Adapter = new ArrayAdapter(PostNotificationSettingActivity.this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(Adapter);
    }
}

