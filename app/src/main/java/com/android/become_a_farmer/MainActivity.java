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
    private home_main home_main;
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

        home_main = new home_main();
        planner_main = new planner_main();
        cartMain = new cartMain();
        user_main = new user_main();

        // 맨 처음 화면 home_main fragment로 설정
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, home_main).commit(); //FrameLayout에 fragment.xml 띄우기


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
                ft.replace(R.id.frameLayout, home_main);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.frameLayout, planner_main);
                ft.commit();
                break;

//            case 2:
//                ft.replace(R.id.frameLayout, cartMain);
//                ft.commit();
//                break;

            case 3:
                ft.replace(R.id.frameLayout, user_main);
                ft.commit();
                break;
        }
    }


    // 현재 사용자의 이메일 가져오기
    public String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return user.getEmail();
        }
        return null;
    }


}