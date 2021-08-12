package com.wisappstudio.hobbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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

import static com.wisappstudio.hobbing.data.ServerData.INNER_POST_READ_URL;
import static com.wisappstudio.hobbing.data.ServerData.PROFILE_SELECT_CATEGORY_URL;

public class SelectCategoryActivity extends AppCompatActivity {
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
    }

    public void click(View view) {
        Button button = (Button) view;
        FrameLayout parentFrameout = (FrameLayout) button.getParent();

        CheckBox checkBox = (CheckBox) parentFrameout.getChildAt(1);

        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
            checkBox.setVisibility(View.INVISIBLE);
        } else {
            checkBox.setChecked(true);
            checkBox.setVisibility(View.VISIBLE);
        }

        Toast.makeText(this, parentFrameout.getContentDescription() , Toast.LENGTH_SHORT).show();
    }

    public void select(View view) {
        queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest selectCategory = new StringRequest(Request.Method.POST, PROFILE_SELECT_CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSEVOLLEY", response);
                Intent intent = new Intent(SelectCategoryActivity.this, WriteProfileActivity.class);
                intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSEVOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
                CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
                CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
                CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkbox4);
                CheckBox checkBox5 = (CheckBox) findViewById(R.id.checkbox5);
                CheckBox checkBox6 = (CheckBox) findViewById(R.id.checkbox6);
                CheckBox[] categoryList = { checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6 };
                int count = 0;
                Log.d("RESPONSEVOLLEY", "getParams() ->");
                for(int i=0; i < categoryList.length; i++) {
                    if(categoryList[i].isChecked()) {
                        Log.d("RESPONSEVOLLEY", categoryList[i].getText().toString());
                        FrameLayout frameLayout = (FrameLayout) categoryList[i].getParent();
                        Button category = (Button) frameLayout.getChildAt(0);
                        params.put(("category_"+count++), category.getText().toString());
                    }
                }
                params.put("user_id", getIntent().getStringExtra("user_id"));
                return params;
            }
        };
        queue.add(selectCategory);
    }
}