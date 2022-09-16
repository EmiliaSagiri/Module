package com.example.handlertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private TextView tHandler;
    private Thread threadWeak;
    private Thread threadName;
    private Thread threadPost;
    private Button btnWeak;
    private Button btnName;
    private Button btnPost;
    private Handler mHandlerPost = new Handler();
    private boolean flagWeak = false;
    private boolean flagName = false;
    private boolean flagPost = false;
    private MyHandler handlerWeak;

//使用匿名handler子类
   final Handler handlerName = new Handler(){
       @SuppressLint("HandlerLeak")
       @Override
       public void handleMessage(@NonNull Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case 1:
                  tHandler.setText("匿名handler子类调用成功");
           }
       }
   };

//使用静态弱引用handler子类
   private static class MyHandler extends Handler{
       private WeakReference<MainActivity> weakReference;
       private MyHandler(MainActivity mainActivity){
           this.weakReference = new WeakReference(mainActivity);
       }

       @Override
       public void handleMessage(@NonNull Message msg) {
           MainActivity activity =weakReference.get();
           super.handleMessage(msg);
           switch (msg.what){
               case 1:
                        if(null != activity){
                            Toast.makeText(activity, "123", Toast.LENGTH_SHORT).show();
                            activity.tHandler.setText("123");
                        }

           }
       }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPost();
        initName();

    }

    private void initView(){
        tHandler = findViewById(R.id.handler);
        btnWeak = findViewById(R.id.handler_weak);
        btnName = findViewById(R.id.handler_name);
        btnPost = findViewById(R.id.handler_post);

        handlerWeak = new MyHandler(this);

        btnWeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(threadWeak != null){
                    return;
                }
                threadWeak = new Thread(new Runnable() {
                    @Override
                    public void run() {

                            Message message = handlerWeak.obtainMessage();
                            message.what = 1;
                            handlerWeak.sendMessage(message);

                    }
                });
                threadWeak.start();
            }
        });
    }

    private void initPost(){
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(threadPost != null){
                    return;
                }

                threadPost = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandlerPost.post(new Runnable() {
                            @Override
                            public void run() {

                                    tHandler.setText("handler.post方法调用成功");

                            }
                        });
                    }
                });

                threadPost.start();
            }
        });
    }

    private void  initName(){
       btnName.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(threadName != null){
                   return;
               }
               threadName = new Thread(new Runnable() {
                   @Override
                   public void run() {


                           handlerName.sendEmptyMessageDelayed(0x1,1000);


                   }
               });
               threadName.start();
           }
       });
    }

public void stopThread(boolean flag){
       flag = true ;
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerPost.removeMessages(0);
        handlerName.removeCallbacksAndMessages(null);
        handlerWeak.removeMessages(1);

    }
}