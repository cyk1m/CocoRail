package com.pro3.coco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable; //안드로이드 문법

public class MemTBL_DBHelper extends SQLiteOpenHelper { //DDL만 담당
    //추상클래스는 상속받은곳에서 강제적으로 꼭 해야할 것을 지정해줌. 뭘 해줘야할지 헷갈릴 일이 없다!

    public MemTBL_DBHelper(@Nullable Context context) {
        super(context, "CocoDB", null, 1); //2번째 것이 db이름. 3번째는 null, 4번째는 상관x
        Log.d("member_sqlite3DDL", "데이터베이스 생성 호출함...!!!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS memTBL2 (mNum INTEGER primary key autoincrement, mId varchar(20), mPw varchar(20), mName varchar(50), mBirth varchar(10), mEmail varchar(50), mTel char(11));");
        Log.d("member_sqlite3DDL", "CREATE TABLE 호출함...!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS memTBL2");
        Log.d("member_sqlite3DDL", "DROP TABLE 호출함...!!!");
        onCreate(db); //있으면 지우고 create하라고.
    }
}
