package com.pro3.coco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// ★================★=============== [ 결제 파트 하단 메뉴 test ] ==================★==================★

public class TicketActivity extends AppCompatActivity {
    Button btnReserv2, btnTicket2, btnMypage2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        Intent intent = getIntent();
        boolean loginCheck = intent.getBooleanExtra("loginOk", false);
        String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        Toast.makeText(getApplicationContext(), "결과: " + loginCheck, Toast.LENGTH_SHORT).show();

        btnReserv2 = findViewById(R.id.btnReserv2);
        btnTicket2 = findViewById(R.id.btnTicket2);
        btnMypage2 = findViewById(R.id.btnMypage2);

        //마이페이지 버튼
        btnMypage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginCheck){
                    Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                    intent.putExtra("loginOk", loginCheck);
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                    Toast.makeText(getApplicationContext(), "로그인 체크: " + loginCheck, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("loginOk", false);
                    startActivity(intent);
                }
            }
        });

        btnReserv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //MaintestActivity 대신 정훈씨 파트
                if (loginCheck){
                    Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                    intent.putExtra("loginOk", loginCheck);
                    Toast.makeText(getApplicationContext(), "로그인 체크: " + loginCheck, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                    intent.putExtra("loginOk", false);
                    startActivity(intent);
                }
            }
        });

    }
}