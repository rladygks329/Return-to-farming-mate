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

import com.android.become_a_farmer.databinding.FragmentSelectLandBinding;
import com.google.android.material.button.MaterialButton;

public class SelectLandFragment extends Fragment {
    private FragmentSelectLandBinding binding;
    private View selectedView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentSelectLandBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //글자 색 변경
        String content = binding.selectLandTitle.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "토지";

        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightGreen)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.selectLandTitle.setText(spannableString);

        String[] crops = {"논", "밭", "과수원", "임야", "매매", "임대"};
        for(int i = 0; i<crops.length; i++) {
            MaterialButton btn = (MaterialButton) binding.selectLandGrid.getChildAt(i);
            btn.setText(crops[i]);
            btn.setTag(crops[i]);
            btn.setOnClickListener(this::selectView);
        }
        binding.selectLandNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (selectedView == null) {
                    Toast.makeText(SelectLandFragment.this.getActivity(), "토지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences pref = SelectLandFragment.this.getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
                String land = selectedView.getTag().toString();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("selectedLand", land);
                editor.commit();

                PlannerActivity activity = (PlannerActivity) SelectLandFragment.this.getActivity();
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
