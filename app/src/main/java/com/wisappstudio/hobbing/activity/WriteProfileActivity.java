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


        Button confirm = findViewById(R.id.activity_write_profile_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            EditText et_nickname = findViewById(R.id.activity_write_profile_nickname);
            EditText et_introduce = findViewById(R.id.activity_write_profile_introduce);
            @Override
            public void onClick(View v) {

                if(et_nickname.getText().toString().equals("")) {
                    Toast.makeText(WriteProfileActivity.this, "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringRequest updateProfile = new StringRequest(Request.Method.POST, PROFILE_UPDATE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VOLLEYLOG", response);
                        Intent intent = new Intent(WriteProfileActivity.this, MainActivity.class);
                        intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEYLOG", error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String NICKNAME = et_nickname.getText().toString();
                        String INTRODUCE = et_introduce.getText().toString();
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", getIntent().getStringExtra("user_id"));
                        params.put("nickname", NICKNAME);
                        params.put("introduce", INTRODUCE);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(updateProfile);
            }
        });
    }
}
