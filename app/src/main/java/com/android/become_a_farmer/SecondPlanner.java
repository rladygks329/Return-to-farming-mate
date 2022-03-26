package com.android.become_a_farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SecondPlanner extends AppCompatActivity {
    private ImageView btn_start_planner;
    private ImageView btnGarlic;
    private ImageView btnRice;
    private ImageView btnApple;
    private ImageView btnUnion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crop);


        btn_start_planner = (ImageView) findViewById(R.id.btn_start_planner);
        btnGarlic = (ImageView) findViewById(R.id.btnGarlic);
        btnRice = (ImageView) findViewById(R.id.btnRice);
        btnApple = (ImageView) findViewById(R.id.btnApple);
        btnUnion = (ImageView) findViewById(R.id.btnUnion);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);


        btnGarlic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedCrop", "마늘");
                editor.commit();
            }
        });

        btnRice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedCrop", "쌀");
                editor.commit();
            }
        });

        btnApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedCrop", "사과");
                editor.commit();
            }
        });

        btnUnion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedCrop", "양파");
                editor.commit();
            }
        });



        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondPlanner.this, ThirdPlanner.class);
                startActivity(intent);
            }
        });
    }
}