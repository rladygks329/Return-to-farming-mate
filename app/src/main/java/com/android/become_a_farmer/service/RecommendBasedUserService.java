package com.android.become_a_farmer.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.become_a_farmer.retrofit.DataClass;
import com.android.become_a_farmer.retrofit.RatingDataClass;
import com.android.become_a_farmer.retrofit.RetrofitAPI;
import com.android.become_a_farmer.retrofit.RetrofitClient;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecommendBasedUserService {
    private static RecommendBasedUserService recommendBasedUserService
            = new RecommendBasedUserService();
    private FirebaseFirestore db;
    private Retrofit retrofit = RetrofitClient.getInstance();
    private RetrofitAPI service = retrofit.create(RetrofitAPI.class);
    private String email = new AuthenticationService().getUserEmail();

    private RecommendBasedUserService() {}

    public static RecommendBasedUserService getInstance(){
        return recommendBasedUserService;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    // firestore에 저장된 rating JSON 형식으로 가져오고 가져온 ratings 서버에 보내기
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
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    try {
                                        JSONObject jsonObject = mapToJSON(document.getData());
                                        hashMap.put(document.getId(), jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                String ratingsJson = new JSONObject(hashMap).toString();

                                // 파이썬 서버로 rating 데이터 전송
                                sendRatingData(ratingsJson);

                            }
                        }

                    });

        } catch (Exception e){
            Log.e(getClass().toString(), e.getMessage());
        }
    }

    // 가져온 ratings와 이메일을 서버로 보내기
    public void sendRatingData(String ratings){
        RatingDataClass data = new RatingDataClass(email, ratings);
        service.sendRating(data).enqueue(new Callback<RatingDataClass>() {
            @Override
            public void onResponse(Call<RatingDataClass> call, Response<RatingDataClass> response) {
            }

            @Override
            public void onFailure(Call<RatingDataClass> call, Throwable t) {
                Log.e(getClass().toString(), t.getMessage());
            }
        });

    }


    // user-user collaborate로 추천받은 지역을 user의 recommendRegions 필드 값에 추가시킨다. --> 서버로 이전
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
}
