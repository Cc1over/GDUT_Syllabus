package com.hebaiyi.www.syllabus.login.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hebaiyi.www.syllabus.R;
import com.hebaiyi.www.syllabus.base.BaseActivity;
import com.hebaiyi.www.syllabus.login.presenter.LoginPresenter;
import com.hebaiyi.www.syllabus.login.presenter.LoginPresenterImp;
import com.hebaiyi.www.syllabus.syllabus.view.SyllabusActivity;
import com.hebaiyi.www.syllabus.util.ToastUtil;

import java.util.Map;

public class LoginActivity extends BaseActivity implements LoginView {

    private static final int CONVERT_DELAY = 2000;

    private Toolbar mTbTop;
    private TextView mTvLogin;
    private View mLayoutInput;
    private View mLayoutProgress;
    private LinearLayout mLlStudent;
    private LinearLayout mLlPassWord;
    private EditText mEtStudent;
    private EditText mEtPassWord;

    private boolean isLogining;

    private AnimatorSet mLoginAnimationSet;
    private ObjectAnimator mProgressAnimator;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 判断是否需要登录
        if (!mLoginPresenter.needToLogin()) {
           conveyToSyllabusAty(mLoginPresenter.obtainCookies(),0);
        }
        // 绑定布局
        setContentView(R.layout.activity_login);
        // 初始化控件
        initWidget();
        // 隐藏状态栏
        hideStatus();
        // 初始化toolbar
        initToolbar();
    }

    @Override
    protected void initVariables() {
        mLoginPresenter = new LoginPresenterImp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 置空处理，防止内存泄漏
        mLoginPresenter = null;
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initEvents() {
        // 注册监听
        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登录
                login();
            }
        });
    }

    /**
     * 隐藏状态栏
     */
    private void hideStatus() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mTbTop.setTitle("");
        setSupportActionBar(mTbTop);
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        mTbTop = findViewById(R.id.login_tb_top);
        mTvLogin = findViewById(R.id.login_btn_login);
        mLayoutInput = findViewById(R.id.login_include_input);
        mLayoutProgress = findViewById(R.id.login_include_progress);
        mLlStudent = findViewById(R.id.layout_input_student_id);
        mLlPassWord = findViewById(R.id.layout_input_password);
        mEtStudent = findViewById(R.id.input_et_student);
        mEtPassWord = findViewById(R.id.input_et_password);
    }

    /**
     * login按钮点击事件
     */
    private void login() {
        // 获取输入的学号和密码
        String studentId = mEtStudent.getText().toString();
        String password = mEtPassWord.getText().toString();
        // 判空处理
        if (studentId.isEmpty() || password.isEmpty()) {
            ToastUtil.showToast(this,
                    "请输入学号或密码", Toast.LENGTH_SHORT);
            return;
        }
        // 登录操作
        if (!isLogining) {
            // 设置标记位
            isLogining = true;
            // 执行登录
            mLoginPresenter.login(studentId, password);
        }
    }

    /**
     * 登录成功
     */
    private void loginSuccess(final Map<String, String> cookies) {
        // 计算出控件宽
        float width = mTvLogin.getMeasuredWidth();
        // 隐藏输入框
        mLlStudent.setVisibility(View.INVISIBLE);
        mLlPassWord.setVisibility(View.INVISIBLE);
        mTvLogin.setVisibility(View.INVISIBLE);
        // 播放动画
        loginAnimation(mLayoutInput, width, cookies);
    }


    /**
     * 设置登录动画
     *
     * @param view    对应的view
     * @param width   控件宽度
     * @param cookies cookies
     */
    private void loginAnimation(final View view,
                                float width,
                                final Map<String, String> cookies) {
        mLoginAnimationSet = new AnimatorSet();
        // 边界变化的动画
        ValueAnimator paramsAnimator = ValueAnimator.ofFloat(0, width);
        paramsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params =
                        (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(mLayoutInput, "scaleX", 1f, 0.5f);
        mLoginAnimationSet.setDuration(1000);
        mLoginAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mLoginAnimationSet.playTogether(paramsAnimator, animator);
        mLoginAnimationSet.start();
        mLoginAnimationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后，先显示加载的动画，然后在隐藏输入框
                mLayoutProgress.setVisibility(View.VISIBLE);
                // 播放加载框的动画
                progressAnimation();
                // 隐藏输入布局
                mLayoutInput.setVisibility(View.INVISIBLE);
                // 启动SyllabusActivity
                conveyToSyllabusAty(cookies, CONVERT_DELAY);
                // 还原标记位
                isLogining = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 启动SyllabusActivity
     *
     * @param cookies 对应的cookies
     * @param delay   延时时间
     */
    private void conveyToSyllabusAty(final Map<String, String> cookies, int delay) {
        if (delay == 0) {
            SyllabusActivity.actionStart(LoginActivity.this, cookies);
            finish();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SyllabusActivity.actionStart(LoginActivity.this, cookies);
                finish();
            }
        }, delay);
    }

    /**
     * 进度条动画
     */
    private void progressAnimation() {
        PropertyValuesHolder holderX = PropertyValuesHolder
                .ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder holderY = PropertyValuesHolder
                .ofFloat("scaleY", 0.5f, 1f);
        mProgressAnimator = ObjectAnimator
                .ofPropertyValuesHolder(mLayoutProgress, holderX, holderY);
        mProgressAnimator.setDuration(1000);
        mProgressAnimator.setInterpolator(new ProgressInterpolator());
        mProgressAnimator.start();
    }

    /**
     * 登录操作的结果处理
     *
     * @param result  登录结果
     * @param cookies cookies
     */
    @Override
    public void onLoginResult(int result, Map<String, String> cookies) {
        switch (result) {
            case LoginPresenter.LOGIN_SUCCESS:
                loginSuccess(cookies);
                break;
            case LoginPresenter.SYSTEM_FAIL:
                ToastUtil.showToast(this, "服务器错误，请重试", Toast.LENGTH_SHORT);
                isLogining = false;
                break;
            case LoginPresenter.INFORMATION_FAIL:
                ToastUtil.showToast(this, "学号或密码错误，请重试", Toast.LENGTH_SHORT);
                isLogining = false;
                break;
        }
    }


    /**
     * 进度条动画插值器
     */
    private class ProgressInterpolator extends LinearInterpolator {

        private float factor;

        private ProgressInterpolator() {
            this.factor = 0.5f;
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input)
                    * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }

}
