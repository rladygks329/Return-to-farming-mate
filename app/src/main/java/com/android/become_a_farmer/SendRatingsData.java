package com.android.become_a_farmer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.unsigned.UArraysKt;

/**
 * firestore에 저장된 모든 ratings 데이터를 서버에 보낸다.
 */
public class SendRatingsData {
//    private FirebaseFirestore db;
//    private String email;
//    private String recommendRegionsUserUser = "";
//
//    public SendRatingsData(FirebaseFirestore db, String email) {
//        this.db = db;
//        this.email = email;
//    }
//
//
//    // firestore에 저장된 rating JSON 형식으로 가져오고
//    // 가져온 ratings 서버에 보내기
//    public void getRatingFromDB(){
//        try{
//            // firestore에서 rating을 json 형식으로 바꾸기
//            db.collection("ratings")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                Map<String, JSONObject> hashMap = new HashMap();
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    try {
//                                        JSONObject jsonObject = mapToJSON(document.getData());
//                                        hashMap.put(document.getId(), jsonObject);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
////                                    Log.d("ratingData", document.getData().toString());
//                                }
//
////                                Log.d("json", new JSONObject(hashMap).toString());
//                                String ratingsJson = new JSONObject(hashMap).toString();
//
//                                // 파이썬 서버로 rating 데이터 전송
//                                getRecommendRegionFromUser(ratingsJson);
//
//                            }
//                        }
//
//                    });
//
//        } catch (Exception e){
//            Log.d(getClass().toString(), e.getMessage());
//        }
//    }
//
//    private JSONObject mapToJSON(Map<String, Object> map) throws JSONException {
//        JSONObject obj = new JSONObject();
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (value instanceof Map) {
//                Map<String, Object> subMap = (Map<String, Object>) value;
//                obj.put(key, mapToJSON(subMap));
//            } else if (value instanceof List) {
//                obj.put(key, listToJSONArray((List) value));
//            }
//            else {
//                obj.put(key, value);
//            }
//        }
//        return obj;
//    }
//
//    private JSONArray listToJSONArray(List<Object> list) throws JSONException {
//        JSONArray arr = new JSONArray();
//        for(Object obj: list) {
//            if (obj instanceof Map) {
//                arr.put(mapToJSON((Map) obj));
//            }
//            if(obj instanceof List) {
//                arr.put(listToJSONArray((List) obj));
//            }
//            else {
//                arr.put(obj);
//            }
//        }
//        return arr;
//    }
//
//    // 가져온 ratings와 이메일을 서버로 보내서 사용자기반 추천 지역 받아오기
//    public void getRecommendRegionFromUser(String ratings){
//        // ratings, email 보내기
//
//        // 사용자 기반 추천 지역 받아오기
//        recommendRegionsUserUser = new String(byteArr, 0, readByteCount, "UTF-8");
////                    Log.d("regions", recommendRegionsUserUser);
////
////        // 사용자 정보 업데이트(추천 지역명 필드에 추가)
////        if (recommendRegionsUserUser.length() > 0) {
////            updateUserDataRegions(email, recommendRegionsUserUser);
////        }
//
//    }
//
//
//    // user-user collaborate로 추천받은 지역을 user의 recommendRegions 필드 값에 추가시킨다.
//    private void updateUserDataRegions(String email, String regions){
//        // recommendRegions 필드 배열에 값 추가
//        DocumentReference docRef = db.collection("users").document(email);
//        String[] regionsList = regions.split(",");
//        if (0 < regionsList.length){
//            for (int i=0; i<regionsList.length; i++) {
//                docRef.update("recommendRegionsBasedUser", FieldValue.arrayUnion(regionsList[i]));
//            }
//        }
//
//    }


}
