package com.wisappstudio.hobbing.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class MyInfoActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"아이디 변경", "비밀번호 변경", "이메일 변경"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info) ;

        ArrayAdapter Adapter = new ArrayAdapter(MyInfoActivity.this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(Adapter) ;
    }
}