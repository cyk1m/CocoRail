package com.pro3.coco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    //나는 깃에 올리려고 내용 수정 중이야~!!(*^▽^*)
    MemTBL_DBHelper memTBL_dbHelper;
    DatabaseReference database;
    ArrayList<mUser> arrayList;
    Toolbar main_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("                     로그인");

        memTBL_dbHelper = new MemTBL_DBHelper(this); //액티비티를 넘겨주겠어!
        database = FirebaseDatabase.getInstance().getReference("users"); //users에 대한 키만 여기 넣고, 나머지는 json파일에 있다.

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
        //===================회원번호 로그인(간편 로그인) 탭======================================
        Button btnNumLogin = logSnsView.findViewById(R.id.btnNumLogin); //껴진 뷰 안에서 findViewById 하는 것 주의!!
        EditText memSnsNum = logSnsView.findViewById(R.id.memSnsNum);
//        btnNumLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //dml => SQLiteDatabase
//                SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase(); //쓸 수 있는 stream을 가져와라.
//                //DB에 있는지 검색조회해서...Email pw를 가져와야
//                String memSnsNum2 = memSnsNum.getText().toString(); //내가 입력한 내 회원번호를
//                String sql = "select * from memTBL2 where mNum ='" + memSnsNum2 + "'"; //하나 검색
//                Cursor cursor = sqlDB.rawQuery(sql, null); //이동하며 검색결과를 꺼내줌
////                while (cursor.moveToNext()){ //있는지없는지 확인. //jdbc는 1번부터 시작했지만, 안드로이드는 0번부터 시작
////                    //cursor.moveToNext() 첫번째 행을 가르키면서 있는지 없는지 체크, 있으면 true 리턴
////                    //각 열에 있는 값들을 인덱스로 꺼내오면 됨. 0부터 시작!
////                    String result = cursor.getString(0) + ": " + cursor.getString(5) + ", " + cursor.getString(2);
////                    Log.d("member_sqlite3DML", result); //하나 찍는 것
////                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
////                }
//                if (cursor.moveToNext()){ //로그인 성공 시
//                    //있는지없는지 확인. //jdbc는 1번부터 시작했지만, 안드로이드는 0번부터 시작
//                    //cursor.moveToNext() 첫번째 행을 가르키면서 있는지 없는지 체크, 있으면 true 리턴
//                    //각 열에 있는 값들을 인덱스로 꺼내오면 됨. 0부터 시작!
//                    String result = cursor.getString(0) + ": " + cursor.getString(5) + ", " + cursor.getString(2);
//                    String email = cursor.getString(5);
//                    //String pw = cursor.getString(2);
//                    //sqlite에 있는 이메일, 패스워드가 파베에 있는지 확인..근데 이거 이상하다. 그럼 파베에 등록부터할때는 sqlite에 없잖아
//                    Log.d("member_sqlite3DML", result); //하나 찍는 것
//                    numAlert(email + "\n등록된 이메일로 로그인합니다.");
//
//                }else{ //로그인 실패 시
//                    numAlert2("등록된 회원 정보가 없습니다.");
//                }
//                cursor.close();
//                sqlDB.close();
//            }
//        });
        //DB에서 가져오는 유저들의 목록을 넣을 공간
        arrayList = new ArrayList<>();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //snapshot이 node같은 것
                //users에 해당하는 자식들 다 가져오기. 유저들의 목록을 가져오는 메서드
                arrayList.clear(); //한번 비워주기
                Log.d("파이어베이스>>", "users아래의 자식들의 개수: " + snapshot.getChildrenCount());
                Log.d("파이어베이스>>", "전체 json 목록 가지고 온 것: " + snapshot.getChildren());
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //users아래에 있는 user 목록을 다 가져온다.
                    //DataSnapshot => for문으로 목록에 들어있는 user를 한명씩 꺼내줌.
                    //user의 값들을 가지고와서, User vo에 넣는다.
                    //getValue(User.class) => 해당하는 멤버변수과 동일한 set메서드를 자동을 부른다.
                    Log.d("파이어베이스>>", "하나의 snapshot: " + snapshot1);
                    Log.d("파이어베이스>>", "하나의 snapshot value: " + snapshot1.getValue());
                    mUser user = snapshot1.getValue(mUser.class); //getValue 3번째거, 가방 클래스 지정만 해주면 됨
                    arrayList.add(user);
                    Log.d("파이어베이스>>", "user 1명: " + user);
                }
                Log.d("파이어베이스>>", "user 목록 전체: " + arrayList);
                Log.d("파이어베이스>>", "user 목록 개수: " + arrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnNumLogin.setOnClickListener(new View.OnClickListener() {
            //이거는 sqlite의 회원번호로 파베의 이메일 pw를 불러오는...
            @Override
            public void onClick(View v) {
                final String memSnsNum2 = memSnsNum.getText().toString();

                database.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUser user = snapshot.getValue(mUser.class);
                        Log.d("파이어베이스>> ", userId + ": userId 상세정보: " + user);
                        et_user_name.setText(user.userName);
                        et_user_email.setText(user.email);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { //ajax에서 error 기능
                        Log.d("파이어베이스>> ", userId + ": userId 없음");
                    }
                });
            }
        });

        //================================== 탭 ======================================
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














    }//onCreate

    //유효성 검사 alert 띄우기
    void numAlert(String text){ //로그인 성공 시
        AlertDialog.Builder a = new AlertDialog.Builder(LoginActivity.this);
        a.setTitle("로그인");
        a.setIcon(R.drawable.cocorail);
        a.setMessage(text);
//        a.setPositiveButton("확인", null);
        a.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "main으로 이동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                startActivity(intent);
            }
        });
        a.show();
    }
    void numAlert2(String text){ //로그인 실패 시
        AlertDialog.Builder a = new AlertDialog.Builder(LoginActivity.this);
        a.setTitle("로그인");
        a.setIcon(R.drawable.cocorail);
        a.setMessage(text);
        a.setPositiveButton("확인", null);
        a.show();
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