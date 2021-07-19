package com.example.hobbing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class LoadingActivity extends AppCompatActivity {
    private int LoadingTime = 3000; // 밀리세컨드 단위 ( 1000(milli second) = 1(second)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }, LoadingTime);
    }

    @Override
    protected void onPause() {
        super.onPause(); finish();
    }

}
