package com.hebaiyi.www.syllabus.syllabus.presenter;

import com.hebaiyi.www.syllabus.syllabus.bean.Syllabus;

import java.util.List;
import java.util.Map;

public interface SyllabusPresenter {

    // 获取课表信息成功
    int OBTAIN_SYLLABUS_SUCCESS = 0X111;
    // 获取课表信息失败
    int OBTAIN_SYLLABUS_FAIL = 0X489;

    void onRequestSyllabus(Map<String,String> cookies);

    void loginOut();


}
