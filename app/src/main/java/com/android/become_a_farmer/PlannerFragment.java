package com.android.become_a_farmer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.become_a_farmer.databinding.ActivityPlannerBinding;
public class PlannerFragment extends Fragment {
    private ActivityPlannerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityPlannerBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    private void changeFragment(Fragment frag){
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.planner_frame, frag)
                .commit();
    }

}
