package com.example.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SecondActivity extends AppCompatActivity {
    private Button btnPost;
    public static final String TAG = SecondActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView(){
        btnPost = findViewById(R.id.post_event);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent("EventBus 调用成功"));
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(MessageEvent event){
        Log.d(TAG, "onMessageEvent: " + event.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
