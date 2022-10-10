package com.android.become_a_farmer;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentHomeMain FragmentHomeMain;
    private planner_main planner_main;
    private cartMain cartMain;
    private user_main user_main;
    private static Activity activity;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);

        activity = this;

        FragmentHomeMain = new FragmentHomeMain();
        planner_main = new planner_main();
        cartMain = new cartMain();
        user_main = new user_main();

        // 제일 처음 띄워주는 home_main fragment로 설정
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, FragmentHomeMain, "home").commitAllowingStateLoss();
        bottomNavigationView.setSelectedItemId(R.id.homeItem);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.homeItem:
                        setFrag(0);
                        break;
                    case R.id.plannerItem:
                        setFrag(1);
                        break;
//                    case R.id.cartItem:
//                        setFrag(2);
//                        break;
                    case R.id.userItem:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

    }

    private void setFrag(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (i) {
            case 0:
                if (fm.findFragmentByTag("home") != null){
                    fm.beginTransaction().show(fm.findFragmentByTag("home")).commit();

                } else {
                    fm.beginTransaction().add(R.id.frameLayout, new FragmentHomeMain(), "home").commit();
                }

                if (fm.findFragmentByTag("planner") != null){
                    fm.beginTransaction().hide(fm.findFragmentByTag("planner")).commit();

                }

                if (fm.findFragmentByTag("user") != null){
                    fm.beginTransaction().hide(fm.findFragmentByTag("user")).commit();

                }
                break;

            case 1:
                if (fm.findFragmentByTag("planner") != null){
                    fm.beginTransaction().show(fm.findFragmentByTag("planner")).commit();

                } else {
                    fm.beginTransaction().add(R.id.frameLayout, new planner_main(), "planner").commit();
                }

                if (fm.findFragmentByTag("home") != null){
                    fm.beginTransaction().hide(fm.findFragmentByTag("home")).commit();

                }

                if (fm.findFragmentByTag("user") != null){
                    fm.beginTransaction().hide(fm.findFragmentByTag("user")).commit();

                }
                break;

//            case 2:
//                ft.replace(R.id.frameLayout, cartMain);
//                ft.commit();
//                break;

            case 3:
                if (fm.findFragmentByTag("user") != null){
                    fm.beginTransaction().show(fm.findFragmentByTag("user")).commit();

                } else {
                    fm.beginTransaction().add(R.id.frameLayout, new user_main(), "user").commit();
                }

                if (fm.findFragmentByTag("home") != null){
                    fm.beginTransaction().hide(fm.findFragmentByTag("home")).commit();

                }

                if (fm.findFragmentByTag("planner") != null){
                    fm.beginTransaction().hide(fm.findFragmentByTag("planner")).commit();

                }
                break;
        }
    }


}