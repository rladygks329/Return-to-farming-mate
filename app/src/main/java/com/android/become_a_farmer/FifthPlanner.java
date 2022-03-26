package com.android.become_a_farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FifthPlanner extends AppCompatActivity {
    private ImageView edu1;
    private ImageView edu2;
    private ImageView edu3;
    private ImageView edu4;
    private ImageView btn_start_planner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_education);

        edu1 = (ImageView) findViewById(R.id.edu1);
        edu2 = (ImageView) findViewById(R.id.edu2);
        edu3 = (ImageView) findViewById(R.id.edu3);
        edu4 = (ImageView) findViewById(R.id.edu4);
        btn_start_planner = (ImageView) findViewById(R.id.btn_start_planner);


        edu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.agriedu.net"));
                startActivity(intent);
            }
        });

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCLeAgEFNCagT_J2-n0VXnOw"));
                startActivity(intent);
            }
        });

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.refarm.org"));
                startActivity(intent);
            }
        });

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.returnfarm.com:444/cmn/returnFarm/module/eduAkademy/localGovEducation.do"));
                startActivity(intent);
            }
        });

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FifthPlanner.this, SixthPlanner.class);
                startActivity(intent);
            }
        });

    }




}