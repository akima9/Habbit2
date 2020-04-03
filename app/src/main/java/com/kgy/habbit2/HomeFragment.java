package com.kgy.habbit2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    Context context;
    OnTabItemSelectedListener listener;

    TextView todayCnt;
    TextView goalCnt;
    TextView backCnt;
    String lsUserId;

    private InterstitialAd mInterstitialAd;

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
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded(){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("Habbit", "The interstitial wasn't loaded yet.");
                }
            }
        });
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.home_fragment, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        // 현재 개수
        todayCnt = rootView.findViewById(R.id.todayCnt);
        // 목표 개수
        goalCnt = rootView.findViewById(R.id.goalCnt);
        // 플러스 버튼
        Button plusBtn = rootView.findViewById(R.id.plusBtn);
        // 되돌리기
        backCnt = rootView.findViewById(R.id.backCnt);

        // 로그인 한 ID를 가지고 오는 부분
        SessionManage sessionManage = new SessionManage();
        lsUserId = sessionManage.getAttribute(getContext(), "userId");

        // 오늘 카운트 가지고 오는 부분
        getCount();

        // 목표 카운트 가지고 오는 부분
        getGoalCount();

        // 되돌리기 클릭
        backCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Habbit", "response : "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                getCount();
                            } else {
                                Toast.makeText(getContext(), "0개 이하로는 되돌릴 수 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                BackTodayCntRequest backTodayCntRequest = new BackTodayCntRequest(lsUserId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(backTodayCntRequest);
            }
        });

        // 플러스 버튼 클릭
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                getCount();
                            } else {
                                Toast.makeText(getContext(), "업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                UpdateTodayCntRequest updateTodayCntRequest = new UpdateTodayCntRequest(lsUserId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(updateTodayCntRequest);
            }
        });
    }
    public void getCount(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String goalCnt = jsonObject.getString("goalCnt");
                    todayCnt.setText(goalCnt+"개");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TodayCntRequest todayCntRequest = new TodayCntRequest(lsUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(todayCntRequest);
    }

    public void getGoalCount(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Cnt = jsonObject.getString("goalCnt");
                    goalCnt.setText(Cnt+"개");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TodayGoalCntRequest todayGoalCntRequest = new TodayGoalCntRequest(lsUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(todayGoalCntRequest);
    }
}
