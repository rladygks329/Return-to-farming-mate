package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import androidx.annotation.Nullable;

import com.android.become_a_farmer.databinding.FragmentSelectRegionBinding;
import com.android.become_a_farmer.databinding.ViewPlannerBtnBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectRegionFragment extends Fragment {

    private FragmentSelectRegionBinding binding;
    private View selectedView = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectRegionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //글자 색 변경
        String content = binding.selectRegionTitle.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "지역";

        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightGreen)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.selectRegionTitle.setText(spannableString);

        getRegionsFromDB();
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

    private void getRegionsFromDB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("regions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        List<String> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            String title = doc.getId();
                            String content = doc.get("introduction").toString();
                            String crop = doc.get("crop").toString();
                            String experienceContent = doc.get("experienceContent").toString();
                            String experienceTitle = doc.get("experienceTitle").toString();
                            list.add(title);
                        }
                        initButtons(list);
                    }
                });
    }

    private void initButtons(List<String> regions){
        Iterator<String> it = regions.iterator();
        while(it.hasNext()){
            ViewPlannerBtnBinding v = ViewPlannerBtnBinding.inflate(getLayoutInflater());
            String region = it.next();
            v.viewPlannerBtnLeft.setText(region);
            v.viewPlannerBtnLeft.setTag(region);
            v.viewPlannerBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectView(view);
                }
            });
            if(it.hasNext()){
                region = it.next();
                v.viewPlannerBtnRight.setText(region);
                v.viewPlannerBtnRight.setTag(region);
                v.viewPlannerBtnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectView(view);
                    }
                });
            }else{
                v.viewPlannerBtnRight.setVisibility(View.INVISIBLE);
            }
            binding.selectRegionLr.addView(v.getRoot());
            v = null;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
