package com.android.become_a_farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class home_main extends AppCompatActivity {
    Button btn_test_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        // 로그인 페이지 테스트
//        btn_test_login = (Button) findViewById(R.id.btn_test_login);
//        btn_test_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//            }
//        });

        // 최초 실행 여부를 판단 -> 최초 실행 : 사용자 데이터 수집(나이, 선호 키워드 ...)
        SharedPreferences pref = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst", false);
        Log.d("checkFirst: ", String.valueOf(checkFirst));
        // false일 경우 최초 실행
        if(!checkFirst){
            // 앱 최초 실행시 하고 싶은 작업
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("checkFirst",true);
            editor.apply();
            finish();

            Intent intent = new Intent(home_main.this, ChoiceAge.class);
            startActivity(intent);

        }
    }
}