package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class ChoiceAge extends AppCompatActivity {
    private Spinner spinner;
    private String age;
    private ImageButton btn_next;
    private FirebaseFirestore db;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_age);

        // spinner 생성
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // spinner item 클릭 처리
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                age = parent.getItemAtPosition(position).toString();
            }

        });



        // 나이 선택 후 다음 버튼 클릭 시, 해당 유저의 나이 데이터 업데이트
        btn_next = (ImageButton) findViewById(R.id.btn_next_spinner);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 사용자의 email이 존재할 때
                email = getUserEmail();
                if (email != null){
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(email);
                    userRef.update("age", age)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("add user age", "DocumentSnapshot successfully updated!");
                                    Intent intent = new Intent(ChoiceAge.this, ChoosePlanningType.class);
                                    startActivity(intent);
                                }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error add user age", e);
                                }
                            });
                }else{
                    Toast.makeText(ChoiceAge.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();

                }
//                Intent intent = new Intent(ChoiceAge.this, ***);
//                startActivity(intent);

            }
        });


    }
    // 현재 사용자의 이메일 가져오기
    public String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            Log.d("user email : " , user.getEmail());
            return user.getEmail();
        }
        return null;
    }

}