package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

        if ((visit_count < 1) && (user != null)){
            visit_count++;
            Intent intent = new Intent(getApplicationContext(), ChoiceAge.class);
            startActivity(intent);
        }

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

//    public boolean checkKeyword(){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null){  // 현재 로그아웃 된 상태
//            return false;
//        } else {    // 로그인했지만, 키워드 선택안한 상태
//            if ()
//        }
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
        private static FirebaseFirestore db;
        private RecyclerView recyclerView;
        private RecyclerViewAdapter rAdapter;
        private ArrayList<RecyclerItem> rList = new ArrayList<>();
        Button button;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.activity_home_main, container, false);
            db = FirebaseFirestore.getInstance();

            button = (Button) view.findViewById(R.id.btn_test_login);
            recyclerView = (RecyclerView) view.findViewById(R.id.rv_lst);
            rAdapter = new RecyclerViewAdapter(rList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));



            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChooseKeyword.class);
                    startActivity(intent);
                }
            });

            // 회원가입하지 않았을 때 보이는 뷰
            // 파이어베이스에 저장된 지역데이터 뿌려줌
            CollectionReference ref = db.collection("regions");
            ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            String title = doc.getId().toString();
                            addItem(title, "sub");
                            Log.d("title: ", title);
                        }
                        rAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("데이터실패", "Error getting documents: ", task.getException());
                    }
                }
            });
            // 회원가입했을 때 보이는 뷰

            return view;
        }

        private void addItem(String title, String sub){
            RecyclerItem item = new RecyclerItem();
            item.setTitle(title);
            item.setSub(sub);
            rList.add(item);
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
        private Context context;

        public static user_main newInstance(){
            return new user_main();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.activity_user_main, container, false);

            Button btn_logout = (Button) view.findViewById(R.id.btn_logout);
            Button btn_login = (Button) view.findViewById(R.id.btn_login);

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
                    activity.finish();
                }
            });


            return view;
        }
    }


}