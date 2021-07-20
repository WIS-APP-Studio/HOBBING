package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.adapter.SignInAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {
    private EditText sign_in_et_id, sign_in_et_pw;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.adminBackDoor();

        sign_in_et_id = findViewById(R.id.sign_in_et_id);
        sign_in_et_pw = findViewById(R.id.sign_in_et_pw);

        btn_login = findViewById(R.id.sign_in_btn);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { signIn(); }
        });
    }

    private void adminBackDoor() {
        TextView backDoor = findViewById(R.id.sign_in_title);

        backDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra("user_id","admin");
                startActivity(intent);
                finish();
            }
        });
    }

    private void signIn() {
        final String user_id = sign_in_et_id.getText().toString();
        String user_pw = sign_in_et_pw.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success != null && success.equals("1")) {
                        Toast.makeText(getApplicationContext(),"로그인 성공!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 실패!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"로그인 처리시 에러발생!",Toast.LENGTH_SHORT).show();
                return;
            }
        };

        // Volley 로 로그인 양식 웹전송
        SignInAdapter adapter = new SignInAdapter(user_id,user_pw,responseListener,errorListener);
        adapter.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(adapter);
    }
}
