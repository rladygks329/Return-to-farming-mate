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

import com.android.become_a_farmer.databinding.FragmentSelectCropBinding;
import com.android.become_a_farmer.databinding.ViewPlannerBtnBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SelectCropFragment extends Fragment {
    private FragmentSelectCropBinding binding;
    private View selectedView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectCropBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //글자 색 변경
        String content = binding.selectCropTitle.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "작물";

        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightGreen)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.selectCropTitle.setText(spannableString);
        getCropsFromDB();
        binding.selectCropNextBtn.setOnClickListener(view1 -> {
            if(selectedView == null){
                Toast.makeText(getActivity(), "작물을 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
            String crop = selectedView.getTag().toString();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("selectedCrop", crop);
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

    private void getCropsFromDB(){
        SharedPreferences pref = requireActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String regionName = pref.getString("selectedRegion", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("regions")
                .whereEqualTo("name", regionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        QuerySnapshot d = task.getResult();
                        List<String> list = Arrays.asList(d.getDocuments().get(0).get("crop").toString().split(","));
                        initButtons(list);
                    }
                });
    }

    private void initButtons(List<String> crops){
        Iterator<String> it = crops.iterator();
        while(it.hasNext()){
            ViewPlannerBtnBinding v = ViewPlannerBtnBinding.inflate(getLayoutInflater());
            String crop = it.next();
            v.viewPlannerBtnLeft.setText(crop);
            v.viewPlannerBtnLeft.setTag(crop);
            v.viewPlannerBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectView(view);
                }
            });
            if(it.hasNext()){
                crop = it.next();
                v.viewPlannerBtnRight.setText(crop);
                v.viewPlannerBtnRight.setTag(crop);
                v.viewPlannerBtnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectView(view);
                    }
                });
            }else{
                v.viewPlannerBtnRight.setVisibility(View.INVISIBLE);
            }
            binding.selectCropLr.addView(v.getRoot());
            v = null;
        }
    }
}