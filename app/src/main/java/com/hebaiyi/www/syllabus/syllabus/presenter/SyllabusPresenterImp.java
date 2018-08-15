package com.hebaiyi.www.syllabus.syllabus.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.hebaiyi.www.syllabus.app.SyllabusApplication;
import com.hebaiyi.www.syllabus.syllabus.bean.Syllabus;
import com.hebaiyi.www.syllabus.syllabus.model.SyllabusModel;
import com.hebaiyi.www.syllabus.syllabus.model.SyllabusModelCallback;
import com.hebaiyi.www.syllabus.syllabus.view.SyllabusView;
import com.hebaiyi.www.syllabus.syllabus.widget.TimeTableModel;
import com.hebaiyi.www.syllabus.util.ContainerUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hebaiyi.www.syllabus.app.SyllabusApplication.PREF_KEY_NAME;
import static com.hebaiyi.www.syllabus.app.SyllabusApplication.SHARED_PREF_NAME;


public class SyllabusPresenterImp implements SyllabusPresenter {

    private SyllabusView mSyllabusView;
    private SyllabusHandler mHandler;
    private SyllabusModel mModel;

    public SyllabusPresenterImp(SyllabusView syllabusView) {
        // 获取课程表视图
        mSyllabusView = syllabusView;
        // 初始化handler
        mHandler = new SyllabusHandler(this);
        // 初始化课程表model类
        mModel = new SyllabusModel();
    }

    @Override
    public void onRequestSyllabus(Map<String, String> cookies) {
        mModel.obtainSyllabus(cookies, new SyllabusModelCallback() {
            @Override
            public void onSuccess(List<Syllabus> syllabuses) {
                requestSuccess(syllabuses);
            }

            @Override
            public void onFail() {
                // 获取数据失败
                mHandler.sendEmptyMessage(OBTAIN_SYLLABUS_FAIL);
            }
        });
    }

    /**
     * 请求成功
     */
    private void requestSuccess(List<Syllabus> syllabuses) {
        List<TimeTableModel> models = new ArrayList<>();
        for (Syllabus syllabus : syllabuses) {
            TimeTableModel model = new TimeTableModel();
            buildModel(model, syllabus);
            models.add(model);
        }
        // 获取数据成功后的处理
        Message message = Message.obtain();
        message.what = OBTAIN_SYLLABUS_SUCCESS;
        message.obj = models;
        mHandler.sendMessage(message);
    }

    private void buildModel(TimeTableModel model, Syllabus syllabus) {
        model.setClazz(syllabus.getClazz());
        model.setDayOfWeek(syllabus.getDayOfWeek());
        model.setWeeks(syllabus.obtainWeeks());
        List<Integer> period = syllabus.obtainPeriod();
        if (!ContainerUtil.isEmpty(period)) {
            model.setEndNum(period.get(period.size() - 1));
            model.setStartNum(period.get(0));
        }
        model.setWhere(syllabus.getWhere());
        model.setName(syllabus.getName());
        model.setSerial(syllabus.getSerial());
        model.setTeacher(syllabus.getTeacher());
    }

    @Override
    public void loginOut() {
        // 获取上下文
        Context context = SyllabusApplication.getContext();
        // 获取sharePreference对象
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = context
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit();
        // 把cookies置空
        editor.putString(PREF_KEY_NAME, "");
        // 提交
        editor.apply();
        // 清空缓存的课表信息
        mModel.deleteAll();
    }



    private static class SyllabusHandler extends Handler {

        private final WeakReference<SyllabusPresenterImp> mPresenter;

        private SyllabusHandler(SyllabusPresenterImp presenter) {
            mPresenter = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            SyllabusPresenterImp spi = mPresenter.get();
            if (spi == null) {
                return;
            }
            SyllabusView syllabusView = spi.mSyllabusView;
            switch (msg.what) {
                case OBTAIN_SYLLABUS_SUCCESS:
                    // 获取课表信息成功
                    syllabusView.onRequestResult(OBTAIN_SYLLABUS_SUCCESS, (List<TimeTableModel>) msg.obj);
                    break;
                case OBTAIN_SYLLABUS_FAIL:
                    // 获取课表信息失败
                    syllabusView.onRequestResult(OBTAIN_SYLLABUS_FAIL, null);
                    break;
            }
        }
    }

}
