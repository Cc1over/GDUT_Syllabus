package com.hebaiyi.www.syllabus.syllabus.widget;

import java.util.List;

public class TimeTableModel {

    private int mark;
    private int startNum;
    private int endNum;
    private int dayOfWeek;
    private List<Integer> weeks;
    private String clazz;
    private String serial;
    private String name;
    private String teacher;
    private String where;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getEndNum() {
        return endNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public TimeTableModel(){

    }

    public TimeTableModel(int mark, int startNum, int endNum,
                          int dayOfWeek, List<Integer> weeks, String clazz,
                          String serial, String name, String teacher, String where) {
        this.mark = mark;
        this.startNum = startNum;
        this.endNum = endNum;
        this.dayOfWeek = dayOfWeek;
        this.weeks = weeks;
        this.clazz = clazz;
        this.serial = serial;
        this.name = name;
        this.teacher = teacher;
        this.where = where;
    }

}
