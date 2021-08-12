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

public class ActionLogSettingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    static final String[] LIST_MENU = {"검색기록 삭제", "검색기록 저장"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_log_setting);

        Switch logSwitch = (Switch) findViewById(R.id.activity_log_switch);

        TextView back = (TextView) findViewById(R.id.activity_my_info_cancel);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 스위치 체크 이벤트(검색기록 저장) 이것도 백엔드 작업 ...ㅎㅎ 파이팅~!
            }
        });


        ArrayAdapter Adapter = new ArrayAdapter(ActionLogSettingActivity.this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(Adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                // 검색 기록 삭제로 연결! 검색기록 삭제 맡으신분 파이팅입니다!
                // Intent intent = new Intent(this, 뭐시기~Activity.class);
                // startActivity(intent);
                break;
            }
        }
    }
}


