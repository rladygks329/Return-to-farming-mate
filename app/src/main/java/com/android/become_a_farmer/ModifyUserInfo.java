package com.android.become_a_farmer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModifyUserInfo extends AppCompatActivity {
    private android.view.View view;
    private EditText edt_txt_age;
    private EditText edt_txt_nickname;
    private EditText edt_txt_name;
    private Button btn_ok;
    private static FirebaseFirestore db;
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        edt_txt_age = findViewById(R.id.edt_txt_age);
        edt_txt_nickname = findViewById(R.id.edt_txt_nickname);
        edt_txt_name = findViewById(R.id.edt_txt_name);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        email = getUserEmail();
        db = FirebaseFirestore.getInstance();

        // db에 저장된 사용자정보 가져와서 화면에 뿌림
        getUserInfo();

        // 사용자가 정보 변경하고 완료버튼 클릭하면 db에 변경사항 업데이트
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo(edt_txt_age.getText().toString(), edt_txt_name.getText().toString(),
                        edt_txt_nickname.getText().toString());
            }
        });
    }

    public void getUserInfo(){
        DocumentReference docRef = db.collection("users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String age = document.getData().get("age").toString();
                    String name = document.getData().get("name").toString();
                    String nickname = document.getData().get("nickname").toString();
                    edt_txt_age.setText(age);
                    edt_txt_name.setText(name);
                    edt_txt_nickname.setText(nickname);

                } else{
                    Log.d("fail", "Error getting documents:", task.getException());
                }
            }
        });
    }

    public void updateUserInfo(String age, String name, String nickname){
        DocumentReference userRef = db.collection("users").document(email);
        userRef.update("age", age)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error add user age", e);
                    }
                });


        userRef.update("name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error add user age", e);
                    }
                });

        userRef.update("nickname", nickname)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error add user age", e);
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