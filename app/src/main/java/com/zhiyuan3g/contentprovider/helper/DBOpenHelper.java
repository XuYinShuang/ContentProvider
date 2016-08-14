package com.zhiyuan3g.contentprovider.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liu_8769 on 2016/7/1.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private final String CREATE_STUDENT_SQL ="Create table Student (_id integer primary key autoincrement,name varchar(10),age integer)";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
