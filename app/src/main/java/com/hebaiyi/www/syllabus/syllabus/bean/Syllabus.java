package com.hebaiyi.www.syllabus.syllabus.bean;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.hebaiyi.www.syllabus.util.StringUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * 课表数据实体类
 */
@Entity
public class Syllabus {

    @NotNull
    @Property(nameInDb = "name")
    @SerializedName("kcmc")
    private String name;

    @NotNull
    @Property(nameInDb = "serial")
    @SerializedName("kcbh")
    private String serial;

    @NotNull
    @Property(nameInDb = "clazz")
    @SerializedName("jxbmc")
    private String clazz;

    @NotNull
    @Property(nameInDb = "mark")
    @SerializedName("kcrwdm")
    private Long mark;

    @NotNull
    @Property(nameInDb = "period")
    @SerializedName("jcdm2")
    private String period;

    @NotNull
    @Property(nameInDb = "weeks")
    @SerializedName("zcs")
    private String weeks;

    @NotNull
    @Property(nameInDb = "day_of_week")
    @SerializedName("xq")
    private Integer dayOfWeek;

    @NotNull
    @Property(nameInDb = "where")
    @SerializedName("jxcdmcs")
    private String where;

    @NotNull
    @Property(nameInDb = "teacher")
    @SerializedName("teaxms")
    private String teacher;


    @Generated(hash = 387245687)
    public Syllabus(@NotNull String name, @NotNull String serial,
                    @NotNull String clazz, @NotNull Long mark, @NotNull String period,
                    @NotNull String weeks, @NotNull Integer dayOfWeek,
                    @NotNull String where, @NotNull String teacher) {
        this.name = name;
        this.serial = serial;
        this.clazz = clazz;
        this.mark = mark;
        this.period = period;
        this.weeks = weeks;
        this.dayOfWeek = dayOfWeek;
        this.where = where;
        this.teacher = teacher;
    }

    @Generated(hash = 1555037715)
    public Syllabus() {
    }


    /**
     * 处理唯一的mark
     */
    private long handleMark(long mark, int dayOfWeek, String period) {
        int peri = StringUtil.obtainNonZeroDigitalInString(period);
        return mark + dayOfWeek + peri;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    /**
     * 获取处理过后的时间段
     *
     * @return 时间段列表
     */
    public List<Integer> obtainPeriod() {
        return handlePeriod(getPeriod());
    }

    /**
     * 获取处理过后的周次
     *
     * @return 周次列表
     */
    public List<Integer> obtainWeeks() {
        return handleWeeks(getWeeks());
    }

    /**
     * 获取处理过后mask标记
     *
     * @return 处理后的标记
     */
    public Long obtainMask() {
        return handleMark(getMark(), getDayOfWeek(), getPeriod());
    }

    /**
     * 处理时间段数据
     *
     * @param period 时间段字符串
     * @return 时间段数组
     */
    private List<Integer> handlePeriod(String period) {
        return StringUtil.obtainNonZeroAarryInString(period);
    }

    /**
     * 处理标记位
     */
    public void treatMask() {
        setMark(handleMark(getMark(), getDayOfWeek(), getPeriod()));
    }

    /**
     * 处理周次的数据
     *
     * @param weeks 周次字符串
     * @return 表示周次的数组
     */
    private List<Integer> handleWeeks(String weeks) {
        String[] strs = weeks.split(",");
        if (strs.length == 0) {
            return null;
        }
        List<Integer> ws = new ArrayList();
        for (String str : strs) {
            if (!StringUtil.isDigital(str)) {
                continue;
            }
            ws.add(Integer.parseInt(str));
        }
        return ws;
    }


}
