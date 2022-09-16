package com.example.handler_thread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private HandlerThread thread;
    static Handler handler;
    private Button btnHandlerThread;
    private Button btnContentProvider;
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHandlerThread = findViewById(R.id.btn_handlerThread);
        btnContentProvider = findViewById(R.id.content_provider);
        thread = new HandlerThread("MyHandlerThread");
        thread.start();
        requestPermission();

        handler = new Handler(thread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if(message.what == 0x1){
                    try {
                        Thread.sleep(1000);
                        Log.d(TAG, "handleMessage: 1s测试");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return false;
            }
        });

        btnHandlerThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread.quit();
            }
        });

        btnContentProvider.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
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




        handler.sendEmptyMessage(0x1);

    }

    /**
     * 请求授权
     */
    private void requestPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){ //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            //调用打电话的方法
            makeTest();
        }
    }

    /**
     * 权限申请返回结果
     * @param requestCode 请求码
     * @param permissions 权限数组
     * @param grantResults  申请结果数组，里面都是int类型的数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ //同意权限申请
                    makeTest();
                }else { //拒绝权限申请
                    Toast.makeText(this,"权限被拒绝了",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void makeTest(){
        Log.i(TAG, "makeTest: 成功");
    }
}