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
import java.io.OutputStream;
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
    private String str_checkedKeywords = "";
    private DataInputStream dis;
    private DataOutputStream dos;
    private ArrayList<String> checkedKeywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_keyword);
        checkedKeywords = new ArrayList<>();
        OkHttpClient okHttpClientclient = new OkHttpClient();
        okHttpClientclient.setConnectTimeout(30, TimeUnit.SECONDS); // connect timeout
        okHttpClientclient.setReadTimeout(30, TimeUnit.SECONDS);    // socket timeout
        connect();  // 농촌,공동체,체험,행복,전통,꽃,미래, 세계

        // ui 업데이트 위한 스레드
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



        // 키둬으 선택 후 다음 버튼 클릭 시, 선택한 키워드 업데이트
        btn_next_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 키워드 리스트 -> 스트링
                str_checkedKeywords = listToString(checkedKeywords);

                // 현재 사용자의 email이 존재할 때
                String email = getUserEmail();

                if (email != null){
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(email);
                    userRef.update("prefferdKeywords", str_checkedKeywords)
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

                // 사용자가 선택한 키워드를 서버에 전송함
                connect2();
            }
        });

    }

    // 서버에서 받아온 keywords split해서 ui에 키워드 체크박스 추가함
    void setUI(){
        ll = (LinearLayout) findViewById(R.id.main_ll);
//        Log.d("setui", "ui돌아가고 있음ㅁㅁㅁ");
        if(keywords!= null){
            String[] s = keywords.split(",");
            for (int i=0; i<s.length; i+=2) {
                CheckBox ch1 = new CheckBox(this);
                ch1.setText(s[i]);
                ch1.setId(i);

                int idx = i + 1;
                CheckBox ch2 = new CheckBox(this);
                ch2.setText(s[idx]);
                ch2.setId(idx);

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
            // ※ 체크박스 클릭했다가 클릭 취소하는 경우 처리
            public void onClick(View v){    // 클릭했을 때 리스트에 없는 경우만 리스트에 추가
                if (!checkedKeywords.contains(button.getText().toString())){
                    checkedKeywords.add(button.getText().toString());
                } else { // 체크한 키워드가 이미 리스트에 있는 경우 리스트에서 삭제
                    checkedKeywords.remove(button.getText().toString());
                }
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

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        getKeywords.start();
    }

    void connect2(){
        Thread sendKeywords = new Thread(){
            public void run(){
                try{    // 서버 접속
                    client = new Socket(SERVER_IP, PORT);
                    DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                    dos.writeUTF(str_checkedKeywords);
                    dos.close();
                    client.close();

                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        sendKeywords.start();
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


}