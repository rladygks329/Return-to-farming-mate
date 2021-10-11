package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Member;

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

        // 최초 실행 여부를 판단 -> 최초 실행 : 사용자 데이터 수집(나이, 선호 키워드 ...)
        SharedPreferences pref = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst", false);
        Log.d("checkFirst: ", String.valueOf(checkFirst));

//        if(!checkFirst){    // false일 경우 최초 실행
//            if(getUserAge() != 0){  // 사용자로부터 입력 받았을 때 최초 실행 true로 바꿈
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putBoolean("checkFirst",true);
//                editor.apply();
//                finish();
//            }
//
//            // 앱 최초 실행시 하고 싶은 작업
//            Intent intent = new Intent(MainActivity.this, ChoiceAge.class);
//            startActivity(intent);
//
//        }
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

    // 현재 사용자의 나이 가져오기
//    public int getUserAge(){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null){
//            Log.d("user email : " , user.get());
//            return user.getAge();
//        }
//        return null;
//    }

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
            //Log.d("user_main: ", "CREATE!!");
//
//            Button btn_logout = (Button) view.findViewById(R.id.btn_logout);
//            Button btn_login = (Button) view.findViewById(R.id.btn_login);
//
//            // 이전에 사용자의 로그인 기록 있는지 확인
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            if (user != null) {
//                // User is signed in
//                // go to main page
//                btn_login.setVisibility(View.INVISIBLE); // 화면에 로그인 버튼 안보이게 한다
//                btn_logout.setVisibility(View.VISIBLE);
//            } else {
//                // No user is signed in
//                // go to loging page
//                btn_logout.setVisibility(View.INVISIBLE); // 화면에 로그아웃 버튼 안보이게 한다
//                btn_login.setVisibility(View.VISIBLE);
//            }
//            finish();
//
//            btn_logout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FirebaseAuth.getInstance().signOut();
//                    Toast.makeText(MainActivity, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Main.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//            btn_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("login click: ", "true");
//                    Intent intent = new Intent(com.android.become_a_farmer.user_main.this, Login.class);
//                    startActivity(intent);
//                }
//            });
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