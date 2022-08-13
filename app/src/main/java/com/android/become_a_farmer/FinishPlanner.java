package com.android.become_a_farmer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FinishPlanner extends AppCompatActivity {
    private TextView region;
    private TextView crop;
    private TextView land;
    private TextView house;
    private ImageButton btn;

    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_planner);

        region = (TextView) findViewById(R.id.region);
        crop = (TextView) findViewById(R.id.crop);
        land = (TextView) findViewById(R.id.land);
        house = (TextView) findViewById(R.id.house);
        btn = (ImageButton) findViewById(R.id.next);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        region.setText(pref.getString("selectedRegion", ""));
        crop.setText(pref.getString("selectedCrop", ""));
        land.setText(pref.getString("selectedLand", ""));
        house.setText(pref.getString("selectedHouse", ""));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishPlanner.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}