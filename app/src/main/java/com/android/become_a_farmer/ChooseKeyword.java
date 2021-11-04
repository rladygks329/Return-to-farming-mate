package com.android.become_a_farmer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChooseKeyword extends AppCompatActivity {
    private Socket client;
    private String SERVER_IP = BuildConfig.SERVER_IP;
    private int PORT = 9090;
    private String keywords;
    private BufferedReader bufferedReader;
    private boolean condition = true;
    private LinearLayout ll;
    private ImageButton btn_next_keyword;
    private FirebaseFirestore db;
    private List<String> checkedKeywords = new ArrayList<String>();
    private String str_checkedKeywords;
    private DataInputStream dis;
    private DataOutputStream dos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_keyword);

        OkHttpClient okHttpClientclient = new OkHttpClient();
        okHttpClientclient.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
        okHttpClientclient.setReadTimeout(30, TimeUnit.SECONDS);    // socket timeout
        connect();  // 농촌,공동체,체험,행복,전통,꽃,미래, 세계

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUI();
                    }
                });
            }
        }).start();

        // 키워드 선택 후 다음 버튼 클릭 시, 선택한 데이터 업데이트
        btn_next_keyword = (ImageButton) findViewById(R.id.btn_next_keyword);
        for(String s : checkedKeywords){
            str_checkedKeywords += s + ",";
        }
        if (checkedKeywords.size() > 0){
            Log.d("checkedKeywords", str_checkedKeywords);
        }
        btn_next_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자
                // 현재 사용자의 email이 존재할 때
                String email = getUserEmail();

                if (email != null){
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(email);
                    userRef.update("prefferdKeywords", checkedKeywords)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(ChooseKeyword.this, MainActivity.class);
                                    startActivity(intent);
                                }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("error add user age", e);
                                }
                            });
                }else{
                    Toast.makeText(ChooseKeyword.this, "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    void setUI(){
        ll = (LinearLayout) findViewById(R.id.main_ll);
        Log.d("setui", "ui돌아가고 있음ㅁㅁㅁ");
        if(keywords!= null){
            String[] s = keywords.split(",");
            for (int i=0; i<s.length; i+=2) {
                CheckBox ch1 = new CheckBox(this);
                ch1.setText(s[i]);


                int idx = i + 1;
                CheckBox ch2 = new CheckBox(this);
//                ch2.setText(s[idx]);

                ch1.setId(i);
//                ch2.setId(idx);

                LinearLayout addll = new LinearLayout(this);
                addll.setOrientation(LinearLayout.HORIZONTAL);
//                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT);

                addll.addView(ch1);
                addll.addView(ch2);
                ll.addView(addll);

                ch1.setOnClickListener(getOnClickSomething(ch1));
                ch2.setOnClickListener(getOnClickSomething(ch2));
            }
        }

    }
    View.OnClickListener getOnClickSomething(final Button button){
        return new View.OnClickListener(){
            public void onClick(View v){
                checkedKeywords.add(button.getText().toString());
            }
        };
    }
    void connect(){
        Thread getKeywords = new Thread(){
            public void run(){
                try{    // 서버 접속
                    client = new Socket(SERVER_IP, PORT);

                    byte[] byteArr = new byte[1024];    // 데이터 가져오기
                    InputStream is = client.getInputStream();
                    int readByteCount = is.read(byteArr);
                    keywords = new String(byteArr, 0, readByteCount, "UTF-8");
                    is.close();
                    client.close();
//                    Log.d("keywords", keywords);
//                    if (keywords != null){
//                        Intent intent = new Intent(getApplicationContext(), ChooseKeyword.class);
//                        intent.putExtra("keywords", keywords);
//                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        getKeywords.start();
    }


    // 현재 사용자의 이메일 가져오기
    public String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return user.getEmail();
        }
        return null;
    }


}