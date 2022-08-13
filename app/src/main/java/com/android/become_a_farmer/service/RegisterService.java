package com.android.become_a_farmer.service;

import android.content.Context;
import android.widget.Toast;

public class RegisterService {

    /**
     * 사용자가 모든 회원 정보를 입력하지 않았을 때 false를 리턴하는 함수
     * @param email
     * @param pwd
     * @param check_pwd
     * @param name
     * @param nickname
     * @param context
     * @return true: 모든 정보를 입력한 경우 | false: 모든 정보를 입력하지 않은 경우
     */
    public boolean checkBlank(String email, String pwd, String check_pwd,
                              String name, String nickname, Context context){
        boolean isBlank = false;

        if ((email.length() == 0) || (pwd.length() == 0) || (check_pwd.length() == 0) ||
                (name.length() == 0) ||  (nickname.length() == 0)) {

            Toast.makeText(context, "회원 정보를 모두 입력해주세요!", Toast.LENGTH_LONG).show();
            isBlank = true;
        }
        return isBlank;

    }

    /**
     * 비밀번호와 확인 비밀번호가 일치하는지 확인하는 함수
     * @param pwd
     * @param check_pwd
     * @param context
     * @return true: 비밀번호 == 확인 비밀번호 | false: 비밀번호 != 확인 비밀번호
     */
    public boolean checkCorrectPwd(String pwd, String check_pwd, Context context){
        boolean isCorrect = true;

        if (!pwd.equals(check_pwd)) {
            Toast.makeText(context, "회원 정보를 모두 입력해주세요!", Toast.LENGTH_LONG).show();
            isCorrect = false;
        }

        return isCorrect;
    }
}
