package com.wisappstudio.hobbing.adapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.wisappstudio.hobbing.data.ServerData;

import java.util.HashMap;
import java.util.Map;

public class SignInAdapter extends StringRequest {
    private final static String URL = ServerData.SIGN_IN_URL;
    private Map<String,String> map;

    public SignInAdapter(String id, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        map = new HashMap<>();
        map.put("id",id);
        map.put("password",password);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}