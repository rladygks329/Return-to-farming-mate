package com.android.become_a_farmer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        movePage(2);    // 2초 후 화면 전환

        // 로그인 상태 유지
        // 이전에 사용자의 로그인 기록 있는지 확인
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    public void movePage(int sec){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);

                } else{
                    Intent intent = new Intent(SplashActivity.this, Login.class);
                    startActivity(intent);
                }
                finish();

            }
        }, 1000 * sec);
    }
}
