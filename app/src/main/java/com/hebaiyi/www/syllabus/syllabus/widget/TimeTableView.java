package com.hebaiyi.www.syllabus.syllabus.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hebaiyi.www.syllabus.R;
import com.hebaiyi.www.syllabus.util.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 课表显示view
 */
public class TimeTableView extends LinearLayout {

    // 最大节数
    private final static int MAX_NUM = 13;
    // 显示到星期几
    private final static int WEEK_NUM = 7;
    // 线宽
    private final static int TIME_TABLE_LINE_HEIGHT = 4;
    // 单个view的高度
    private final static int TIME_TABLE_HEIGHT = 65;
    // 第一行中星期的字体高度
    private final static int WEEK_NAME_HEIGHT = 30;
    // 表格中单个格子宽度
    private final static int TIME_TABLE_WIDTH = 20;
    // 星期名字
    private final String[] WEEK_NAME = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    // 第一行的星期显示
    private LinearLayout mHorizontalLayout;
    // 课程格子
    private LinearLayout mVerticalLayout;
    // 数据源
    private List<TimeTableModel> mDatas = new ArrayList<>();
    // 颜色字符串数组
    private String[] colorStr = new String[20];
    // 颜色数量
    private int colorNum = 0;
    // 当前周
    private int currWeek = 1;
    // 颜色数组
    private int[] color =
            {Color.parseColor("#e0FF6699"), Color.parseColor("#e0FF99CC"),
                    Color.parseColor("#e0CC99FF"), Color.parseColor("#e099CCFF"),
                    Color.parseColor("#e09999FF"), Color.parseColor("#e0FF6666"),
                    Color.parseColor("#e0FFFFCC"), Color.parseColor("#e0CCFFCC"),
                    Color.parseColor("#e033FFCC"), Color.parseColor("#e000FF33"),
                    Color.parseColor("#e066FF99"), Color.parseColor("#e099CCCC"),
                    Color.parseColor("#e0CC9999"), Color.parseColor("#e09999CC"),
                    Color.parseColor("#e0000099"), Color.parseColor("#e0663366"),
                    Color.parseColor("#e0006699")};

    public TimeTableView(Context context) {
        super(context);
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    /**
     * 横的分界线
     *
     * @return 横向分界线
     */
    private View obtianHorizontalLine() {
        TextView line = new TextView(getContext());
        line.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        line.setHeight(TIME_TABLE_LINE_HEIGHT);
        line.setWidth(LayoutParams.MATCH_PARENT);
        return line;
    }


    /**
     * 竖向分界线
     *
     * @return 竖向分界线
     */
    private View obtainVerticalLine() {
        TextView line = new TextView(getContext());
        line.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        line.setHeight(ViewUtil.dip2px(getContext(), WEEK_NAME_HEIGHT));
        line.setWidth((TIME_TABLE_LINE_HEIGHT));
        return line;
    }


    public void setTimeTable(List<TimeTableModel> mlist) {
        Collections.sort(mlist, new Comparator<TimeTableModel>() {
            @Override
            public int compare(TimeTableModel o1, TimeTableModel o2) {
                return o1.getStartNum() - o2.getStartNum();
            }
        });
        this.mDatas = mlist;
        for (TimeTableModel timeTableModel : mlist) {
            addTimeName(timeTableModel.getName());
        }
        setCurrWeek(currWeek);
    }

    public void setCurrWeek(int week) {
        if (week == 0) {
            throw new IllegalStateException("week must no be zero");
        }
        currWeek = week;
        List<TimeTableModel> models = new ArrayList<>(mDatas);
        for (int i = 0; i < models.size(); i++) {
            TimeTableModel model = models.get(i);
            List<Integer> weeks = model.getWeeks();
            if (!weeks.contains(week)) {
                models.remove(model);
            }
        }
        initView(models);
        invalidate();
    }


    /**
     * 初始化
     */
    private void initView(List<TimeTableModel> models) {
        // 初始化第一行星期显示布局
        mHorizontalLayout = new LinearLayout(getContext());
        mHorizontalLayout.setOrientation(HORIZONTAL);
        // 初始化课程格子布局
        mVerticalLayout = new LinearLayout(getContext());
        mVerticalLayout.setOrientation(HORIZONTAL);
        // 初始化表格
        initTable(models);
    }

    /**
     * 初始化表格
     */
    private void initTable(List<TimeTableModel> models) {
        for (int i = 0; i <= WEEK_NUM; i++) {
            switch (i) {
                case 0:
                    // 课程表出的0，0格子为空白
                    TextView zero = new TextView(getContext());
                    zero.setHeight(ViewUtil.dip2px(getContext(), WEEK_NAME_HEIGHT));
                    zero.setWidth(ViewUtil.dip2px(getContext(), TIME_TABLE_WIDTH));
                    mHorizontalLayout.addView(zero);
                    // 绘制左边的1~MAX_NUM
                    drawNumLayout();
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    // 设置显示从周一到周日
                    showWeekLayout(i, models);
                    break;
                default:
                    break;
            }
            TextView line = new TextView(getContext());
            line.setHeight(ViewUtil.dip2px(getContext(),
                    TIME_TABLE_HEIGHT * MAX_NUM) + MAX_NUM * 2);
            line.setWidth(2);
            line.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            mVerticalLayout.addView(line);
            mHorizontalLayout.addView(obtainVerticalLine());
        }
        removeAllViews();
        NestedScrollView nsv = new NestedScrollView(getContext());
        nsv.addView(mVerticalLayout);
        nsv.setOverScrollMode(OVER_SCROLL_NEVER);
        addView(mHorizontalLayout);
        addView(obtianHorizontalLine());
        addView(nsv);
        addView(obtianHorizontalLine());
    }

    /**
     * 显示周布局，显示星期一到星期日
     */
    private void showWeekLayout(int current, List<TimeTableModel> models) {
        LinearLayout weekLayout = new LinearLayout(getContext());
        weekLayout.setOrientation(VERTICAL);
        TextView weekTv = new TextView(getContext());
        weekTv.setTextColor(getResources().getColor(R.color.white));
        // 计算textView的宽度
        int width = (obtainViewWidth() -
                ViewUtil.dip2px(getContext(), TIME_TABLE_WIDTH)) / WEEK_NUM;
        weekTv.setWidth(width);
        weekTv.setHeight(ViewUtil.dip2px(getContext(), WEEK_NAME_HEIGHT));
        weekTv.setGravity(Gravity.CENTER);
        weekTv.setTextSize(16);
        weekTv.setText(WEEK_NAME[current - 1]);
        weekLayout.addView(weekTv);
        mHorizontalLayout.addView(weekLayout);
        // 显示课程信息
        showSyllabus(current, models);

    }

    /**
     * 展示课程信息
     */
    private void showSyllabus(int current, List<TimeTableModel> models) {
        // 用来保存当天的课程
        List<TimeTableModel> currDaySyll = new ArrayList<>();
        // 遍历出星期一到日的课表
        for (TimeTableModel timeTableModel : models) {
            if (timeTableModel.getDayOfWeek() == current) {
                currDaySyll.add(timeTableModel);
            }
        }
        // 添加
        LinearLayout layout = obtainTimeTableView(currDaySyll, current);
        layout.setOrientation(VERTICAL);
        ViewGroup.LayoutParams linearParams =
                new ViewGroup.LayoutParams(
                        (obtainViewWidth() -
                                ViewUtil.dip2px(getContext(), 20)) / WEEK_NUM,
                        LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(linearParams);
        layout.setWeightSum(1);
        mVerticalLayout.addView(layout);
    }

    /**
     * 获取当天的课表布局
     *
     * @param model   当天课程的集合
     * @param current 当前循环次数
     * @return 当天的课表
     */
    private LinearLayout obtainTimeTableView(List<TimeTableModel> model, int current) {
        LinearLayout currView = new LinearLayout(getContext());
        currView.setOrientation(VERTICAL);
        int size = model.size();
        if (size <= 0) {
            currView.addView(addStartView(MAX_NUM + 1, current, 0));
        } else {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    currView.addView(addStartView(model.get(0).getStartNum(), current, 0));
                    currView.addView(obtainModeView(model.get(0)));
                } else if (model.get(i).getStartNum() - model.get(i - 1).getStartNum() > 0) {
                    currView.addView(addStartView(
                            model.get(i).getStartNum() - model.get(i - 1).getEndNum(),
                            current, model.get(i - 1).getEndNum()));
                    currView.addView(obtainModeView(model.get(i)));
                }
                if (i + 1 == size) {
                    currView.addView(addStartView(MAX_NUM - model.get(i).getEndNum(),
                            current, model.get(i).getEndNum()));
                }
            }
        }
        return currView;
    }

    @SuppressLint("SetTextI18n")
    private View obtainModeView(final TimeTableModel model) {
        if (!model.getWeeks().contains(currWeek)) {
            return new LinearLayout(getContext());
        }
        LinearLayout tableView = new LinearLayout(getContext());
        tableView.setOrientation(VERTICAL);
        CardView card = new CardView(getContext());
        card.setCardElevation(ViewUtil.dip2px(getContext(), 5));
        card.setRadius(ViewUtil.dip2px(getContext(), 8));
        card.setCardBackgroundColor(color[getColorNum(model.getName())]);
        TextView contentTv = new TextView(getContext());
        int num = model.getEndNum() - model.getStartNum();
        contentTv.setHeight(ViewUtil.dip2px(getContext(),
                (num + 1) * TIME_TABLE_HEIGHT) + num * 2);
        contentTv.setTextColor(getContext()
                .getResources().getColor(R.color.white));
        contentTv.setWidth(ViewUtil.dip2px(getContext(), TIME_TABLE_HEIGHT));
        contentTv.setTextSize(16);
        contentTv.setGravity(Gravity.CENTER);
        contentTv.setText(model.getName() + "@" + model.getWhere());
        card.addView(contentTv);
        tableView.addView(card);
        tableView.addView(obtianHorizontalLine());
        tableView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeTableDialog dialog = new TimeTableDialog(getContext());
                dialog.setName(model.getName());
                dialog.setTeacher(model.getTeacher());
                dialog.setWhere(model.getWhere());
                dialog.setClazz(model.getClazz());
                dialog.show();
            }
        });
        return tableView;
    }

    private View addStartView(int startNum, final int week, final int start) {
        LinearLayout startView = new LinearLayout(getContext());
        startView.setOrientation(VERTICAL);
        for (int i = 1; i < startNum; i++) {
            TextView time = new TextView(getContext());
            time.setGravity(Gravity.CENTER);
            time.setHeight(ViewUtil.dip2px(getContext(), TIME_TABLE_HEIGHT));
            time.setWidth(ViewUtil.dip2px(getContext(), TIME_TABLE_HEIGHT));
            startView.addView(time);
            startView.addView(obtianHorizontalLine());
            //这里可以处理空白处点击添加课表
            time.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return startView;
    }


    /**
     * 获取屏幕的宽度
     *
     * @return 屏幕宽度
     */
    private int obtainViewWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


    /**
     * 绘制1~MAX_NUM
     */
    @SuppressLint("SetTextI18n")
    private void drawNumLayout() {
        LinearLayout numLayout = new LinearLayout(getContext());
        // 设置layoutParams
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewUtil.dip2px(getContext(), 35),
                        ViewUtil.dip2px(getContext(),
                                MAX_NUM * TIME_TABLE_HEIGHT) + 2 * MAX_NUM);
        numLayout.setLayoutParams(params);
        numLayout.setOrientation(VERTICAL);
        // 轮循创建textView并添加到布局中
        for (int j = 1; j <= MAX_NUM; j++) {
            TextView numTv = new TextView(getContext());
            numTv.setGravity(Gravity.CENTER);
            numTv.setTextColor(getResources().getColor(R.color.white));
            numTv.setHeight(ViewUtil.dip2px(getContext(), TIME_TABLE_HEIGHT));
            numTv.setWidth(ViewUtil.dip2px(getContext(), TIME_TABLE_WIDTH));
            numTv.setTextSize(16);
            numTv.setText(j + "");
            numLayout.addView(numTv);
            numLayout.addView(obtianHorizontalLine());
        }
        mVerticalLayout.addView(numLayout);
    }

    private void addTimeName(String name) {
        boolean isRepeat = true;
        for (int i = 0; i < 20; i++) {
            if (name.equals(colorStr[i])) {
                isRepeat = true;
                break;
            } else {
                isRepeat = false;
            }
        }
        if (!isRepeat) {
            colorStr[colorNum] = name;
            colorNum++;
        }
    }

    public int getColorNum(String name) {
        int num = 0;
        for (int i = 0; i < 20; i++) {
            if (name.equals(colorStr[i])) {
                num = i;
            }
        }
        return num;
    }
}


