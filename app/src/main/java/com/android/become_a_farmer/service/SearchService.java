package com.android.become_a_farmer.service;

import androidx.annotation.NonNull;

import com.android.become_a_farmer.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchService {
    String text;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerViewAdapter rAdapter;

    public SearchService(String text, RecyclerViewAdapter rAdapter) {
        this.text = text;
        this.rAdapter = rAdapter;
    }

    public void search(){
        db.collection("regions").whereEqualTo("name", text)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            rAdapter.initItem();
                            if (task.getResult().isEmpty()){
                                rAdapter.initItem();
                                rAdapter.notifyDataSetChanged();
                            } else{
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

                        } else {

                        }
                    }
                });

    }


}
