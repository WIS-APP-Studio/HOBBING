package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbing.R;

import java.util.HashMap;
import java.util.Map;

import static com.wisappstudio.hobbing.data.ServerData.PROFILE_UPDATE_URL;

public class WriteProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_profile);

        clickConfirm(); // 닉네임 변경
    }
    private void clickConfirm() {
        Button confirm = findViewById(R.id.activity_write_profile_confirm);
        EditText et_nickname = findViewById(R.id.activity_write_profile_nickname);
        EditText et_introduce = findViewById(R.id.activity_write_profile_introduce);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_nickname.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringRequest updateProfile = new StringRequest(Request.Method.POST, PROFILE_UPDATE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(WriteProfileActivity.this, MainActivity.class);
                        intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", getIntent().getStringExtra("user_id"));
                        params.put("nickname", et_nickname.getText().toString());
                        params.put("introduce", et_introduce.getText().toString());
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(updateProfile);
            }
        });
    }
}
