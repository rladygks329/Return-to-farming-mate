package com.android.become_a_farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_main extends AppCompatActivity {
    private Button btn_logout;
    private Button btn_login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Log.d("user_main", " create!!!");

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_login = (Button) findViewById(R.id.btn_login);

        // 이전에 사용자의 로그인 기록 있는지 확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            // go to main page
            btn_login.setVisibility(View.INVISIBLE); // 화면에 로그인 버튼 안보이게 한다
            btn_logout.setVisibility(View.VISIBLE);
        } else {
            // No user is signed in
            // go to loging page
            btn_logout.setVisibility(View.INVISIBLE); // 화면에 로그아웃 버튼 안보이게 한다
            btn_login.setVisibility(View.VISIBLE);
        }
        finish();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(user_main.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(user_main.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("login click: ", "true");
                Intent intent = new Intent(user_main.this, Login.class);
                startActivity(intent);
            }
        });
    }
}