package com.wisappstudio.hobbing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
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

public class LoadingActivity extends AppCompatActivity {
    private int LoadingTime = 3000; // 밀리세컨드 단위 ( 1000(milli second) = 1(second)
    String id, pw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            // 딜레이가 진행되는 동안, 서버 응답 체크 및 자동 로그인 기능을 실행한다.
            @Override
            public void run() {
                autoLogin();
            }
        }, LoadingTime);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        TextView logoShadow = (TextView) findViewById(R.id.logo_shadow);
        logoShadow.startAnimation(fadeIn);

        fadeIn.setDuration(2500);


    }

    @Override
    protected void onPause() {
        super.onPause(); finish();
    }

    private void signIn() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success"); // 로그인 성공 시 1, 실패 시 0을 리턴한다.
                    if (success != null && success.equals("1")) {
                        Toast.makeText(getApplicationContext(),"자동 로그인 되었습니다.",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        intent.putExtra("user_id",id);
                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"자동 로그인 실패했습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "서버가 응답하지 않습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"자동 로그인 에러 발생",Toast.LENGTH_SHORT).show();
                return;
            }
        };

        // 아이디와 비밀번호 값과 리스너를 adapter 에 전송해 처리함
        SignInAdapter adapter = new SignInAdapter(id,pw,responseListener,errorListener);
        adapter.setShouldCache(false);

        // Volley 웹 통신을 진행함
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(adapter);
    }

    private void autoLogin() {
        // 자동 로그인을 위한 값을 저장소에서 불러옴, 초기엔 아무것도 없는 상태이다.
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        id = auto.getString("id", null); // 초기 값은 null 이다.
        pw = auto.getString("pw", null); // 자동 로그인 선택, 재 실행 시 defValue 엔 값이 저장되어 있다.

        if(id != null && pw != null) {
            signIn();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
