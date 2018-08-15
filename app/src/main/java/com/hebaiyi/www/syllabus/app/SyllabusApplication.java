package com.hebaiyi.www.syllabus.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class SyllabusApplication extends Application {

   // sharedPreferences的缓存名字
    public static final String SHARED_PREF_NAME = "SYLLABUS_COOKIES";
    // sharedPreferences数据的键名
    public static final String PREF_KEY_NAME = "COOKIES";

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext(){
        return sContext;
    }

}
