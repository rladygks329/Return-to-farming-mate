package com.android.become_a_farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChooseKeyword extends AppCompatActivity {
    private Socket client;
    private Handler mHandler;
    private DataOutputStream dataOutput;
    private String SERVER_IP = getResources().getString(R.string.my_ip_key);
    private int port = 8080;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_keyword);


    }

    void connect(){
        mHandler = new Handler();
        Thread getText = new Thread(){
            public void run(){
                // 서버 접속
                try{
                    client = new Socket(SERVER_IP, port);
                    Log.d("서버 접속 됨", "서버 접속 됨");
                } catch (IOException e){
                    Log.d("서버 접속 못 함", "서버 접속 못함");
                    e.printStackTrace();
                }
            }
        };
    }
}