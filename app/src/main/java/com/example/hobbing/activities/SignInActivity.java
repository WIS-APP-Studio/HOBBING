package com.example.hobbing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class SignInActivity extends AppCompatActivity {
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.btnClick();
    }

    protected void btnClick() {
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                // 이 부분에 로그인 기능을 적용


                // 로그인 성공 시 아래 함수 두 줄을 실행
                startActivity(intent);
                finish();
            }
        });
    }
}
