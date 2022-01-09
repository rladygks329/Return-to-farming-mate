package com.android.become_a_farmer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * firestore에 저장된 모든 ratings 데이터를 서버에 보낸다.
 */
public class SendRatingsData {
    private FirebaseFirestore db;
    private String email;
    String allJson = "";


    public SendRatingsData(FirebaseFirestore db, String email) {
        this.db = db;
        this.email = email;
    }

    // firestore에 저장된 rating JSON 형식으로 가져오기
    public String getRatingFromDB(){
        try{
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

                                Log.d("json", new JSONObject(hashMap).toString());

                            } else { // 아직 rating 정보가 없는 경우
//                                Log.d("SendRatingsData.class", "Error getting documents: ", task.getException());
                            }
                        }

                    });
//            Log.d("sendJson", allJson);
//            ratingJSONListener.onRatingJSONLoaded(allJson);
//            Log.d("sibal", sibal.get(0));

        } catch (Exception e){
//            Log.d("error!!!!", e.getMessage());
        }

        return allJson;
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

    // 가져온 ratings를 서버로 보내기

}
