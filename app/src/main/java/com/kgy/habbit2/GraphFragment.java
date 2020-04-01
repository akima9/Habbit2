package com.kgy.habbit2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GraphFragment extends Fragment {
    Context context;
    OnTabItemSelectedListener listener;

    CalendarView calendarView;
    TextView selectedDate;
    TextView goalCnt;

    String lsUserId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        if (context instanceof OnTabItemSelectedListener) {
            listener = (OnTabItemSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.graph_fragment, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        // 로그인 한 ID를 가지고 오는 부분
        SessionManage sessionManage = new SessionManage();
        lsUserId = sessionManage.getAttribute(getContext(), "userId");

        calendarView = rootView.findViewById(R.id.calendarView);
        selectedDate = rootView.findViewById(R.id.selectedDate);
        goalCnt = rootView.findViewById(R.id.goalCnt);

        // 초기 날짜 세팅
        Date currentTime = Calendar.getInstance().getTime();
        String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 목표 개수", Locale.getDefault()).format(currentTime);
        selectedDate.setText(date_text);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Habbit", "response : "+response);
                    JSONObject jsonObject = new JSONObject(response);
                    String goal_Cnt = jsonObject.getString("goalCnt");
                    goalCnt.setText(goal_Cnt+"개");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TodayGoalCntRequest todayGoalCntRequest = new TodayGoalCntRequest(lsUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(todayGoalCntRequest);

        // 날짜를 선택했을 때
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String date;
                if (month + 1 < 10) {
                    if (dayOfMonth < 10) {
                        date = year + "년 0"+ (month + 1) + "월 0" + dayOfMonth + "일 목표 개수";
                    } else {
                        date = year + "년 0"+ (month + 1) + "월 " + dayOfMonth + "일 목표 개수";
                    }
                } else {
                    if (dayOfMonth < 10) {
                        date = year + "년 " + (month + 1) + "월 0" + dayOfMonth + "일 목표 개수";
                    } else {
                        date = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 목표 개수";
                    }
                }

                selectedDate.setText(date);

                // history table에서 todayCnt, goal 가져와서 보여주기
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // todayCnt, goal 받아와서 set 하기
                            Log.d("Habbit", "response2 : "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            //String goal_Cnt = jsonObject.getString("goalCnt");
                            //goalCnt.setText(goal_Cnt+"개");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                HistoryRequest historyRequest = new HistoryRequest(lsUserId, year, month, dayOfMonth, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(historyRequest);
            }
        });
    }

}
