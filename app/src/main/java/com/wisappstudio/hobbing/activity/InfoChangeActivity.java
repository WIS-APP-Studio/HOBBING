package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.wisappstudio.hobbing.adapter.SignInAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class InfoChangeActivity extends AppCompatActivity {
    private EditText change_in_id, change_in_pw, change_in_pw_2, change_in_mail;
    private Button confirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_change);

        change_in_id = findViewById(R.id.change_in_id);
        change_in_pw = findViewById(R.id.change_in_pw);
        change_in_pw_2 = findViewById(R.id.change_in_pw_2);
        change_in_mail = findViewById(R.id.change_in_mail);


        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idRegex = "^[a-z0-9_]{5,20}$";
                String pwRegex = "^[a-zA-Z0-9_]{8,16}$";
                String pwRegex2 = "^[a-zA-Z0-9_]{8,16}$";
                String emailRegex = "\\w+@\\w+\\.\\w+(\\.\\w+)?";

                boolean checkIdRegex = Pattern.matches(idRegex, change_in_id.getText().toString());
                boolean checkPwRegex = Pattern.matches(pwRegex, change_in_pw.getText().toString());
                boolean checkPwRegex2 = Pattern.matches(pwRegex2, change_in_pw_2.getText().toString());
                boolean checkEmailRegex = Pattern.matches(emailRegex, change_in_mail.getText().toString());

                if (checkIdRegex && checkPwRegex && checkPwRegex2 && checkEmailRegex) {
                    // 1차 비밀번호와 2차 비밀번호가 일치한치 확인.
                    if (pwRegex == pwRegex2) {
                        // 커스텀 다이얼로그를 생성. 사용자가 만든 클래스이다.
                        InfoCustomDialogActivity customDialog = new InfoCustomDialogActivity(InfoChangeActivity.this);
                        InfoChange();
                    }
                    else {
                        Toast.makeText(InfoChangeActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InfoChangeActivity.this, "작성 조건에 알맞게 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InfoChange() {
        String change_id = change_in_id.getText().toString();
        String change_pw = change_in_pw.getText().toString();
        String change_mail = change_in_mail.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success != null && success.equals("1")) {  // 회원정보 변경 완료
                        Toast.makeText(getApplicationContext(), "회원정보 변경 성공!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InfoChangeActivity.this, InfoChangeActivity.class);
                        intent.putExtra("user_id",change_id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "회원정보 변경 실패!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"회원정보 변경 처리시 에러발생!",Toast.LENGTH_SHORT).show();
                return;
            }
        };

        // Volley 로 변경된 양식 웹전송 (인태님 ㅎㅎ ...)
        SignInAdapter adapter = new SignInAdapter(change_id,change_pw,change_mail,responseListener,errorListener);
        adapter.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(adapter);
    }

}
