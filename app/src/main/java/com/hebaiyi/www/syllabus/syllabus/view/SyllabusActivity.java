package com.hebaiyi.www.syllabus.syllabus.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hebaiyi.www.syllabus.R;
import com.hebaiyi.www.syllabus.base.BaseActivity;
import com.hebaiyi.www.syllabus.login.view.LoginActivity;
import com.hebaiyi.www.syllabus.syllabus.presenter.SyllabusPresenter;
import com.hebaiyi.www.syllabus.syllabus.presenter.SyllabusPresenterImp;
import com.hebaiyi.www.syllabus.syllabus.widget.TimeTableModel;
import com.hebaiyi.www.syllabus.syllabus.widget.TimeTableView;
import com.hebaiyi.www.syllabus.util.StringUtil;
import com.hebaiyi.www.syllabus.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyllabusActivity extends BaseActivity implements SyllabusView {

    private TimeTableView mTtvSyllabus;
    private TextView mTvWeek;
    private Toolbar mTbTop;
    private ImageButton mIbLoginOut;
    private ImageButton mIbSelect;

    private List<TimeTableModel> mModels;
    private SyllabusPresenter mPresenter;
    private int maxWeeks;

    public static void actionStart(Context context, Map<String, String> cookies) {
        Intent intent = new Intent(context, SyllabusActivity.class);
        intent.putExtra("cookies", (HashMap<String, String>) cookies);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_syllabus);
        mTtvSyllabus = findViewById(R.id.syllabus_time_table_view_syllabus);
        mTbTop = findViewById(R.id.syllabus_tb_top);
        mTvWeek = findViewById(R.id.syllabus_tv_week);
        mIbLoginOut = findViewById(R.id.syllabus_ib_login_out);
        mIbSelect = findViewById(R.id.syllabus_ib_select_week);
        // 隐藏状态栏
        hideStatus();
        // 初始化toolbar
        initToolbar();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        mTbTop.setTitle("");
        setSupportActionBar(mTbTop);
    }


    @Override
    protected void initVariables() {
        mPresenter = new SyllabusPresenterImp(this);
    }

    @Override
    protected void initEvents() {
        // 登出按钮
        mIbLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOut();
            }
        });
        // 选择星期
        mIbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWeek();
            }
        });
    }

    /**
     * 选择星期
     */
    private void selectWeek() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (maxWeeks == 0) {
            maxWeeks = obtainMaxWeeks();
        }
        final String[] items = new String[maxWeeks];
        for (int i = 0; i < items.length; i++) {
            items[i] = StringUtil.joint("第", i + 1 + "", "周");
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTtvSyllabus.setCurrWeek(which + 1);
                if ((which + 1) < 10) {
                    mTvWeek.setText(StringUtil.joint("0", which + 1 + ""));
                } else {
                    mTvWeek.setText(which + 1 + "");
                }
            }
        });
        builder.setCancelable(true);
        builder.show();
    }


    private int obtainMaxWeeks() {
        int max = 0;
        for (TimeTableModel mo : mModels) {
            List<Integer> weeks = mo.getWeeks();
            for (int o : weeks) {
                if (o > max) {
                    max = o;
                }
            }
        }
        return max;
    }

    /**
     * 隐藏状态栏
     */
    private void hideStatus() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        LoginActivity.actionStart(this);
        mPresenter.loginOut();
        finish();
    }


    @Override
    protected void loadData() {
        Map<String, String> cookies = (Map<String, String>) getIntent()
                .getSerializableExtra("cookies");
        // 请求数据
        mPresenter.onRequestSyllabus(cookies);
    }

    private void obtainSyllabusSuccess(List<TimeTableModel> models) {
        mModels = models;
        mTtvSyllabus.setTimeTable(models);
    }

    @Override
    public void onRequestResult(int result, List<TimeTableModel> models) {
        switch (result) {
            case SyllabusPresenter.OBTAIN_SYLLABUS_SUCCESS:
                // 数据获取成功
                obtainSyllabusSuccess(models);
                break;
            case SyllabusPresenter.OBTAIN_SYLLABUS_FAIL:
                ToastUtil.showToast(this,
                        "获取课程表信息失败，请重试", Toast.LENGTH_SHORT);
                break;
        }
    }
}
