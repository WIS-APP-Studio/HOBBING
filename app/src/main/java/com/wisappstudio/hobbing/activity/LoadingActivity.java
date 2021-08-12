package com.wisappstudio.hobbing.activity;

<<<<<<< HEAD
import android.app.ProgressDialog;
=======
import android.app.Activity;
>>>>>>> 456fd76290b3c31f15956d44493fd4dab353b60c
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
            @Override
            public void run() {
<<<<<<< HEAD

                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();

=======
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                id = auto.getString("id", null);
                pw = auto.getString("pw", null);

                if(id != null && pw != null) {
                    signIn();
                } else {
                    Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(intent);
                    finish();
                }
>>>>>>> 456fd76290b3c31f15956d44493fd4dab353b60c
            }
        }, LoadingTime);
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
                    String success = jsonObject.getString("success");
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
                    Toast.makeText(LoadingActivity.this, "서버가 응답하지 않습니다.", Toast.LENGTH_SHORT).show();
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

        // Volley 로 로그인 양식 웹전송
        SignInAdapter adapter = new SignInAdapter(id,pw,responseListener,errorListener);
        adapter.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(adapter);
    }
}
