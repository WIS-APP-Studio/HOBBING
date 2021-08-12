package com.wisappstudio.hobbing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.regex.Pattern;

import static com.wisappstudio.hobbing.data.ServerData.UPDATE_ACCOUNT_URL;

public class InfoChangeActivity extends AppCompatActivity {
    private EditText change_in_pw, change_in_pw_2, change_in_mail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_change);

        change_in_pw = findViewById(R.id.change_in_pw);
        change_in_pw_2 = findViewById(R.id.change_in_pw_2);
        change_in_mail = findViewById(R.id.change_in_mail);

        clickConfirm();
    }

    private void clickConfirm() {
        Button confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 정보가 맞는지 확인하는 함수, 맞으면 true, 아니면 false를 호출한다.
                if (isValidInfo()) {
                    infoChange();
                    finish();
                }
            }
        });
    }

    private boolean isValidInfo() {
        String pwRegex = "^[a-zA-Z0-9_]{8,16}$";
        String pwRegex2 = "^[a-zA-Z0-9_]{8,16}$";
        String emailRegex = "\\w+@\\w+\\.\\w+(\\.\\w+)?";

        boolean checkPwRegex = Pattern.matches(pwRegex, change_in_pw.getText().toString());
        boolean checkPwRegex2 = Pattern.matches(pwRegex2, change_in_pw_2.getText().toString());
        boolean checkEmailRegex = Pattern.matches(emailRegex, change_in_mail.getText().toString());

        if (checkPwRegex && checkPwRegex2 && checkEmailRegex) {
            // 1차 비밀번호와 2차 비밀번호가 일치한치 확인.
            if (pwRegex.equals(pwRegex2)) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "작성 조건에 알맞게 다시 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void infoChange() {
        StringRequest infoChangeRequest = new StringRequest(Request.Method.POST, UPDATE_ACCOUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "회원정보를 변경했습니다.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "서버가 응답하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", getIntent().getStringExtra("user_id"));
                params.put("pw", change_in_pw.getText().toString());
                params.put("email", change_in_mail.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(infoChangeRequest);
    }
}
