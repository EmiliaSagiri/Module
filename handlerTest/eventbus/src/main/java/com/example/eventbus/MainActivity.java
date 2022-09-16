package com.example.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private Button btnNext;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
    }

    public void initView(){
        btnNext = findViewById(R.id.next_activity);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new MessageEvent("hello"));
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
    }
/*
订阅者方法将在主线程（UI线程）中被调用。因此，可以在该模式的订阅者方法中直接更新UI界面。如果发布事件的线程是主线程，那么该模式的订阅者方法将被直接调用。使用该模式的订阅者方法必须快速返回，以避免阻塞主线程。
 */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onMessageEvent(MessageEvent event){
        Log.i(TAG, "onMessageEvent: " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void onMessageEventPosting(MessageEvent event) {
        Log.d(TAG, "onMessageEventPosting(), current thread is " + Thread.currentThread().getName());
        EventBus.getDefault().cancelEventDelivery(event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED, priority = 2)
    public void onMessageEventMainOrdered(MessageEvent event) {
        Log.d(TAG, "onMessageEventMainOrdered(), current thread is " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 3)
    public void onMessageEventBackground(MessageEvent event) {
        Log.d(TAG, "onMessageEventBackground(), current thread is " + Thread.currentThread().getName());

    }

    @Subscribe(threadMode = ThreadMode.ASYNC, priority = 4)
    public void onMessageEventAsync(MessageEvent event) {
        Log.d(TAG, "onMessageEventAsync(), current thread is " + Thread.currentThread().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}