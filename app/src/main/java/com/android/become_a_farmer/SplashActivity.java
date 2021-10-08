package com.android.become_a_farmer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 로그인 상태 유지
        // 이전에 사용자의 로그인 기록 있는지 확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            // go to main page
            intent = new Intent(this, cartMain.class);
        } else {
            // No user is signed in
            // go to loging page
            intent = new Intent(this, Login.class);
        }

        startActivity(intent);
        finish();
    }
}
