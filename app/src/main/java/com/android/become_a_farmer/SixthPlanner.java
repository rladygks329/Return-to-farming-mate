package com.android.become_a_farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SixthPlanner extends AppCompatActivity {
    private ImageView btn_start_planner;
    private ImageView regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_register);

        btn_start_planner = (ImageView) findViewById(R.id.btn_start_planner);
        regist = (ImageView) findViewById(R.id.regist);


        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://uni.agrix.go.kr/docs2/potal/main.html"));
                startActivity(intent);
            }
        });

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SixthPlanner.this, FinishPlanner.class);
                startActivity(intent);
            }
        });
    }
}