package com.hebaiyi.www.syllabus.syllabus.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hebaiyi.www.syllabus.R;

public class TimeTableDialog extends Dialog {

    private TextView mTvName;
    private TextView mTvWhere;
    private TextView mTvTeacher;
    private TextView mTvClazz;
    private ImageView mIvClose;

    public TimeTableDialog(@NonNull Context context) {
        super(context);
        // 初始化
        init();
    }

    /**
     *  初始化
     */
    private void init(){
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.time_table_view_dalog_view,null);
        mTvName = view.findViewById(R.id.dalog_tv_name);
        mIvClose = view.findViewById(R.id.dalog_iv_close);
        mTvTeacher = view.findViewById(R.id.dalog_tv_teacher);
        mTvWhere = view.findViewById(R.id.dalog_tv_where);
        mTvClazz = view.findViewById(R.id.dalog_tv_clazz);
        setContentView(view);
        //设置dialog的大小
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth() - 100;
        getWindow().setAttributes(p);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeTableDialog.this.dismiss();
            }
        });
    }

    public void setName(String name){
        mTvName.setText(name);
    }

    public void setWhere(String where){
        mTvWhere.setText(where);
    }

    public void setTeacher(String teacher){
        mTvTeacher.setText(teacher);
    }

    public void setClazz(String clazz){
        mTvClazz.setText(clazz);
    }

}
