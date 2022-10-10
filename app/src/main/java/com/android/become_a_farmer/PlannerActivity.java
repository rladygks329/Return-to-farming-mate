package com.android.become_a_farmer;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.become_a_farmer.databinding.ActivityPlannerBinding;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class PlannerActivity extends AppCompatActivity {
    private ActivityPlannerBinding binding;
    private int current = R.id.planner_first_btn;
    private SelectRegionFragment frag1;
    private SelectCropFragment frag2;
    private SelectLandFragment frag3;
    private SelectHouseFragment frag4;
    private SelectEducationFragment frag5;
    private SelectRegisterFragment frag6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        frag1 = new SelectRegionFragment();
        frag2 = new SelectCropFragment();
        frag3 = new SelectLandFragment();
        frag4 = new SelectHouseFragment();
        frag5 = new SelectEducationFragment();
        frag6 = new SelectRegisterFragment();

        binding.plannerToggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(!isChecked){
                return;
            }
            current = checkedId;
            switch (checkedId){
                case R.id.planner_first_btn:
                    changeFragment(frag1);
                    break;
                case R.id.planner_second_btn:
                    changeFragment(frag2);
                    break;
                case R.id.planner_third_btn:
                    changeFragment(frag3);
                    break;
                case R.id.planner_fourth_btn:
                    changeFragment(frag4);
                    break;
                case R.id.planner_fifth_btn:
                    changeFragment(frag5);
                case R.id.planner_sixth_btn:
                    changeFragment(frag6);
            }
            Toast.makeText(PlannerActivity.this, Integer.toString(checkedId), Toast.LENGTH_SHORT).show();
        });
        binding.plannerPrev.setOnClickListener(view -> finish());
        changeFragment(frag1);
    }

    private void changeFragment(Fragment frag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.planner_frame, frag)
                .commit();
    }
    public void moveNextStep(){
        switch (current){
            case R.id.planner_first_btn:
                binding.plannerToggleButtonGroup.check(R.id.planner_second_btn);
                break;
            case R.id.planner_second_btn:
                binding.plannerToggleButtonGroup.check(R.id.planner_third_btn);
                break;
            case R.id.planner_third_btn:
                binding.plannerToggleButtonGroup.check(R.id.planner_fourth_btn);
                break;
            case R.id.planner_fourth_btn:
                binding.plannerToggleButtonGroup.check(R.id.planner_fifth_btn);
                break;
            case R.id.planner_fifth_btn:
                binding.plannerToggleButtonGroup.check(R.id.planner_sixth_btn);
            case R.id.planner_sixth_btn:
                finish();
        }
    }

}
