package com.android.become_a_farmer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 로그인 상태 유지
        // 이전에 사용자의 로그인 기록 있는지 확인
        user = FirebaseAuth.getInstance().getCurrentUser();
        movePage(0);    // 2초 후 화면 전환

    }


    /**
     * 상태에 따라 진입 경로 다르게 설정하는 함수
     * @param sec splash 화면이 노출되는 시간
     */
    public void movePage(int sec){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {     // 이전에 로그인 한 기록이 있다면, 메인 화면으로 전환
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);

                } else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();

            }
        }, 1000 * sec);
    }
}
