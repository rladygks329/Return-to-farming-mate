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
    private FirebaseFirestore db;
    private String email;
    private Socket client;
    private InputStream is;
    private DataOutputStream dos;
    private String SERVER_IP = BuildConfig.SERVER_IP;
    private int PORT = 9000;
    private int gubun;
    private String recommendRegionsUserUser = "";

    public SendRatingsData(FirebaseFirestore db, String email) {
        this.db = db;
        this.email = email;
    }

//    public SendRatingsData(FirebaseFirestore db, String email, Socket client, InputStream is,
//                           DataOutputStream dos) {
//        this.db = db;
//        this.email = email;
//        this.client = client;
//        this.is = is;
//        this.dos = dos;
//    }

    // firestore에 저장된 rating JSON 형식으로 가져오고
    // 가져온 ratings 서버에 보내기
    public void getRatingFromDB(){
        try{
            // firestore에서 rating을 json 형식으로 바꾸기
            db.collection("ratings")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, JSONObject> hashMap = new HashMap();
                                String oneRecord = "";
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    try {
                                        JSONObject jsonObject = mapToJSON(document.getData());
//                                        oneRecord = document.getId() + " : " + jsonObject.toString();
//                                        allJsonList.add(oneRecord);
                                        hashMap.put(document.getId(), jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                    Log.d("ratingData", document.getData().toString());
                                }

//                                Log.d("json", new JSONObject(hashMap).toString());
                                String ratingsJson = new JSONObject(hashMap).toString();

                                // 파이썬 서버로 rating 전송
                                sendRatingAndGetRegions(ratingsJson);

                            } else { // 아직 rating 정보가 없는 경우
//                                Log.d("SendRatingsData.class", "Error getting documents: ", task.getException());
                            }
                        }

                    });

        } catch (Exception e){
            Log.d(getClass().toString(), e.getMessage());
        }
    }

    private JSONObject mapToJSON(Map<String, Object> map) throws JSONException {
        JSONObject obj = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<String, Object> subMap = (Map<String, Object>) value;
                obj.put(key, mapToJSON(subMap));
            } else if (value instanceof List) {
                obj.put(key, listToJSONArray((List) value));
            }
            else {
                obj.put(key, value);
            }
        }
        return obj;
    }

    private JSONArray listToJSONArray(List<Object> list) throws JSONException {
        JSONArray arr = new JSONArray();
        for(Object obj: list) {
            if (obj instanceof Map) {
                arr.put(mapToJSON((Map) obj));
            }
            if(obj instanceof List) {
                arr.put(listToJSONArray((List) obj));
            }
            else {
                arr.put(obj);
            }
        }
        return arr;
    }

    // 가져온 ratings를 서버로 보내고 추천지역 받아오기
    public void sendRatingAndGetRegions(String ratings){
        if (ratings != null) {
            connect(ratings);
        }
    }


    // 서버와 연결
    void connect(String ratings){

        Thread sendRatings = new Thread(){
            public void run(){
                Log.d("ddd", "ChooseKeyword 돌아감!!!");
                try{
                    client = new Socket(SERVER_IP, PORT);
                    dos = new DataOutputStream(client.getOutputStream());
                    is = client.getInputStream();

//                    gubun = 2;
//                    dos.writeUTF(Integer.toString(gubun));
//                    dos.flush();

                } catch (Exception e){
                    Log.d(getClass().toString(), e.getMessage());
                }

                try{
                    dos.writeUTF(ratings);  // rating 보내기
                    dos.flush();
                } catch (Exception e) {
                    Log.d(getClass().toString(), e.getMessage());
                }

                try{
                    dos.writeUTF(email);    // 이메일 보내기
                    dos.flush();
                } catch (Exception e) {
                    Log.d(getClass().toString(), e.getMessage());
                }

                try{
                    byte[] byteArr = new byte[1024];    // 추천 지역명 서버에서 받아오기(user-user collaborate)
                    int readByteCount = is.read(byteArr);
                    recommendRegionsUserUser = new String(byteArr, 0, readByteCount, "UTF-8");
//                    Log.d("regions", recommendRegionsUserUser);

                    // 사용자 정보 업데이트(추천 지역명 필드에 추가)
                    if (recommendRegionsUserUser.length() > 0) {
                        updateUserDataRegions(email, recommendRegionsUserUser);
                    }
                    dos.close();
                    is.close();
                    client.close();

                } catch (Exception e){
                    Log.d(getClass().toString(), e.getMessage());
                }
            }
        };
        sendRatings.start();
    }

    // user-user collaborate로 추천받은 지역을 user의 recommendRegions 필드 값에 추가시킨다.
    private void updateUserDataRegions(String email, String regions){
        // recommendRegions 필드 배열에 값 추가
        DocumentReference docRef = db.collection("users").document(email);
        String[] regionsList = regions.split(",");
        if (0 < regionsList.length){
            for (int i=0; i<regionsList.length; i++) {
                docRef.update("recommendRegionsBasedUser", FieldValue.arrayUnion(regionsList[i]));
            }
        }

    }


}
