package com.android.become_a_farmer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.become_a_farmer.databinding.ActivityChooseKeywordBinding;
import com.android.become_a_farmer.databinding.ViewKeywordBtnBinding;
import com.android.become_a_farmer.retrofit.DataClass;
import com.android.become_a_farmer.retrofit.RetrofitAPI;
import com.android.become_a_farmer.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseKeyword extends AppCompatActivity {
    private ActivityChooseKeywordBinding binding;
    private String keywords = "";
    private FirebaseFirestore db;
    private String str_checkedKeywords = "";
    private ArrayList<String> checkedKeywords;
    private String recommendRegions = "";
    private SharedPreferences pref;
    private Retrofit retrofit;
    private RetrofitAPI service;
    private static final String TAG = "[ChooseKeyword.java]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseKeywordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkedKeywords = new ArrayList<>();

        // retrofit
        retrofit = RetrofitClient.getInstance();
        service = retrofit.create(RetrofitAPI.class);
        keywords = "농촌,공동체,체험,행복,전통,꽃,미래, 세계";
//        getKeywordsFromServer();

        // 텍스트 색 변경
        String content = binding.titleKeyword.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        String word ="관심있는 키워드";
        int start = content.indexOf(word);
        int end = start + word.length();

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#23cd87")),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.titleKeyword.setText(spannableString);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);


        // ui 업데이트 위한 스레드
        setUI();

        // 키워드 선택 후 다음 버튼 클릭 시, 선택한 키워드 업데이트
        binding.btnNextKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedKeywords.size() < 3){
                    Toast.makeText(ChooseKeyword.this, "3개 이상 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = getUserEmail();
                if(email == null){
                    Toast.makeText(ChooseKeyword.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 현재 사용자의 email이 존재할 때
                // 키워드 리스트 -> 스트링
                str_checkedKeywords = listToString(checkedKeywords);
                db = FirebaseFirestore.getInstance();
                sendSelectedKeyword(str_checkedKeywords);
                DocumentReference userRef = db.collection("users").document(email);
                userRef.update("prefferdKeywords", str_checkedKeywords)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(ChooseKeyword.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "error add user age: " + e);
                            }
                        });
            }
        });

    }

    // 서버에서 받아온 keywords split해서 ui에 키워드 체크박스 추가함
    void setUI(){
        if(keywords == null){
            return;
        }
        storeKeyword(keywords);
        String[] s = keywords.split(",");

        List<String> strings = Arrays.asList(s);
        Iterator<String> it = strings.iterator();
        while(it.hasNext()){
            ViewKeywordBtnBinding v = ViewKeywordBtnBinding.inflate(getLayoutInflater());
            String k = it.next();
            v.keywordLeft.setTextOn(k);
            v.keywordLeft.setTextOff(k);
            v.keywordLeft.setTag(k);
            v.keywordLeft.setChecked(v.keywordLeft.isChecked()); //새로 고침
            v.keywordLeft.setOnClickListener(getOnClickSomething(v.keywordLeft));
            if(it.hasNext()){
                k = it.next();
                v.keywordRight.setTextOn(k);
                v.keywordRight.setTextOff(k);
                v.keywordRight.setTag(k);
                v.keywordRight.setChecked(v.keywordRight.isChecked()); //새로 고침
                v.keywordRight.setOnClickListener(getOnClickSomething(v.keywordRight));
            }else{
                //홀수일때는 오른쪽에 그려진 뷰를 가려준다.
                v.keywordRight.setVisibility(View.INVISIBLE);
            }
            binding.keywordLr.addView(v.getRoot());
        }
    }
    View.OnClickListener getOnClickSomething(final View view){
        return new View.OnClickListener(){
            // ※ 체크박스 클릭했다가 클릭 취소하는 경우 처리
            public void onClick(View v){    // 클릭했을 때 리스트에 없는 경우만 리스트에 추가
                String selectedWord = view.getTag().toString();
                if (!checkedKeywords.contains(selectedWord)){
                    checkedKeywords.add(selectedWord);
                } else { // 체크한 키워드가 이미 리스트에 있는 경우 리스트에서 삭제
                    checkedKeywords.remove(selectedWord);
                }
            }
        };
    }

    // 현재 사용자의 이메일 가져오기
    public String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return user.getEmail();
        }
        return null;
    }

    // arraylist -> string
    public String listToString(ArrayList<String> arr) {
        String res = "";
        for (String s : arr) {
            res += s + ",";
        }
        // 맨 마지막 , 지움
        res = res.substring(0, res.length() - 1);
        return res;
    }

    // db users 필드(추천 지역) 추가
    public void updateUserDataRegions(){
        // 현재 사용자의 email이 존재할 때
        String email = getUserEmail();
        if (email != null){
            DocumentReference userRef = db.collection("users").document(email);
            String[] recommendRegionsArray = recommendRegions.split(",");
            if (0 < recommendRegionsArray.length){
                userRef.update("recommendRegions", Arrays.asList(recommendRegionsArray));
            }

        }else{
            Toast.makeText(ChooseKeyword.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();

        }
    }

    // 서버에서 키워드 가져오기
    public void getKeywordsFromServer(){
        Call<DataClass> call = service.getKeywords();
        call.enqueue(new Callback<DataClass>() {
            @Override
            public void onResponse(Call<DataClass> call, Response<DataClass> response) {
                if(response.isSuccessful()){
                     keywords = response.body().toString();
                     Log.d(TAG, keywords);
                }
            }

            // 통신 실패
            @Override
            public void onFailure(Call<DataClass> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    // 선택한 키워드 서버에 보내고, 키워드 기반 추천 지역 받아오기
    public void sendSelectedKeyword(String selectedKeywords){
        DataClass data = new DataClass(selectedKeywords);
        service.getRegions(data).enqueue(new Callback<DataClass>() {
            @Override
            public void onResponse(Call<DataClass> call, Response<DataClass> response) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recommendRegions = response.body().toString();
//                Log.d("추천지역", recommendRegions);
                if (1 < recommendRegions.length()) {
                    updateUserDataRegions();    // db에 키워드 기반 추천 지역 업데이트
                }
            }

            @Override
            public void onFailure(Call<DataClass> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    // 서버에서 받은 키워드 SharedPreference에 저장해두기
    public void storeKeyword(String keywords){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("keywords", keywords);
        editor.commit();
    }
}