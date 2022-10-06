package com.android.become_a_farmer;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.become_a_farmer.databinding.FragmentSelectRegionBinding;
import com.google.android.material.button.MaterialButton;

public class SelectRegionFragment extends Fragment {

    private FragmentSelectRegionBinding binding;
    private View selectedView = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectRegionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String[] regions = {
                "강원도 강릉시", "강원도 고성군", "강원도 삼척시", "강원도 속초시", "강원도 영월군", "강원도 홍천군",
                "강원도 횡성군", "경기도 남양주시", "경기도 화성시", "경상북도 김천시", "경상북도 상주시", "경상북도 의성군",
                "전라남도 고흥군", "전라남도 곡성군", "전라남도 구례군", "전라남도 나주시", "전라북도 고창군", "전라북도 군산시",
                "전라북도 순창군", "전라북도 익산시", "전라북도 정읍시", "충청남도 공주시", "충청남도 아산시", "충청남도 흥성군",
                "충청북도 음성군"
        };
        for (int i = 0; i < regions.length; i++) {
            MaterialButton btn = (MaterialButton) binding.selectRegionGrid.getChildAt(i);
            btn.setText(regions[i]);
            btn.setTag(regions[i]);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectView(view);
                }
            });
        }
        binding.selectRegionNextBtn.setOnClickListener(view1 -> {
            if(selectedView == null){
                Toast.makeText(getActivity(), "지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
            String region = selectedView.getTag().toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("selectedRegion", region);
            editor.commit();

            PlannerActivity activity = (PlannerActivity) getActivity();
            activity.moveNextStep();
        });
        return view;
    }

    private void selectView(View view){
        if (selectedView != null) {
            selectedView.setSelected(false);
        }
        view.setSelected(true);
        selectedView = view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
