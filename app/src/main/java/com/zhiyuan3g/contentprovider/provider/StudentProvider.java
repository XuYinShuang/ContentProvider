package com.zhiyuan3g.contentprovider.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zhiyuan3g.contentprovider.helper.DBOpenHelper;

import java.util.IllegalFormatException;

/**
 * Created by liu_8769 on 2016/7/1.
 */
public class StudentProvider extends ContentProvider {

    private DBOpenHelper helper;
    private SQLiteDatabase db;

    //常量UriMatcher.NO_MATCH：不匹配Uri时，给本应用程序返回-1
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    //匹配的返回码
    private static final int STUDENTS = 1;
    private static final int STUDENT = 2;

    //静态语句，注册两个Uri，以提供外部程序通过Uri进行匹配
    static {
        matcher.addURI("com.zhiyuan3g.contentprovider", "student", STUDENTS);
        //#表示的是：通配符，用于具体到某一位置
        matcher.addURI("com.zhiyuan3g.contentprovider", "student/#", STUDENT);
    }

    @Override
    public boolean onCreate() {
        helper = new DBOpenHelper(getContext(), "MySQL", null, 1);
        db = helper.getReadableDatabase();
        return true;
    }

    /**
     * @param uri           资源位置
     * @param projection    数据库中的字段
     * @param selection     查询条件
     * @param selectionArgs 填充的数据
     * @param sortOrder     排列顺序
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (matcher.match(uri)) {
            case STUDENTS:
                cursor = db.query("Student", projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case STUDENT:
                long id = ContentUris.parseId(uri);

                String whereClause = "_id = " + id;

                if (selection != null) {
                    whereClause += " and " + selection;
                }
                cursor = db.query("Student", projection, whereClause,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("该URI" + uri + "无法解析！");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String type = "";
        switch (matcher.match(uri)) {
            //操作的是集合类型
            case STUDENTS:
                type = "vnd.android.cursor.dir/student";
                break;
            //操作的是非集合类型
            case STUDENT:
                type = "vnd.android.cursor.item/student";
                break;
            default:
                throw new IllegalArgumentException("该URI" + uri + "无法解析！");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return type;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri insertUri;
        switch (matcher.match(uri)) {
            case STUDENTS:
                //表中插入数据
                Long id = db.insert("Student", "Name", values);
                //通过ContentUris指定到具体的Uri
                insertUri = ContentUris.withAppendedId(uri, id);
                //通知改变
                getContext().getContentResolver().notifyChange(insertUri, null);
                break;
            default:
                throw new IllegalArgumentException("该URI" + uri + "无法解析！");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowNum = -1;
        switch (matcher.match(uri)) {
            case STUDENTS:
                rowNum = db.delete("Student", selection, selectionArgs);
                break;
            case STUDENT:
                Long id = ContentUris.parseId(uri);
                String whereClause = "_id = " + id;
                if (selection != null) {
                    whereClause += " and " + selection;
                }
                rowNum = db.delete("Student", whereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("该URI" + uri + "无法解析！");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowNum;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowNum;

        switch (matcher.match(uri)) {
            case STUDENTS:
                rowNum = db.update("Student", values, selection, selectionArgs);
                break;
            case STUDENT:
                Long id = ContentUris.parseId(uri);
                String whereClause = "_id = " + id;
                if (selection != null) {
                    whereClause += " and " + selection;
                }
                rowNum = db.update("Student", values, whereClause,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("该URI" + uri + "无法解析！");

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowNum;
    }
}
