package com.pro3.coco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MypageActivity extends AppCompatActivity {
    TextView mypageName, mypageNum, btnMyLogout, btnMyExit;
    Button btnReserv3, btnTicket3;
    MemTBL_DBHelper memTBL_dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("                   마이페이지");
        memTBL_dbHelper = new MemTBL_DBHelper(this);
        Intent intent = getIntent();
        //기본값은 false여서 로그인 안 한 상태에선 마이페이지 메뉴 클릭 시 로그인 액티비티를 보여준다.
        //로그인 액티비티에서 로그인 성공으로 true값을 받으면 마이페이지를 보여준다!
        boolean loginCheck = intent.getBooleanExtra("loginOk", false);
        String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        //Toast.makeText(getApplicationContext(), "결과: " + loginCheck, Toast.LENGTH_SHORT).show();

        mypageName = findViewById(R.id.mypageName);
        mypageNum = findViewById(R.id.mypageNum);
        btnReserv3 = findViewById(R.id.btnReserv3);
        btnTicket3 = findViewById(R.id.btnTicket3);
        btnMyLogout = findViewById(R.id.btnMyLogout);
        btnMyExit = findViewById(R.id.btnMyExit);

        //받아온 로그인 정보(id, pw) 표현
        mypageName.setText(name + "님");
        mypageNum.setText("회원번호: " + number);

        btnReserv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginCheck){
                    Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                    intent.putExtra("loginOk", loginCheck);
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                    //Toast.makeText(getApplicationContext(), "로그인 체크: " + loginCheck, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                    intent.putExtra("loginOk", false);
                    startActivity(intent);
                }
            }
        });

        btnTicket3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //MaintestActivity 대신 정훈씨 파트
                if (loginCheck){
                    Intent intent = new Intent(getApplicationContext(), TicketActivity.class);
                    intent.putExtra("loginOk", loginCheck);
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                    //Toast.makeText(getApplicationContext(), "로그인 체크: " + loginCheck, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), TicketActivity.class);
                    intent.putExtra("loginOk", false);
                    startActivity(intent);
                }

            }
        });

        //로그아웃
        btnMyLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                intent.putExtra("loginOk", false);
                startActivity(intent);
            }
        });

        //회원탈퇴
        btnMyExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MypageActivity.this);
                dialog.setTitle("회원 탈퇴");
                dialog.setIcon(R.drawable.cocorail);
                dialog.setMessage("정말 회원 탈퇴를 하시겠습니까?");
                dialog.setNegativeButton("취소", null);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase();
                        String sql = "delete from memTBL2 where mName = '" + name + "';";
                        sqlDB.execSQL(sql);
                        Log.d("member_sqlite3DML", "데이터 삭제 성공...!!!");
                        Toast.makeText(getApplicationContext(), "회원 탈퇴 성공", Toast.LENGTH_SHORT).show();
                        sqlDB.close();
                        //자동 로그아웃
                        Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                        intent.putExtra("loginOk", false);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });

    }//onCreate

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