package com.hebaiyi.www.syllabus.login.presenter;

import java.util.Map;

public interface LoginPresenter {

    // 登录成功
    int LOGIN_SUCCESS = 0XF005;
    // 登录时信息错误
    int INFORMATION_FAIL = 0X894;
    // 登录时系统错误
    int SYSTEM_FAIL = 0X222;

    void login(String username, String password);

    boolean needToLogin();

    Map<String,String> obtainCookies();

}
