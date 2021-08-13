package com.wisappstudio.hobbing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SignInActivity extends AppCompatActivity {
    private EditText sign_in_et_id, sign_in_et_pw;
    private Button btn_login;

    boolean autoLoginChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_in_et_id = findViewById(R.id.sign_in_et_id);
        sign_in_et_pw = findViewById(R.id.sign_in_et_pw);

        clickSignUp(); // 회원가입 버튼 클릭
        switchAutoLogin(); // 자동 로그인 동작
        clickSignIn(); // 로그인 버튼 클릭
    }

    private void clickSignIn() {
        btn_login = findViewById(R.id.sign_in_btn);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isValidInfo()) {
                    signIn();
                }
                else {
                    Toast.makeText(getApplicationContext(), "아이디 및 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clickSignUp() {
        TextView signUp = (TextView) findViewById(R.id.activity_sign_in_to_sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsOfServiceActivity.class);
                finish();
                startActivity(intent);
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
                        Toast.makeText(getApplicationContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra("user_id",user_id);
                        IntroActivity introActivity = (IntroActivity) IntroActivity.activity;
                        startActivity(intent);
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

                        if(autoLoginChecked==true) {
                            SharedPreferences.Editor autoLogin = auto.edit();
                            autoLogin.putString("id", user_id);
                            autoLogin.putString("pw", user_pw);
                            autoLogin.commit();
                        }

                        introActivity.finish();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"아이디 및 비밀번호를 다시 입력하세요.",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"서버가 응답하지 않습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
        };

        // Volley 로 로그인 양식 웹전송
        SignInAdapter adapter = new SignInAdapter(user_id,user_pw,responseListener,errorListener);
        adapter.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(adapter);
    }

    private boolean isValidInfo() {
        String idRegex = "^[a-z0-9_]{5,20}$";
        String pwRegex = "^[a-zA-Z0-9_]{8,16}$";
        boolean checkIdRegex = Pattern.matches(idRegex, sign_in_et_id.getText().toString());
        boolean checkPwRegex = Pattern.matches(pwRegex, sign_in_et_pw.getText().toString());

        return checkIdRegex&&checkPwRegex ? true : false;
    }

    private void switchAutoLogin() {
        ImageView autoLogin = (ImageView) findViewById(R.id.auto_login_check);
        autoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoLoginChecked==false) {
                    autoLogin.setImageResource(R.drawable.check2);
                    autoLoginChecked=true;
                } else {
                    autoLogin.setImageResource(R.drawable.check);
                    autoLoginChecked=false;
                }
            }
        });
    }
}
