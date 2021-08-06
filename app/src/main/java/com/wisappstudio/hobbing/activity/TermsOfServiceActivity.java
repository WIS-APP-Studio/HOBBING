package com.wisappstudio.hobbing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hobbing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class TermsOfServiceActivity extends AppCompatActivity {
    private Button agree, disagree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);

        agree = findViewById(R.id.agree);
        disagree = findViewById(R.id.disagree);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TermsOfServiceActivity.this, "이용약관 동의 완료", Toast.LENGTH_SHORT).show();

                // 이동할 레이아웃 설정(어떤 레이아웃과 연결할까요?)
            }
        });
        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TermsOfServiceActivity.this, "이용약관을 동의하지 않으면 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show();

                return;
            }
        });
    }
}
