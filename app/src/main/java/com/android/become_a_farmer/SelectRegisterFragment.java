package com.android.become_a_farmer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.become_a_farmer.databinding.FragmentSelectRegisterBinding;

public class SelectRegisterFragment extends Fragment {
    private FragmentSelectRegisterBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //글자 색 변경
        String content = binding.selectRegisterTitle.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "농업 경영체";

        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightGreen)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.selectRegisterTitle.setText(spannableString);

        binding.selectRegisterBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                btn.setSelected(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btn.setSelected(false);
                    }
                }, 500);
                //loadEdu("");
            }
        });
        binding.selectRegisterBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                btn.setSelected(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btn.setSelected(false);
                    }
                }, 500);
                loadEdu("http://uni.agrix.go.kr/docs2/potal/main.html");
            }
        });


        binding.selectRegisterNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlannerActivity activity = (PlannerActivity) getActivity();
                activity.moveNextStep();
            }
        });

        return view;
    }

    private void loadEdu(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
