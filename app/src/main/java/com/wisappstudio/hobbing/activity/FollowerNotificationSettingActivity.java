package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class FollowerNotificationSettingActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"팔로워 알림", "팔로우 취소 알림"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_notification_setting);

        Switch followerSwitch = (Switch) findViewById(R.id.activity_follower_switch);
        Switch followerCancelSwitch = (Switch) findViewById(R.id.activity_follower_cancel_switch);

        TextView back = (TextView) findViewById(R.id.activity_my_info_cancel);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        followerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 어... 제가 찾아보니까 푸시 알림를 하기 위해서는 Notification Builder 기능이 구현이 되어있어야 하더라구요..
            }
        });

        followerCancelSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 저희 만든거 보면 아직 Notification Builder 기능이 들어가지 않아서... 찡긋2(｡•̀ᴗ-)
            }
        });


        ArrayAdapter Adapter = new ArrayAdapter(FollowerNotificationSettingActivity.this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(Adapter);
    }
}

