package com.hebaiyi.www.syllabus.welcome;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hebaiyi.www.syllabus.R;
import com.hebaiyi.www.syllabus.login.view.LoginActivity;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 隐藏状态栏
        hideStatus();
        // 跳转到住activity
        convert();
    }

    /**
     *  跳转到其他Activity
     */
    private void convert(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.actionStart(WelcomeActivity.this);
                finish();
            }
        }, 3000);
    }

    /**
     * 隐藏状态栏
     */
    private void hideStatus() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}
