package com.android.become_a_farmer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.become_a_farmer.databinding.FragmentSelectHouseBinding;
import com.google.android.material.button.MaterialButton;

public class SelectHouseFragment extends Fragment {
    private FragmentSelectHouseBinding binding;
    private View selectedView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentSelectHouseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //글자 색 변경
        String content = binding.selectHouseTitle.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "하우스/시공";

        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightGreen)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.selectHouseTitle.setText(spannableString);

        String[] houses = {"비닐하우스", "주택", "컨테이너", "원막"};
        for(int i = 0; i<houses.length; i++) {
            MaterialButton btn = (MaterialButton) binding.selectHouseGrid.getChildAt(i);
            btn.setText(houses[i]);
            btn.setTag(houses[i]);
            btn.setOnClickListener(this::selectView);
        }
        binding.selectHouseNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (selectedView == null) {
                    Toast.makeText(getActivity(), "시공 방법을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
                String house = selectedView.getTag().toString();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedHouse", house);
                editor.commit();

                PlannerActivity activity = (PlannerActivity) getActivity();
                activity.moveNextStep();
            }
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
