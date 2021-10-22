package com.pro3.coco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {
    Toolbar main_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("                     로그인");

//어떤 l.l에 분리시켜놓은 layout파일을 넣을지 결정!
        LinearLayout layout1 = findViewById(R.id.numLoginTab); //l.l객체 가져오기
        LinearLayout layout2 = findViewById(R.id.telLoginTab);
        LinearLayout layout3 = findViewById(R.id.snsLoginTab);

        //분리시켜놓은 xml파일(layout)들을 객체화 시키자! = inflation
        View logNumView = View.inflate(LoginActivity.this, R.layout.login_number, null);
        View logTelView = View.inflate(LoginActivity.this, R.layout.login_tel, null);
        View logSnsView = View.inflate(LoginActivity.this, R.layout.login_sns, null);
        //끼우기!
        layout1.addView(logNumView);
        layout2.addView(logTelView);
        layout3.addView(logSnsView);

//        ImageView imageView = songView.findViewById(R.id.imageView4);
        Button btnSignUp = logNumView.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
//
//        Button btnChange2 = artView.findViewById(R.id.btnChange2); //껴진 뷰 안에서 findViewById 하는 것 주의!!
//        ImageView imageView2 = artView.findViewById(R.id.imageView2);
//        TextView text4 = artView.findViewById(R.id.text4);
//        btnChange2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imageView2.setImageResource(R.drawable.sbm1);
//                text4.setText("아티스트입니다!!!!!!");
//            }
//        });

        //이 안에서 쓰는거라 굳이 전역변수로 안해줘도 됨
        TabHost tabHost = findViewById(R.id.tabhost); //탭 하나 당 탭호스트 1개
        tabHost.setup(); //기초적인 tab 초기화! 인식시켜주는 부분

        //각 탭마다의 설정을 넣음
        TabHost.TabSpec tabSpecNum = tabHost.newTabSpec("logNum").setIndicator("회원번호"); //index 0번
        tabSpecNum.setContent(R.id.numLoginTab); //layout을 끼워주기
        tabHost.addTab(tabSpecNum); //탭 끼워주기

        TabHost.TabSpec tabSpecTel = tabHost.newTabSpec("logTel").setIndicator("휴대폰번호"); //1
        tabSpecTel.setContent(R.id.telLoginTab); //내용 끼워주기
        tabHost.addTab(tabSpecTel); //탭 끼워주기

        TabHost.TabSpec tabSpecSns = tabHost.newTabSpec("logSns").setIndicator("간편로그인"); //2
        tabSpecSns.setContent(R.id.snsLoginTab); //내용 끼워주기
        tabHost.addTab(tabSpecSns); //탭 끼워주기

        tabHost.setCurrentTab(0);














    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }//toolbar
}