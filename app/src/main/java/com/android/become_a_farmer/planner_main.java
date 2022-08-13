package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class planner_main extends Fragment {
    private android.view.View view;
    private ImageView btn_start_planner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_planner_main, container, false);

        btn_start_planner = (ImageView) view.findViewById(R.id.btn_start_planner);

        btn_start_planner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FirstPlanner.class);
                startActivity(intent);

            }
        });

        return view;
    }


}