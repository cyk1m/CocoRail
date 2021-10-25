package com.pro3.coco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    //나는 깃에 올리려고 내용 수정 중이야~!!(*^▽^*)
    MemTBL_DBHelper memTBL_dbHelper;
    DatabaseReference database;
    WebView webView;
    LinearLayout allspaceLL, pwChgLL;
    EditText yourId, yourPw;
    //로그인 여부 판단할 변수 (세션 대용)
    boolean loginCheck = false;
    Toolbar main_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("                     로그인");

        memTBL_dbHelper = new MemTBL_DBHelper(this); //액티비티를 넘겨주겠다!
        database = FirebaseDatabase.getInstance().getReference("users"); //users에 대한 키만 여기 넣고, 나머지는 json파일에 있다.
        allspaceLL = findViewById(R.id.allspaceLL);

        //어떤 l.l에 분리시켜놓은 layout파일을 넣을지 결정!
        LinearLayout layout1 = findViewById(R.id.numLoginTab); //l.l객체 가져오기
        LinearLayout layout2 = findViewById(R.id.snsLoginTab);

        //분리시켜놓은 xml파일(layout)들을 객체화 시키자! = inflation
        View logNumView = View.inflate(LoginActivity.this, R.layout.login_number, null);
        View logSnsView = View.inflate(LoginActivity.this, R.layout.login_sns, null);
        //끼우기!
        layout1.addView(logNumView);
        layout2.addView(logSnsView);

//======================================= 1. 폰 번호 본인인증 일반 로그인 탭 ==========================================

        //로그인 버튼
        Button btnLogin = logNumView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText memID = logNumView.findViewById(R.id.memID);
                EditText memPW = logNumView.findViewById(R.id.memPW);
                String id = memID.getText().toString();
                String pw = memPW.getText().toString();
                SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase();
                String sql2 = "select * from memTBL2 where mId ='" + id + "'"; //하나 검색
                Cursor cursor = sqlDB.rawQuery(sql2, null); //이동하며 검색결과를 꺼내줌
                if (cursor.moveToNext()) {
                    Log.d("member_sqlite3DML", cursor.getString(0));
                    if (id.equals(cursor.getString(1)) && pw.equals(cursor.getString(2))){
                        //id, pw 일치
                        AlertDialog.Builder a = new AlertDialog.Builder(LoginActivity.this);
                        a.setTitle("로그인");
                        a.setIcon(R.drawable.cocorail);
                        a.setMessage("로그인 성공");
                        a.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                                loginCheck = true;
                                intent.putExtra("loginOk", loginCheck);
                                intent.putExtra("name", cursor.getString(3));
                                intent.putExtra("number", cursor.getString(0));
                                //Toast.makeText(getApplicationContext(), "로그인 체크: " + loginCheck, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        });
                        a.show();
                    }else{
                        //id, pw 불일치
                        numAlert2("입력한 아이디와 패스워드를 확인해주세요.");
                    }
                }
            }
        });

        //회원가입 버튼
        Button btnSignUp = logNumView.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        //비밀번호 찾기
        TextView memPwFind = logNumView.findViewById(R.id.memPwFind);
        memPwFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //자바파일과 필요한 레이아웃 파일이 합쳐지는 것이 인플레이션
                View dialogView = View.inflate(LoginActivity.this, R.layout.mem_pw_find, null); //파라메터 3개
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("비밀번호 찾기");
                dialog.setIcon(R.drawable.cocorail);
                dialog.setView(dialogView);
                pwChgLL = dialogView.findViewById(R.id.pwChgLL);
                pwChgLL.setVisibility(View.INVISIBLE);
                yourId = dialogView.findViewById(R.id.yourId); //다이얼로그에 있는 변수는 여기서 findView 해줘야
                yourPw = dialogView.findViewById(R.id.yourPw);
                TextView dialog_idcheck = dialogView.findViewById(R.id.dialog_idcheck);
                TextView dialog_pwcheck = dialogView.findViewById(R.id.dialog_pwcheck);
                Button btnIdCheck = dialogView.findViewById(R.id.btnIdCheck);
                Button btnPwChange = dialogView.findViewById(R.id.btnPwChange);
                //존재하는 id 정보인지 체크
                btnIdCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String yourId2 = yourId.getText().toString();
                        String yourPw2 = yourPw.getText().toString();
                        //if id가 db에 있으면, pw를 update하기
                        SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase();
                        String sql = "select * from memTBL2 where mId ='" + yourId2 + "'"; //하나 검색
                        Cursor cursor = sqlDB.rawQuery(sql, null); //이동하며 검색결과를 꺼내줌
                        if (cursor.moveToNext() && yourId2.equals(cursor.getString(1))) {
                            int blue = ContextCompat.getColor(getApplicationContext(), R.color.blue);
                            dialog_idcheck.setTextColor(blue);
                            dialog_idcheck.setText("ID 확인 완료");
                            pwChgLL.setVisibility(View.VISIBLE);
                            yourId.setEnabled(false);
                            btnIdCheck.setEnabled(false);
                        }else{
                            dialog_idcheck.setText("등록된 ID가 없습니다.");
                        }
                    }
                });
                //패스워드 변경하기
                btnPwChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String yourId2 = yourId.getText().toString();
                        String yourPw2 = yourPw.getText().toString();
                        if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-/+=])(?=.*[a-zA-Z]).{8,16}$", yourPw2)){
                            dialog_pwcheck.setText("8~16자의 영문 대/소문자, \n숫자와 특수기호를 사용하세요.");
                        }else{
                            dialog_pwcheck.setText("");
                            //기존 pw와 동일한 지 확인
                            SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase();
                            String sql = "select * from memTBL2 where mPw ='" + yourPw2 + "'"; //하나 검색
                            Cursor cursor = sqlDB.rawQuery(sql, null); //이동하며 검색결과를 꺼내줌
                            if (cursor.moveToNext() && yourPw2.equals(cursor.getString(2))){
                                dialog_pwcheck.setText("기존 비밀번호와 동일합니다.");
                            }else{
                                String sql2 = "update memTBL2 set mPw ='" + yourPw2 + "' where mId = '" + yourId2 + "';";
                                sqlDB.execSQL(sql2);
                                int blue = ContextCompat.getColor(getApplicationContext(), R.color.blue);
                                dialog_pwcheck.setTextColor(blue);
                                dialog_pwcheck.setText("패스워드 변경 성공!");
                                Log.d("member_sqlite3DML", "데이터 수정 성공...!!!");
                            }
                        }
                    }
                });
                dialog.setPositiveButton("확인", null);
                dialog.show();
            }
        });

//======================================= 2. 회원번호 로그인(간편 로그인) 탭======================================

        Button btnNumLogin = logSnsView.findViewById(R.id.btnNumLogin); //껴진 뷰 안에서 findViewById 하는 것 주의!!
        EditText memSnsNum = logSnsView.findViewById(R.id.memSnsNum);
        btnNumLogin.setOnClickListener(new View.OnClickListener() {
            //이거는 sqlite의 회원번호로, 1. db에 2값이 있으면 => 2. alert에 Firebase의 이메일 pw를 불러오기
            @Override
            public void onClick(View v) {
                final String memSnsNum2 = memSnsNum.getText().toString(); //회원 번호
                database.child(memSnsNum2).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mUser user = snapshot.getValue(mUser.class);
                        Log.d("파이어베이스>> ", memSnsNum2 + ": userId 상세정보: " + user);
                        //1.
                        SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase(); //쓸 수 있는 stream을 가져와라.
                        //DB에 있는지 검색조회해서...Email pw를 가져와야
                        String sql = "select * from memTBL2 where mNum ='" + memSnsNum2 + "'"; //하나 검색
                        Cursor cursor = sqlDB.rawQuery(sql, null); //이동하며 검색결과를 꺼내줌
                        if (user != null && cursor.moveToNext() && cursor.getString(5).equals(user.email) && cursor.getString(2).equals(user.pw)) { //로그인 성공 시
                            Log.d("member_sqlite3DML", cursor.getString(5) + " : " + user.email); //sqlite db의 이메일 = firebase의 이메일 정보
                            Log.d("member_sqlite3DML", cursor.getString(2) + " : " + user.pw); //sqlite db의 pw = firebase의 pw 정보
//                            numAlert(email + "\n등록된 이메일로 로그인합니다.");
                            //2. 이게 alert에 네 정보 이거로 로그인 할게~할때 끼울 것
                            AlertDialog.Builder a = new AlertDialog.Builder(LoginActivity.this);
                            a.setTitle("로그인");
                            a.setIcon(R.drawable.cocorail);
                            a.setMessage(user.email + "\n등록된 정보로 로그인합니다.");
                            a.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                                    loginCheck = true;
                                    intent.putExtra("loginOk", loginCheck);
                                    intent.putExtra("name", cursor.getString(3));
                                    intent.putExtra("number", cursor.getString(0));
                                    //Toast.makeText(getApplicationContext(), "로그인 체크: " + loginCheck, Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }
                            });
                            a.show();
                        } else { //로그인 실패 시
                            numAlert2("등록된 회원 정보가 없습니다.");
                        }
                        sqlDB.close();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { //ajax에서 error 기능
                        Log.d("파이어베이스>> ", memSnsNum2 + ": userId 없음");
                    }
                });
            }
        });

        //회원가입 버튼. 회원가입 페이지로
        Button btnNumSignUp = logSnsView.findViewById(R.id.btnNumSignUp);
        btnNumSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });


//======================================= 하단 메뉴 영역 ========================================================

        Button btnReserv = findViewById(R.id.btnReserv);
        Button btnTicket = findViewById(R.id.btnTicket);
        btnReserv.setOnClickListener(new View.OnClickListener() { //첫 번째 메뉴(승차권 예매)
            @Override
            public void onClick(View view) { //MaintestActivity 대신 팀원 파트
                Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
                startActivity(intent);
            }
        });
        btnTicket.setOnClickListener(new View.OnClickListener() { //두 번째 메뉴(승차권 확인)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TicketActivity.class);
                startActivity(intent);
            }
        });

//======================================= 탭 설정 영역 ========================================================

        //이 안에서 쓰는거라 굳이 전역변수로 안해줘도 됨
        TabHost tabHost = findViewById(R.id.tabhost); //탭 하나 당 탭호스트 1개
        tabHost.setup(); //기초적인 tab 초기화! 인식시켜주는 부분

        //각 탭마다의 설정을 넣음
        TabHost.TabSpec tabSpecNum = tabHost.newTabSpec("logNum").setIndicator("일반로그인"); //index 0번
        tabSpecNum.setContent(R.id.numLoginTab); //layout을 끼워주기
        tabHost.addTab(tabSpecNum); //탭 끼워주기

        TabHost.TabSpec tabSpecSns = tabHost.newTabSpec("logSns").setIndicator("간편로그인"); //2
        tabSpecSns.setContent(R.id.snsLoginTab); //내용 끼워주기
        tabHost.addTab(tabSpecSns);

        tabHost.setCurrentTab(0);

        // EditText 밖을 클릭하면 키보드 내리기
        // 부모 레이아웃에 id를 주고, onCreate()에서 호출
        allspaceLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(); //onCreate 밖에 만든 메서드 호출
                return false;
            }
        });
    }//onCreate

//========================================== 메서드 영역 ===================================================

    private void hideKeyboard() { //키보드 내리기
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(allspaceLL.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    void numAlert2(String text) { //로그인 실패 시
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