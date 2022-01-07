package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_main extends Fragment {
    private android.view.View view;
    private Context context;
    private Button btn_logout;
    private Button btn_login;
    private Button btn_modify_plan;
    private Button btn_modify_user_info;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Button btn_test;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_main, container, false);

        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_modify_plan = (Button) view.findViewById(R.id.btn_modify_plan);
        btn_modify_user_info = (Button) view.findViewById(R.id.btn_modify_user_info);
        btn_test = (Button) view.findViewById(R.id.test);

        // 이전에 사용자의 로그인 기록 있는지 확인
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            // go to main page
            btn_login.setVisibility(View.INVISIBLE); // 화면에 로그인 버튼 안보이게 한다
            btn_logout.setVisibility(View.VISIBLE);
        } else {
            // No user is signed in
            // go to loging page
            btn_logout.setVisibility(View.INVISIBLE); // 화면에 로그아웃 버튼 안보이게 한다
            btn_login.setVisibility(View.VISIBLE);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user_main 프래그먼트 -> Login 액티비티
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();

                // 로그아웃 후 앱 다시 시작
//                activity.finish();
            }
        });

        btn_modify_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user_main 프래그먼트 -> plan 수정하는 액티비티
                Intent intent = new Intent(getActivity(), ModifyPlan.class);
                startActivity(intent);
            }
        });

        btn_modify_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user_main 프래그먼트 -> user info 수정하는 액티비티
                Intent intent = new Intent(getActivity(), ModifyUserInfo.class);
                startActivity(intent);
            }
        });

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user_main 프래그먼트 -> user info 수정하는 액티비티
                Intent intent = new Intent(getActivity(), ChooseKeyword.class);
                startActivity(intent);
            }
        });



        return view;
    }
}