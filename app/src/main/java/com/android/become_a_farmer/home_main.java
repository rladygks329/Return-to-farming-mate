package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class home_main extends Fragment {
    private android.view.View view;
    private static FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter rAdapter;
    private ArrayList<String> regionList;
    private TextView txt_name;
    private UserDTO userDTO;
    private Button test;
    private SendRatingsData sendRatingsData;
    private String email;
    private Socket client;
    private String SERVER_IP = BuildConfig.SERVER_IP;
    private int PORT = 9090;
    private InputStream is;
    private DataOutputStream dos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home_main, container, false);
        db = FirebaseFirestore.getInstance();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_lst);
        rAdapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        txt_name = (TextView) view.findViewById(R.id.txt_name);
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null){      // 회원가입한 경우
//            email = user.getEmail();
//            setUserName(email, txt_name);   // 이름 화면에 표시
//            getRecommendRegionName(email);
//        } else {    // 회원가입하지 않았을 때 보이는 뷰 -> 파이어베이스에 저장된 지역데이터 뿌려줌
//            getAllRegion();
//        }
        email = getUserEmail();
        if (email != null){
            try {
                client = new Socket(SERVER_IP, PORT);
                dos = new DataOutputStream(client.getOutputStream());
                is = client.getInputStream();

                sendRatingsData = new SendRatingsData(db, email, client, is, dos);
                sendRatingsData.getRatingFromDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        String ratingFromDB = sendRatingsData.getRatingFromDB();

//        Log.d("json", ratingFromDB);
        return view;
    }

    // string -> arraylist 변환
    private ArrayList<String> toArrayList(String regions){
        String[] arr = regions.split(",");
        ArrayList<String> strRegions = new ArrayList<String>(Arrays.asList(arr));
        return strRegions;
    }

    // 사용자 이름 ui에 표시
    private void setUserName(String email, TextView txt_name){
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String name = document.getData().get("name").toString();
                    if (name != null) {
                        txt_name.setText(name);
                    }
                    else{   // 사용자의 이름이 없는 경우
                        txt_name.setText("");
                    }

                } else{
                    Log.d("fail", "Error getting documents:", task.getException());
                }
            }
        });
    }

    // 모든 지역 데이터 뿌림
    private void getAllRegion(){
        CollectionReference ref = db.collection("regions");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String title = doc.getId().toString();
                        // 지역 리스트에 저장하고, 화면에 뿌림
                        rAdapter.addItem(title, " ",
                                doc.get("introduction").toString());
                        rAdapter.notifyDataSetChanged();
                    }
                    rAdapter.notifyDataSetChanged();
                } else {
                }
            }
        });
    }

    // 추천 지역명의 데이터 getRegionDataByName 호출하여 가져옴
    private void getRecommendRegionName(String email){
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String recommendRegions = document.getData().get("recommendRegions").toString();

                    // 사용자의 추천 지역명 데이터가 있는 경우
                    if (recommendRegions != null) {
                        regionList = toArrayList(recommendRegions.toString());
                        // 추천 지역 데이터 db에서 가져와서 뿌려줌
                        for(int i=0; i<regionList.size(); i++){
                            getRegionDataByName(db, regionList.get(i));
                        }
                    } else{ // 사용자의 추천 지역명 데이터가 없는 경우 -> 파이어베이스에 저장된 지역데이터 뿌려줌

                    }

                } else{
                    Log.d("fail", "Error getting documents:", task.getException());
                }
            }
        });
    }

    // 추천 지역명으로 db에서 데이터 가져오는 쿼리
    private void getRegionDataByName(FirebaseFirestore db, String regionName){
        db.collection("regions")
                .whereEqualTo("do", regionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                // 리사이클뷰에 지역명 추가 -> 지역 리스트에 저장하고, 화면에 뿌림
                                rAdapter.addItem(document.getId(), " ",
                                        document.get("introduction").toString());
                                rAdapter.notifyDataSetChanged();
//                                    Log.d("introduction", document.get("introduction").toString());
                            }
                        } else{

                        }
                    }
                });
    }

    // 현재 사용자의 이메일 가져오기
    public String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return user.getEmail();
        }
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            dos.close();
            is.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            dos.close();
            is.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}