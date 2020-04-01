package com.kgy.habbit2;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class HistoryRequest extends StringRequest {

    private static final String URL = "http://15.164.250.22/history_select.php";
    private Map<String, String> map;

    public HistoryRequest(String userId, int year, int month, int dayOfMonth, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userId", userId);
        map.put("year", Integer.toString(year));

        if (month + 1 < 10) {
            map.put("month", "0" + (month + 1));
        } else {
            map.put("month", Integer.toString(month + 1));
        }

        if (dayOfMonth < 10) {
            map.put("dayOfMonth", "0" + dayOfMonth);
        } else {
            map.put("dayOfMonth", Integer.toString(dayOfMonth));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
