package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static com.wisappstudio.hobbing.data.ServerData.DELETE_ACCOUNT_URL;

public class DeleteAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        Button deleteAccount = (Button) findViewById(R.id.activity_delete_account_confirm);
        Button cancel = (Button) findViewById(R.id.activity_delete_account_cancel);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest deleteAccountRequest = new StringRequest(Request.Method.POST, DELETE_ACCOUNT_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DeleteAccountActivity.this, "탈퇴 되었습니다.\r\n이용해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                        Log.d("VOLLEYLOG", response);
                        MainActivity mainActivity = (MainActivity) MainActivity.activity;
                        mainActivity.finish();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEYLOG", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        String USER_ID = getIntent().getStringExtra("user_id");
                        Log.d("VOLLEYLOG", USER_ID);
                        params.put("user_id",USER_ID);
                        return params;
                    }
                };
                queue.add(deleteAccountRequest);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
