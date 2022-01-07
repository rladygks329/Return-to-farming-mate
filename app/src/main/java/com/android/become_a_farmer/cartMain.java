package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class cartMain extends Fragment {

    private android.view.View view;
    private Button test;

//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            view = inflater.inflate(R.layout.activity_cart_main, container, false);
//
//            test = (Button) view.findViewById(R.id.button1);
//            test.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.ContentLayout, fragment).commit();
//
//                    ((MainActivity)getActivity()).replaceFragment(new user_main());
//
//                }
//            });
//
//            return view;
//        }
}