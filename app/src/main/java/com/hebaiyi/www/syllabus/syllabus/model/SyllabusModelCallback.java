package com.hebaiyi.www.syllabus.syllabus.model;

import com.hebaiyi.www.syllabus.syllabus.bean.Syllabus;

import java.util.List;

public interface SyllabusModelCallback {

    void onSuccess(List<Syllabus> syllabuses);

    void onFail();

}
