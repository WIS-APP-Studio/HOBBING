package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hobbing.R;
import com.wisappstudio.hobbing.adapter.SignUpAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText sign_up_et_id, sign_up_et_pw, sign_up_et_email;
    private Button btn_sign_up;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up_et_id = findViewById(R.id.sign_up_et_id);
        sign_up_et_pw = findViewById(R.id.sign_up_et_pw);
        sign_up_et_email = findViewById(R.id.sign_up_et_email);


        btn_sign_up = findViewById(R.id.sign_up_btn);
        btn_sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String idRegex = "^[a-z0-9_]{5,20}$";
                String pwRegex = "^[a-zA-Z0-9_]{8,16}$";
                String emailRegex = "\\w+@\\w+\\.\\w+(\\.\\w+)?";

                boolean checkIdRegex = Pattern.matches(idRegex, sign_up_et_id.getText().toString());
                boolean checkPwRegex = Pattern.matches(pwRegex, sign_up_et_pw.getText().toString());
                boolean checkEmailRegex = Pattern.matches(emailRegex, sign_up_et_email.getText().toString());
                if(checkIdRegex&&checkPwRegex&&checkEmailRegex) {
                    signUp();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "아이디 및 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp() {
        String id = sign_up_et_id.getText().toString();
        String pw = sign_up_et_pw.getText().toString();
        String email = sign_up_et_email.getText().toString();

        Response.Listener<String> resposneListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success != null && success.equals("1")) {  // 회원가입 완료
                        Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                        IntroActivity introActivity = (IntroActivity) IntroActivity.activity;
                        startActivity(intent);
                        introActivity.finish();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "회원가입 처리시 에러발생!", Toast.LENGTH_SHORT).show();
                return;
            }
        };

        SignUpAdapter adapter = new SignUpAdapter(id, pw, email, resposneListener, errorListener);
        adapter.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(adapter);
    }
}
