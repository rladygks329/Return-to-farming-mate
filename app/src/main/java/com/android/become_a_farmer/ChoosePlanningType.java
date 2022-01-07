package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChoosePlanningType extends AppCompatActivity {
    private CheckBox chbox_quinong;
    private CheckBox chbox_quichon;
    private ImageButton btn_next_plan;
    private FirebaseFirestore db;
    private String planningType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_planning_type);

        chbox_quinong = (CheckBox) findViewById(R.id.chbox_quinong);
        chbox_quichon = (CheckBox) findViewById(R.id.chbox_quichon);

        chbox_quinong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planningType = "quinong";
            }
        });

        chbox_quichon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planningType = "quichon";
            }
        });

        // 계획 선택 후 다음 버튼 클릭 시, 귀농 or 귀촌 중 선택한 데이터 업데이트
        btn_next_plan = (ImageButton) findViewById(R.id.btn_next_plan);
        btn_next_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 사용자의 email이 존재할 때
                String email = getUserEmail();

                if (email != null){
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(email);
                    userRef.update("planningType", planningType)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(ChoosePlanningType.this, ChoosePreferredType.class);
                                    startActivity(intent);
                                }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error add user age", e);
                                }
                            });
                }else{
                    Toast.makeText(ChoosePlanningType.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();

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
}