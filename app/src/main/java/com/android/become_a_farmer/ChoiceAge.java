package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.become_a_farmer.databinding.ActivityChoiceAgeBinding;
import com.android.become_a_farmer.service.AuthenticationService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChoiceAge extends AppCompatActivity {
    private ActivityChoiceAgeBinding binding;
    private FirebaseFirestore db;
    private String age;
    private AuthenticationService authenticationService;
    private static final String TAG = "[ChoiceAge.java]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoiceAgeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authenticationService = new AuthenticationService();

        // 텍스트 색 바꾸기
        String content = binding.textAge.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        String word ="연령대";
        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightGreen)),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.textAge.setText(spannableString);

        // spinner 생성
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        // spinner item 클릭 처리
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 나이 선택 후 다음 버튼 클릭 시, 해당 유저의 나이 데이터 업데이트
        binding.btnNextSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 사용자의 email이 존재할 때
                String email = authenticationService.getUserEmail();
                if (email != null){
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(email);
                    userRef.update("age", age)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(ChoiceAge.this, ChooseKeyword.class);
                                    startActivity(intent);
                                }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "error add user age: " + e);
                                }
                            });
                }else{
                    Toast.makeText(ChoiceAge.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

}