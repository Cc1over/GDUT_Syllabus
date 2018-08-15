package com.hebaiyi.www.syllabus.login.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hebaiyi.www.syllabus.app.SyllabusApplication;

import java.util.HashMap;
import java.util.Map;

import static com.hebaiyi.www.syllabus.app.SyllabusApplication.PREF_KEY_NAME;
import static com.hebaiyi.www.syllabus.app.SyllabusApplication.SHARED_PREF_NAME;

public class LoginModel {

    /**
     * 将cookies缓存到本地
     *
     * @param cookies 对应的cookies
     */
    public void cacheCookies(Map<String, String> cookies) {
        // 获取全局上下文
        Context context = SyllabusApplication.getContext();
        // 获取SharedPreferences对象
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit();
        // 保存字符串
        editor.putString(PREF_KEY_NAME, cookies.toString());
        // 提交
        editor.apply();
    }

    /**
     * 获取缓存在本地的cookies
     *
     * @return cookies
     */
    public Map<String, String> obtainCookies() {
        // 获取上下文
        Context context = SyllabusApplication.getContext();
        // 获取SharedPreferences对象
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // 获取保存的字符串
        String str = pref.getString(PREF_KEY_NAME, "");
        // 判空处理
        if("".equals(str)){
            return null;
        }
        // 去掉大括号
        String string = str.substring(1,str.length()-1);
        Log.e("string",string);
        // 切分
        String[] stirs = string.split(",",3);
        // 组建Map
        Map<String,String> cookies = new HashMap<>();
        for (String stir : stirs) {
            Log.e("stir",stir);
            String[] stars = stir.split("=");
            cookies.put(stars[0],stars[1]);
        }
        return cookies;
    }


}
