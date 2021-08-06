package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

        Intent intent = getIntent();
        String where = intent.getStringExtra("where");

        if(where != null) {
            if(where.equals("Service")) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.activity_terms_of_service_confirm);
            layout.setVisibility(View.INVISIBLE);
            }
        }


        agree = findViewById(R.id.agree);
        disagree = findViewById(R.id.disagree);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TermsOfServiceActivity.this, "이용약관 동의 완료", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TermsOfServiceActivity.this, SignUpActivity.class);
                startActivity(intent);
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
