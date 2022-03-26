package com.android.become_a_farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ThirdPlanner extends AppCompatActivity {
    private ImageView btn_start_planner;
    private ImageView btn1;
    private ImageView btn2;
    private ImageView btn3;
    private ImageView btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_land);

        btn_start_planner = (ImageView) findViewById(R.id.btn_start_planner);
        btn1 = (ImageView) findViewById(R.id.btn1);
        btn2 = (ImageView) findViewById(R.id.btn2);
        btn3 = (ImageView) findViewById(R.id.btn3);
        btn4 = (ImageView) findViewById(R.id.btn4);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedLand", "논");
                editor.commit();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedLand", "밭");
                editor.commit();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedLand", "과수원");
                editor.commit();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedLand", "임야");
                editor.commit();
            }
        });

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdPlanner.this, FourthPlanner.class);
                startActivity(intent);
            }
        });


    }
}