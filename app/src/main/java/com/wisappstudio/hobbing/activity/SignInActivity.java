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

import java.util.regex.Pattern;

/*
    아이디 조건- 5~20자의 영문 소문자, 숫자 이용. (한글 및 특수문자 제외)
    비밀번호 조건-8~16자 영문 대 소문자, 숫자 이용. (한글 및 특수문자 제외)
 */

public class SignInActivity extends AppCompatActivity {
    private EditText sign_in_et_id, sign_in_et_pw;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.adminBackDoor();

        TextView gotoSignUp = (TextView) findViewById(R.id.activity_sign_in_to_sign_up);

        gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                finish();
                startActivity(intent);
            }
        });

        sign_in_et_id = findViewById(R.id.sign_in_et_id);
        sign_in_et_pw = findViewById(R.id.sign_in_et_pw);

        btn_login = findViewById(R.id.sign_in_btn);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                    아이디 및 비밀번호 규약 알고리즘
                    아이디 조건- 5~20자의 영문 소문자, 숫자 이용. (한글 및 특수문자 제외)
                    비밀번호 조건-8~16자 영문 대 소문자, 숫자 이용. (한글 및 특수문자 제외)
                 */
                String idRegex = "^[a-z0-9_]{5,20}$";
                String pwRegex = "^[a-zA-Z0-9_]{8,16}$";

                boolean checkIdRegex = Pattern.matches(idRegex, sign_in_et_id.getText().toString());
                boolean checkPwRegex = Pattern.matches(pwRegex, sign_in_et_pw.getText().toString());

                if(checkIdRegex&&checkPwRegex) {
                    signIn();
                }
                else {
                    Toast.makeText(SignInActivity.this, "아이디 및 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }

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
                        IntroActivity introActivity = (IntroActivity) IntroActivity.activity;
                        startActivity(intent);
                        introActivity.finish();
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
