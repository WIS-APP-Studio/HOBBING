package com.wisappstudio.hobbing.adapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.wisappstudio.hobbing.data.SignUpData;

import java.util.HashMap;
import java.util.Map;


public class SignUpAdapter extends StringRequest {
    public static String URL = SignUpData.URL;

    private Map<String, String> map;

    public SignUpAdapter(String id, String password, String email, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST,URL, listener, errorListener);

        map = new HashMap<>();
        map.put("id",id);
        map.put("password",password);
        map.put("email",email);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
