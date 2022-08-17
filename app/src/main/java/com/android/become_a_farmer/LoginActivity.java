package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.become_a_farmer.databinding.ActivityLoginBinding;
import com.android.become_a_farmer.service.LoginService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //null exception을 막아줌
        firebaseAuth = FirebaseAuth.getInstance();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLoginPage.setOnClickListener(this); // 로그인 버튼 클릭
        binding.btnResgister.setOnClickListener(this); // 회원가입 버튼 클릭

        String content = binding.text.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        String word ="떠나볼까요?";
        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.darkGray)),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.text.setText(spannableString);

    }
    // 다른 화면 터치 시 키보드 내림
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.txtId.getWindowToken(), 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_page:

              String email = binding.txtId.getText().toString().trim();
              String pwd = binding.txtPwd.getText().toString().trim();
                LoginService loginService = new LoginService();
                // 빈 입력값 확인
                boolean checkBlank = loginService.checkBlank(email, pwd, Login.this);

                // 이메일로 로그인
                if (!checkBlank){
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){ // 로그인 성공
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else{ // 로그인 실패
                                        Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                break;

            case  R.id.btn_resgister:
                Intent intent = new Intent(LoginActivity.this, ResisterActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}
