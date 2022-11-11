package com.android.become_a_farmer.service;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.become_a_farmer.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class RecommendService {
    private final FirebaseFirestore db;
    private RecyclerViewAdapter rAdapter;

    public RecommendService(FirebaseFirestore db, RecyclerViewAdapter rAdapter) {
        this.db = db;
        this.rAdapter = rAdapter;
    }

    // 추천 지역명 db에서 가져옴
    public void getRecommendRegion(String email) {
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    List<String> recommendRegionsBasedKeyword;
                    // 1. 키워드 바탕으로 추천
                    try{
                        recommendRegionsBasedKeyword = (List<String>) document.getData()
                                                        .get("recommendRegions");
                    } catch (Exception e) {
                        recommendRegionsBasedKeyword = null;
                    }

                    // 2. 사용자 기반 추천
                    List<String> recommendRegionsBasedUser;
                    try{
                        recommendRegionsBasedUser = (List<String>) document.getData()
                                                    .get("recommendRegionsBasedUser");
                    } catch (Exception e){
                        recommendRegionsBasedUser = null;
                    }

                    // 키워드 기반 추천 지역이 있는 경우
                    if (recommendRegionsBasedKeyword != null) {
                        // 해당 지역 데이터 db에서 가져와서 화면에 뿌려줌
                        for (int i = 0; i < recommendRegionsBasedKeyword.size(); i++) {
                            getRegionDataFromKeyword(db, recommendRegionsBasedKeyword.get(i));
                        }
                    }

                    if (recommendRegionsBasedUser != null) {
                        // 추천 지역 데이터 db에서 가져와서 화면에 뿌려줌
                        for (int i = 0; i < recommendRegionsBasedUser.size(); i++) {
                            getRegionDataFromUser(db, recommendRegionsBasedUser.get(i));
                        }
                    }

                    // 추천 지역명이 없는 경우 N개 랜덤으로 뿌려줌
                    if ((recommendRegionsBasedKeyword == null) && (recommendRegionsBasedUser == null)) {
                        getAllRegion();
                    }


                } else {
                    // 서버에서 제대로 값을 못 받아올 경우
                }
            }


        });
    }


    /** 키워드 기반 추천 지역명의 데이터 가져오는 쿼리
        - 해당 추천 지역의 정보 리스트에 저장
        - 추천 '도' 에 해당하는 지역 2개씩 랜덤으로 뽑음 --> 랜덤으로 접근 불가..
    */
    public void getRegionDataFromKeyword(FirebaseFirestore db, String regionName){
        db.collection("regions")
                .whereEqualTo("do", regionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                String title = doc.getId();
                                String content = doc.get("introduction").toString();
                                String crop = doc.get("crop").toString();
                                String experienceContent = doc.get("experienceContent").toString();
                                String experienceTitle = doc.get("experienceTitle").toString();

                                rAdapter.addItem(title, " ", content, crop, experienceTitle,
                                        experienceContent);
                                rAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }



    /** 사용자 기반 추천 지역명의 데이터 가져오는 쿼리
     * - db에 저장된 user의 recommendRegionsByUser 필드 값과 regions의 문서 id값이 같아야 한다.
     * - 해당 추천 지역의 정보 리스트에 저장
     */
    public void getRegionDataFromUser(FirebaseFirestore db, String regionName){
        db.collection("regions")
                .whereEqualTo(String.valueOf(FieldPath.documentId()), regionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                // 리사이클뷰에 지역명 추가 -> 지역 리스트에 저장하고, 화면에 뿌림
                                String title = doc.getId();
                                String content = doc.get("introduction").toString();
                                String crop = doc.get("crop").toString();
                                String experienceContent = doc.get("experienceContent").toString();
                                String experienceTitle = doc.get("experienceTitle").toString();

                                rAdapter.addItem(title, " ", content, crop, experienceTitle,
                                        experienceContent);
                                rAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    // 모든 지역 데이터 뿌림
    public void getAllRegion(){
        CollectionReference ref = db.collection("regions");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String title = doc.getId().toString();
                        String content;
                        // 지역 리스트에 저장하고, 화면에 뿌림
                        if (doc.get("introduction").toString() == null){
                            content = "";
                        }else{
                            content = doc.get("introduction").toString();
                        }
                        String crop = doc.get("crop").toString();
                        String experienceContent = doc.get("experienceContent").toString();
                        String experienceTitle = doc.get("experienceTitle").toString();

                        rAdapter.addItem(title, " ", content, crop, experienceTitle,
                                experienceContent);
                        rAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }

    // 추천 지역이 없는 경우 랜덤으로 N개 뽑아서 노출
    void getRandomNRegion(int n){
        // then just get the document that falls under that random index

        CollectionReference ref = db.collection("regions");
        Random random = new Random();
        int randomNum = random.nextInt(n);


        for (int i=0; i < randomNum; i++) {
            ref.document();
        }
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String title = doc.getId().toString();
                        String content;
                        // 지역 리스트에 저장하고, 화면에 뿌림
                        if (doc.get("introduction").toString() == null){
                            content = "";
                        }else{
                            content = doc.get("introduction").toString();
                        }
                        String crop = doc.get("crop").toString();
                        String experienceContent = doc.get("experienceContent").toString();
                        String experienceTitle = doc.get("experienceTitle").toString();

                        rAdapter.addItem(title, " ", content, crop, experienceTitle,
                                experienceContent);
                        rAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

}
