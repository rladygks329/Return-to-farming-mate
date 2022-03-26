package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText txt_id, txt_pwd, txt_check_pwd, txt_nickname, txt_name;
    private ImageView btn_register;
    private FirebaseAuth firebaseAuth;
    private ImageButton btn_prev;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_pwd = (EditText) findViewById(R.id.txt_pwd);
        txt_check_pwd = (EditText) findViewById(R.id.txt_check_pwd);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_nickname = (EditText) findViewById(R.id.txt_nickname);

        btn_register = (ImageView) findViewById(R.id.btn_register);
        btn_prev = (ImageButton) findViewById(R.id.btn_prev);

        firebaseAuth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(this);
    }
    // 다른 화면 터치 시 키보드 내림
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt_id.getWindowToken(), 0);
        return true;
    }

    // 사용자 정보 firestore에 저장(document : email)
    private void addUserData(String email, String pwd, String name, String nickname){
        // 사용자 데이터(userDTO) 생성한 후 fireStore에 저장
        UserDTO userDTO = new UserDTO(email, pwd, name, nickname);
        db.collection("users").document(email).set(userDTO)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 데이터 추가 성공 시
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("add user data", "Error writing document", e);
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                final String email = txt_id.getText().toString().trim();
                final String pwd = txt_pwd.getText().toString().trim();
                final String check_pwd = txt_check_pwd.getText().toString().trim();
                final String name = txt_name.getText().toString().trim();
                final String nickname = txt_nickname.getText().toString().trim();

                // 사용자 인증
                firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    addUserData(email, pwd, name, nickname);
                                    Toast.makeText(Register.this, "회원 가입을 완료했습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });




                break;



            case R.id.btn_prev:
                finish();
        }
    }
}

