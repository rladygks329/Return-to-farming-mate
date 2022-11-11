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

import com.android.become_a_farmer.databinding.FragmentSelectEducationBinding;

public class SelectEducationFragment extends Fragment {
    private FragmentSelectEducationBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectEducationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //글자 색 변경
        String content = binding.selectEduTitle.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "교육";

        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightGreen)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.selectEduTitle.setText(spannableString);

        binding.selectEdiBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                btn.setSelected(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btn.setSelected(false);
                    }
                }, 500);
                SelectEducationFragment.this.loadEdu("https://www.agriedu.net");
            }
        });
        binding.selectEdiBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                btn.setSelected(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btn.setSelected(false);
                    }
                }, 500);
                SelectEducationFragment.this.loadEdu("https://www.youtube.com/channel/UCLeAgEFNCagT_J2-n0VXnOw");
            }
        });
        binding.selectEdiBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                btn.setSelected(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btn.setSelected(false);
                    }
                }, 500);
                SelectEducationFragment.this.loadEdu("http://www.refarm.org");
            }
        });
        binding.selectEdiBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                btn.setSelected(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btn.setSelected(false);
                    }
                }, 500);
                SelectEducationFragment.this.loadEdu("https://www.returnfarm.com:444/cmn/returnFarm/module/eduAkademy/localGovEducation.do");
            }
        });

        binding.selectEduNextBtn.setOnClickListener(new View.OnClickListener() {
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
