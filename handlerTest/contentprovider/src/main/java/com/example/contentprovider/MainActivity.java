package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnUser;
    private Button btnBook;
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
        btnUser = findViewById(R.id.get_user);
        btnBook = findViewById(R.id.get_book);
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUser = Uri.parse("content://" + "com.example.contentprovider" + "/user");
                Cursor cursor = getContentResolver().query(uriUser,new String[]{"_id","userName","sex"},null,null,null);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        Log.e(TAG, "ID " + cursor.getInt(cursor.getColumnIndex("_id")) +
                                " userName " + cursor.getString(cursor.getColumnIndex("userName")) +
                                " sex " + cursor.getString(cursor.getColumnIndex("sex")));
                    }
                    cursor.close();
                }
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriBook = Uri.parse("content://" + "com.example.contentprovider" + "/book");
                Cursor cursor = getContentResolver().query(uriBook,new String[]{"_id","bookName"},null,null,null);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        Log.e(TAG, "ID " + cursor.getInt(cursor.getColumnIndex("_id")) +
                                " bookName " + cursor.getString(cursor.getColumnIndex("bookName")));
                    }
                    cursor.close();
                }
            }
        });
    }

}