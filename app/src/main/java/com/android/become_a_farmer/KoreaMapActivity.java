package com.android.become_a_farmer;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class KoreaMapActivity extends AppCompatActivity {
    ImageView img;
    Button btn;
    Button btn2;
    Button btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korea_map);

        img = (ImageView) findViewById(R.id.activityKrMapImg);
        btn = (Button) findViewById(R.id.activityKrMapBtn1);
        btn2 = (Button) findViewById(R.id.activityKrMapBtn2);
        btn3= (Button) findViewById(R.id.activityKrMapBtn3);

        /*
        vector drawable 속 각각의 구성요소의 색깔을 attrs.xml에서 참고하고 있음 ex) colorSeoul
        특정 지역 색깔이 다른 테마를 여러개 만들어서
        ContextThemeWrapper로 drawable을 불러온 후 theme를 변경 시켜서 특정 지역 색깔을 조절할 수 있음
        */
        final ContextThemeWrapper wrapper = new ContextThemeWrapper(this, R.style.DefaultMap);
        changeTheme(wrapper.getTheme());

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                wrapper.setTheme(R.style.SeoulMap);
                changeTheme(wrapper.getTheme());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                wrapper.setTheme(R.style.SouthJeollaMap);
                changeTheme(wrapper.getTheme());
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                wrapper.setTheme(R.style.NorthChungcheongMap);
                changeTheme(wrapper.getTheme());
            }
        });
    }

    private void changeTheme(Resources.Theme theme) {
        final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.korea_map, theme);
        img.setImageDrawable(drawable);
    }
}
