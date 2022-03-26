package com.android.become_a_farmer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FirstPlanner extends AppCompatActivity {
    private ImageView btn_start_planner;
    private EditText editTextRegion;
    private TextView btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);

        btn_start_planner = (ImageView) findViewById(R.id.btn_start_planner);
        editTextRegion = (EditText) findViewById(R.id.editTextRegion);
        btnGoHome = (TextView) findViewById(R.id.btnGoHome);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPlanner.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String region = editTextRegion.getText().toString();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedRegion", region);
                editor.commit();

                Intent intent = new Intent(FirstPlanner.this, SecondPlanner.class);
                startActivity(intent);
            }
        });
    }

    // 다른 화면 터치 시 키보드 내림
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextRegion.getWindowToken(), 0);
        return true;
    }



}