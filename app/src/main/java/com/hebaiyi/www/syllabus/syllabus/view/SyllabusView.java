package com.hebaiyi.www.syllabus.syllabus.view;

import com.hebaiyi.www.syllabus.syllabus.widget.TimeTableModel;

import java.util.List;

public interface SyllabusView {

    void onRequestResult(int result, List<TimeTableModel> models);

}
