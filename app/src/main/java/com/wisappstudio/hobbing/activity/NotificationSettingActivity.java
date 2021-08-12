package com.wisappstudio.hobbing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class NotificationSettingActivity extends Activity implements AdapterView.OnItemClickListener{

    static final String[] LIST_MENU = {"게시글 알림", "팔로잉 및 팔로워 알림", "활동기록 설정", "이메일 알림"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting) ;

        Switch mailSwitch = (Switch) findViewById(R.id.activity_mail_switch);
        TextView back = (TextView) findViewById(R.id.activity_my_info_cancel);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mailSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 스위치 체크 이벤트(이메일 알림) 등록(아마도 백엔드 작업이 필요하겠죠? ...ㅎㅎ)
            }
        });

        ArrayAdapter Adapter = new ArrayAdapter(NotificationSettingActivity.this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(Adapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                // '게시글 알림' 연결
                Intent intent = new Intent(this, PostNotificationSettingActivity.class);
                startActivity(intent);
                break;
            }
            case 1: {
                // '팔로잉 및 팔로워' 연결
                Intent intent = new Intent(this, FollowerNotificationSettingActivity.class);
                startActivity(intent);
                break;
            }
            case 2: {
                // '활동기록 설정' 연결
                Intent intent = new Intent(this, ActionLogSettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
