package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private home_main home_main;
    private planner_main planner_main;
    private cartMain cartMain;
    private user_main user_main;
    private static Activity activity;
    private FirebaseUser user;
    private static int visit_count = 0;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);

        activity = this;

        // 최초 실행 여부를 판단
        // -> 최초 실행 : 사용자 데이터 수집(나이, 선호 키워드 ...)
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("visit", String.valueOf(visit_count));
        checkKeyword();

//        if ((visit_count < 1) && (user != null)){
//            visit_count++;
//            Intent intent = new Intent(getApplicationContext(), ChoiceAge.class);
//            startActivity(intent);
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

    }

    // 현재 사용자가 키워드 선택했는지 확인하는 메서드
    // @return : true => 사용자가 키워드 선택
    // @return : false => 사용자가 키워드 선택 X
    public boolean checkKeyword(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){  // 현재 로그아웃 된 상태
            return false;
        } else {    // 로그인했지만, 키워드 선택안한 상태
            DocumentReference docRef = db.collection("users").document(user.getEmail());
            Task<DocumentSnapshot> userInfo = docRef.get();
            DocumentSnapshot document = userInfo.getResult();
            UserDTO userDTO = null;
            if (document.exists()) {
                userDTO = document.toObject(userDTO.getClass());
//                Log.d("userChooseKeyword", userDTO.getKeywords().get(0));
            }

        }
        return true;
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


}