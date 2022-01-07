package com.android.become_a_farmer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * firestore에 저장된 모든 ratings 데이터를 서버에 보낸다.
 */
public class SendRatingsData {
    private FirebaseFirestore db;
    private String email;

    public SendRatingsData(FirebaseFirestore db, String email) {
        this.db = db;
        this.email = email;
    }

    // firestore에 저장된 rating 가져오기
    public void getRatingFromDB(){
        try{
            db.collection("ratings")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("SendRatingsData.class", document.getId());
                                }
                            } else { // 아직 rating 정보가 없는 경우
//                                Log.d("SendRatingsData.class", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } catch (Exception e){
//            Log.d("error!!!!", e.getMessage());
        }
    }

    // 가져온 ratings를 서버로 보내기

}
