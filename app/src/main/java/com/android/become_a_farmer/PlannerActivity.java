package com.android.become_a_farmer;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.become_a_farmer.databinding.ActivityPlannerBinding;

public class PlannerActivity extends AppCompatActivity {
    private ActivityPlannerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.plannerPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void changeFragment(Fragment frag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.planner_frame, frag)
                .commit();
    }

}
