package com.wisappstudio.hobbing.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class ProfileSettingActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"내 정보", "알림 설정", "활동 기록", "로그아웃", "탈퇴"} ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        ArrayAdapter Adapter = new ArrayAdapter(ProfileSettingActivity.this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(Adapter) ;
    }
}
