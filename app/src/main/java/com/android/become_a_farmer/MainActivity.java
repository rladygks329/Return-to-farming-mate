package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private home_main home_main;
    private planner_main planner_main;
    private cartMain cartMain;
    private user_main user_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);

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
                    case R.id.cartItem:
                        setFrag(2);
                        break;
                    case R.id.userItem:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        home_main = new home_main();
        planner_main = new planner_main();
        cartMain = new cartMain();
        user_main = new user_main();
        setFrag(0);
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

            case 2:
                ft.replace(R.id.frameLayout, cartMain);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.frameLayout, user_main);
                ft.commit();
                break;
        }
    }

    public static class home_main extends Fragment {
        private android.view.View view;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.activity_home_main, container, false);

            return view;
        }

    }

    public static class planner_main extends Fragment {
        private android.view.View view;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.activity_planner_main, container, false);

            return view;
        }

    }

    public static class cartMain extends Fragment {
        private android.view.View view;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.activity_cart_main, container, false);

            return view;
        }

    }

    public static class user_main extends Fragment {
        private android.view.View view;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.activity_user_main, container, false);

            return view;
        }
    }


}