package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.net.URI;

public class MyFirstContentProvider extends ContentProvider {
    private Context context;
    private static UriMatcher uriMatcher ;
    private SQLiteDatabase db;
    private final static String AUTHORITY = "com.example.contentprovider";
    public static final int CODE_BOOK = 0;
    public static final int CODE_USER = 1;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,DbOpenHelper.BOOK_TABLE_NAME,CODE_BOOK);
        uriMatcher.addURI(AUTHORITY,DbOpenHelper.USER_TABLE_NAME,CODE_USER);
    }
    public MyFirstContentProvider() {
    }

    private String getTableName(Uri uri){
        String tableName = null;
        switch (uriMatcher.match(uri)){
            case CODE_BOOK:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case CODE_USER:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
        }
        return tableName;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("不支持的URI" +uri);
        }
        int count = db.delete(tableName,selection,selectionArgs);
        if(count > 0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("不支持的URI" + uri);
        }
        db.insert(tableName,null,values);
        context.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        initView();
        return false;
    }

    private void initView(){
        db = new DbOpenHelper(context).getWritableDatabase();
        db.beginTransaction();
        //ContentValues储存基本类型的数据，往数据库插入数据时需要一个ContentVakue对象
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookName", "java从入门到放弃");
        db.insert(DbOpenHelper.BOOK_TABLE_NAME,null,contentValues);
        contentValues.put("bookName", "第一行代码");
        db.insert(DbOpenHelper.BOOK_TABLE_NAME,null,contentValues);
        contentValues.clear();

        contentValues.put("userName","小明");
        contentValues.put("sex","男");
        db.insert(DbOpenHelper.USER_TABLE_NAME,null,contentValues);
        contentValues.put("userName","小红");
        contentValues.put("sex","女");
        db.insert(DbOpenHelper.USER_TABLE_NAME,null,contentValues);
        contentValues.clear();
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("不支持的URI" + uri);
        }
        return db.query(tableName,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("不支持的URI" + uri);
        }
        int count = db.update(tableName,values,selection,selectionArgs);
        if(count > 0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return count;
    }
}