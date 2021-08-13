package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class ServiceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static final String[] LIST_MENU = {"도움말", "공지사항", "이용약관", "개인정보처리방침", "버전 정보", "문의"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_page);


        ArrayAdapter Adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.activity_service_page_list);
        listview.setAdapter(Adapter) ;
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0 : { // 도움말
                break;
            }
            case 1 : { // 공지사항
                Intent intent = new Intent(view.getContext(), NoticeActivity.class);
                startActivity(intent);
                break;
            }
            case 2 : { // 이용약관
                Intent intent = new Intent(view.getContext(), BackTermsOfServiceActivity.class);
                startActivity(intent);
                break;
            }
            case 3 : { // 개인정보처리방침
                break;
            }
            case 4 : { // 버전 정보
                Intent intent = new Intent(view.getContext(), VersionActivity.class);
                startActivity(intent);
                break;
            }
            case 5 : { // 문의
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
