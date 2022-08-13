package com.android.become_a_farmer.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.become_a_farmer.Login;

public class LoginService {

    /**
     * 아이디와 비밀번호를 입력하지 않을 경우 처리하는 로직
     * @param email 입력한 아이디 텍스트
     * @param pwd   입력한 비밀번호 텍스트
     * @param context   해당 화면
     * @return false: 빈 입력값 없음 | true: 빈 입력값 있음
     */

    public boolean checkBlank(String email, String pwd, Context context){
        boolean isBlank = false;

        if (email.length() == 0) {
            if (pwd.length() == 0) {
                Toast.makeText(context, "아이디와 비밀번호를 입력해주세요!", Toast.LENGTH_LONG).show();

            } else{
                Toast.makeText(context, "아이디를 입력해주세요!", Toast.LENGTH_LONG).show();
            }

            isBlank = true;
        }

        else if (pwd.length() == 0) {
            Toast.makeText(context, "비밀번호를 입력해주세요!", Toast.LENGTH_LONG).show();
            isBlank = true;
        }

        return isBlank;

    }
}
