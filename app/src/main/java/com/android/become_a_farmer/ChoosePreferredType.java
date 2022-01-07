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

public class ChoosePreferredType extends AppCompatActivity {
    private CheckBox chbox_nature;
    private CheckBox chbox_culture;
    private ImageButton btn_next_prefer;
    private FirebaseFirestore db;
    private String preferredType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_prefferd_type);

        chbox_nature = (CheckBox) findViewById(R.id.chbox_nature);
        chbox_culture = (CheckBox) findViewById(R.id.chbox_culture);

        chbox_nature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferredType = "nature";
            }
        });

        chbox_culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferredType = "culture";
            }
        });

        // 계획 선택 후 다음 버튼 클릭 시, 귀농 or 귀촌 중 선택한 데이터 업데이트
        btn_next_prefer = (ImageButton) findViewById(R.id.btn_next_prefer);
        btn_next_prefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 사용자의 email이 존재할 때
                String email = getUserEmail();

                if (email != null){
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(email);
                    userRef.update("preferredType", preferredType)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(ChoosePreferredType.this, ChooseKeyword.class);
                                    startActivity(intent);
                                }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error add user age", e);
                                }
                            });
                }else{
                    Toast.makeText(ChoosePreferredType.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return user.getEmail();
        }
        return null;
    }


}