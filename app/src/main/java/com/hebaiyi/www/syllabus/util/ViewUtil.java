package com.hebaiyi.www.syllabus.util;

import android.content.Context;

public class ViewUtil {

    /**
     * 根据手机分辨率从dp的单位转换成px
     *
     * @param context 对应的上下文
     * @param dpValue dp值
     * @return 相应的px值
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px的单位转化为dp
     *
     * @param context 对应的上下文
     * @param pxValue px值
     * @return 相应的dp值
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
