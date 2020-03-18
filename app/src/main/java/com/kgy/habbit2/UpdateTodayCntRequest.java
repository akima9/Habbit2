package com.kgy.habbit2;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateTodayCntRequest extends StringRequest {

    private static final String URL = "http://15.164.250.22/today_count_update.php";
    private Map<String, String> map;

    public UpdateTodayCntRequest(String userId, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userId", userId);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
