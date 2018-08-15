package com.hebaiyi.www.syllabus.login.presenter;

import android.os.Handler;
import android.os.Message;

import com.hebaiyi.www.syllabus.login.bean.URL;
import com.hebaiyi.www.syllabus.login.model.LoginModel;
import com.hebaiyi.www.syllabus.login.view.LoginView;
import com.hebaiyi.www.syllabus.util.JsoupUtil;

import java.lang.ref.WeakReference;
import java.util.Map;

public class LoginPresenterImp implements LoginPresenter {

    private LoginView mLoginView;
    private LoginHandler mHandler;

    private LoginModel mLoginModel;

    public LoginPresenterImp(LoginView loginView) {
        // 获取登录view
        mLoginView = loginView;
        // 初始化handler
        mHandler = new LoginHandler(this);
        // 初始化登录model
        mLoginModel = new LoginModel();
    }


    @Override
    public void login(String username, String password) {
        // 模拟登录
        JsoupUtil.enter(URL.GDUT_LOGIN_URL, username, password,
                new JsoupUtil.EnterCallback() {

                    @Override
                    public void onInformationFail() {
                        mHandler.sendEmptyMessage(INFORMATION_FAIL);
                    }

                    @Override
                    public void onSystemFail() {
                        mHandler.sendEmptyMessage(SYSTEM_FAIL);
                    }

                    @Override
                    public void onSuccess(Map<String, String> map) {
                        loginSuccess(map);
                    }
                });
    }

    @Override
    public boolean needToLogin() {
        // 获取cookies
        Map<String, String> cookies = mLoginModel.obtainCookies();
        if (cookies == null) {
            return true;
        }
        return cookies.toString().isEmpty() || "{}".equals(cookies.toString());
    }

    /**
     * 获取cookies
     *
     * @return 对应的cookies
     */
    @Override
    public Map<String, String> obtainCookies() {
        return mLoginModel.obtainCookies();
    }

    /**
     * 登录成功
     *
     * @param cookies 对应的cookies
     */
    private void loginSuccess(Map<String, String> cookies) {
        // 获取小心并发送消息
        Message message = Message.obtain();
        message.obj = cookies;
        message.what = LoginPresenterImp.LOGIN_SUCCESS;
        mHandler.sendMessage(message);
        // 缓存cookies
        mLoginModel.cacheCookies(cookies);
    }

    private static class LoginHandler extends Handler {

        private final WeakReference<LoginPresenterImp> mPresenter;

        private LoginHandler(LoginPresenterImp presenterImp) {
            mPresenter = new WeakReference<>(presenterImp);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginPresenterImp presenter = mPresenter.get();
            if (presenter == null) {
                return;
            }
            LoginView loginView = presenter.mLoginView;
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    // 登录成功
                    loginView.onLoginResult(LOGIN_SUCCESS, (Map<String, String>) msg.obj);
                    break;
                case SYSTEM_FAIL:
                    // 系统异常
                    loginView.onLoginResult(SYSTEM_FAIL, null);
                    break;
                case INFORMATION_FAIL:
                    // 个人信息异常
                    loginView.onLoginResult(INFORMATION_FAIL, null);
                    break;

            }
        }
    }

}
