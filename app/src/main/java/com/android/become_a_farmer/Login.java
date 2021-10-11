package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private ImageView btn_login;
    private ImageView btn_register;
    private EditText txt_id;
    private EditText txt_pwd;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (ImageView) findViewById(R.id.btn_login);
        btn_register = (ImageView) findViewById(R.id.btn_resgister);
        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_pwd = (EditText) findViewById(R.id.txt_pwd);
        firebaseAuth = firebaseAuth.getInstance();

        btn_login.setOnClickListener(this); // 로그인 버튼 클릭
        btn_register.setOnClickListener(this); // 회원가입 버튼 클릭


    }
    // 다른 화면 터치 시 키보드 내림
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt_id.getWindowToken(), 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String email = txt_id.getText().toString().trim();
                String pwd = txt_pwd.getText().toString().trim();
                // 이메일로 로그인
                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){ // 로그인 성공
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else{ // 로그인 실패
                                    Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case  R.id.btn_resgister:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;

        }
    }
}