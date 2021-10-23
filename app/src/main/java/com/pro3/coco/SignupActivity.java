package com.pro3.coco;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    Toolbar main_toolbar;
    MemTBL_DBHelper memTBL_dbHelper;
    LinearLayout signupLL;
    Button btnSignUpFin, btnSignUpInit;
    ImageView pwCheckImg;
    TextView idCheckTxt, pwCheckTxt1, pwCheckTxt2, nameCheckTxt, birthCheckTxt, telCheckTxt, emailCheckTxt;
    EditText signupId, signupPw, signupPw2, signupName, signupBirth, signupTel, signupEmail;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("                     회원가입");
        signupLL = findViewById(R.id.signupLL);
        btnSignUpFin = findViewById(R.id.btnSignUpFin);
        btnSignUpInit = findViewById(R.id.btnSignUpInit);
        pwCheckImg = findViewById(R.id.pwCheckImg);
        idCheckTxt = findViewById(R.id.idCheckTxt);
        pwCheckTxt1 = findViewById(R.id.pwCheckTxt1);
        pwCheckTxt2 = findViewById(R.id.pwCheckTxt2);
        nameCheckTxt = findViewById(R.id.nameCheckTxt);
        birthCheckTxt = findViewById(R.id.birthCheckTxt);
        telCheckTxt = findViewById(R.id.telCheckTxt);
        emailCheckTxt = findViewById(R.id.emailCheckTxt);
        signupId = findViewById(R.id.signupId);
        signupPw = findViewById(R.id.signupPw);
        signupPw2 = findViewById(R.id.signupPw2);
        signupName = findViewById(R.id.signupName);
        signupBirth = findViewById(R.id.signupBirth);
        signupTel = findViewById(R.id.signupTel);
        signupEmail = findViewById(R.id.signupEmail);

        memTBL_dbHelper = new MemTBL_DBHelper(this); //액티비티를 넘겨주겠다.

        // 아이디 중복 체크
        signupId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (signupId.getText().toString().equals("") || signupId.getText().toString() == null) {
                    idCheckTxt.setText("필수 정보입니다.");
                } else {
                    idCheckTxt.setText("");
                    //5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능
                    if (!Pattern.matches("^(?=.*\\d)(?=.*[a-z]).{5,20}$", signupId.getText().toString())){
                        idCheckTxt.setText("5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
                    }else{
                        idCheckTxt.setText("");
                    }
                }
            }
        });

        // 패스워드 일치여부 확인하기
        signupPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (signupPw.getText().toString().equals(signupPw2.getText().toString())) {
                    pwCheckImg.setImageResource(R.drawable.pw_check_ok);
                } else {
                    pwCheckImg.setImageResource(R.drawable.pw_check_no);
                    pwCheckTxt2.setText(".");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (signupPw.getText().toString().equals("") || signupPw.getText().toString() == null) {
                    pwCheckTxt1.setText("필수 정보입니다.");
                } else {
                    pwCheckTxt1.setText("");
                    //5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능
                    if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-/+=])(?=.*[a-zA-Z]).{8,16}$", signupPw.getText().toString())){
                        pwCheckTxt1.setText("8~16자의 영문 대/소문자, 숫자와 특수기호를 사용하세요.");
                    }else{
                        pwCheckTxt1.setText("");
                    }
                }
            }
        }); // signupPw1 end
        signupPw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (signupPw2.getText().toString().equals(signupPw.getText().toString())) {
                    pwCheckImg.setImageResource(R.drawable.pw_check_ok);
                } else {
                    pwCheckImg.setImageResource(R.drawable.pw_check_no);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (signupPw2.getText().toString().equals("") || signupPw2.getText().toString() == null) {
                    pwCheckTxt2.setText("필수 정보입니다.");
                } else {
                    pwCheckTxt2.setText("");
                }
            }
        }); // signupPw2 end

        // 이름
        signupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (signupName.getText().toString().equals("") || signupName.getText().toString() == null) {
                    nameCheckTxt.setText("필수 정보입니다.");
                } else {
                    nameCheckTxt.setText("");
                }
            }
        });

        // 생년월일
        signupBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (signupBirth.getText().toString().equals("") || signupBirth.getText().toString() == null) {
                    birthCheckTxt.setText("필수 정보입니다.");
                } else {
                    birthCheckTxt.setText("");
                    //5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능
                    if (!Pattern.matches("^(?=.*\\d).{8}$", signupBirth.getText().toString())){
                        birthCheckTxt.setText("생년월일을 바르게 입력해주세요.");
                    }else{
                        birthCheckTxt.setText("");
                    }
                }
            }
        });

        // 휴대폰번호
        signupTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (signupTel.getText().toString().equals("") || signupTel.getText().toString() == null) {
                    telCheckTxt.setText("필수 정보입니다.");
                } else {
                    telCheckTxt.setText("");
                    //5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능
                    if (!Pattern.matches("^(?=.*\\d).{10,11}$", signupTel.getText().toString())){
                        telCheckTxt.setText("휴대폰 번호를 바르게 입력해주세요.");
                    }else{
                        telCheckTxt.setText("");
                    }
                }
            }
        });

        // 이메일
        signupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (signupEmail.getText().toString().equals("") || signupEmail.getText().toString() == null) {
                    emailCheckTxt.setText("필수 정보입니다.");
                } else {
                    emailCheckTxt.setText("");
                    //5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능
//                    "^(?=.*\d)(?=.*[a-z]).{5,20}$"
//                    "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-/+=])(?=.*[a-zA-Z]).{8,16}$"
//                    "^[a-zA-X0-9]@[a-zA-Z0-9].[a-zA-Z0-9]$"
                    if (!Pattern.matches("^[a-zA-Z0-9.-_]{5,20}+@[a-zA-Z0-9.-]{2,8}+\\.[a-zA-Z]{2,6}$", signupEmail.getText().toString())){
                        emailCheckTxt.setText("이메일 형식을 바르게 입력해주세요.");
                    }else{
                        emailCheckTxt.setText("");
                    }
                }
            }
        });


        //====================================================버튼 영역===============================================================


        btnSignUpInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB 연결
                SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase(); //쓸 수 있는 stream을 가져와라.
                memTBL_dbHelper.onUpgrade(sqlDB, 1, 2); //2,3번째는 상관x //onUpgrade안에 sql이 들어있다.
                Log.d("member_sqlite3DML", "DDL 호출함...!!!");
                sqlDB.close(); //stream 닫아주기
            }
        });

        // 회원가입 완료 버튼(insert)
        btnSignUpFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mId = signupId.getText().toString();
                String mPw = signupPw.getText().toString();
                String mName = signupName.getText().toString();
                String mBirth = signupBirth.getText().toString();
                String mEmail = signupEmail.getText().toString();
                String mTel = signupTel.getText().toString();
                if ((mId.equals("") || mId == null) || (mPw.equals("") || mPw == null) || (mName.equals("") || mName == null)
                        || (mBirth.equals("") || mBirth == null) || (mEmail.equals("") || mEmail == null) || (mTel.equals("") || mTel == null)
                        && (!idCheckTxt.equals("") || !pwCheckTxt1.equals("") || !pwCheckTxt2.equals("") || !telCheckTxt.equals("") || !nameCheckTxt.equals("")
                        || !birthCheckTxt.equals("") || !emailCheckTxt.equals(""))){
                    signupAlert("필수 항목을 바르게 입력했는지 확인해주세요."); //유효성 검사
                }else {
                    SQLiteDatabase sqlDB = memTBL_dbHelper.getWritableDatabase(); //stream을 얻어옴
                    String sql = "insert into memTBL2 values (" + null + ", '" + mId + "', '" + mPw + "', '" + mName + "', '" + mBirth + "', '" + mEmail + "', '" + mTel + "');";
//                    String sql = "insert into memTBL2 values ('" + mId + "', '" + mPw + "', '" + mName + "', '" + mBirth + "', '" + mEmail + "', '" + mTel + "');";
                    sqlDB.execSQL(sql);
                    Log.d("member_sqlite3DML", "데이터 삽입 성공...!!!");
                    sqlDB.close();
                    Log.d("member_sqlite3DML", "데이터 closed...!!!");
                    signupId.setText("");
                    signupPw.setText("");
                    signupPw2.setText("");
                    signupName.setText("");
                    signupBirth.setText("");
                    signupTel.setText("");
                    signupEmail.setText("");
                    idCheckTxt.setText("");
                    pwCheckTxt1.setText("");
                    pwCheckTxt2.setText("");
                    nameCheckTxt.setText("");
                    birthCheckTxt.setText("");
                    telCheckTxt.setText("");
                    emailCheckTxt.setText("");
                    pwCheckImg.setImageResource(R.drawable.pw_check_clear);
                    String text = "회원가입이 완료되었습니다.";
                    signupAlert(text); //alert에서 확인을 누르면 intent를 하게 바꾸기!!!!
//                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), MaintestActivity.class);
//                    startActivity(intent);

                }
            }
        });


        // EditText 밖을 클릭하면 키보드 내리기
        // 부모 레이아웃에 id를 주고, onCreate()에서 호출
        signupLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(); //onCreate 밖에 만든 메서드 호출
                return false;
            }
        });

    }//onCreate

    private void hideKeyboard() {
        // private 는 외부에서 클래스 변수에 직접 접근할 수 없고 클래스의 메소드를 통해서만 접근이 가능
        // 키보드를 제어하려면 InputMethodManager 객체를 사용 (android.view.inputmethod에 있다.)
        // 이 객체는 getSystemService(키보드 추적 객체)로 구할 수 있고, 변수는 Context.INPUT_METHOD_SERVICE이다.
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //InputMethodManager 클래스에서 제공하는 hideSoftInputFromWindow() 함수를 이용해 키보드를 숨김
//        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // getWindowToken()이 Null 값을 리턴하여 에러.  getCurrentFocus()에서 Null을 리턴했기에, windowToken 값을 얻을 수 없었음
        // 해결방법은 현재 액티비티에서 레이아웃 파일에 그려져있는 뷰 들중(당연히 findViewById로 연결된) 하나를 선택해서 사용하면 된다.
        // 예) signupLL.getWindowToken()
        inputManager.hideSoftInputFromWindow(signupLL.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //유효성 검사 alert 띄우기
    void signupAlert(String text){
        AlertDialog.Builder a = new AlertDialog.Builder(SignupActivity.this);
        a.setTitle("회원가입");
        a.setIcon(R.drawable.cocorail);
        a.setMessage(text);
        a.setPositiveButton("확인", null);
        a.show();
    }

}//class
