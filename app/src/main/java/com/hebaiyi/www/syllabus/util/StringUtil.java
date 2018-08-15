package com.hebaiyi.www.syllabus.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 拼接字符串
     *
     * @param params 零散字符串
     * @return 拼接后的字符串
     */
    public static String joint(String... params) {
        if (params.length == 0) {
            throw new IllegalStateException("params must no be empty");
        }
        StringBuilder builder = new StringBuilder();
        for (String param : params) {
            builder.append(param);
        }
        return builder.toString();
    }

    /**
     * 判断字符串是否为数据
     *
     * @param str 字符串
     * @return 是否为数字
     */
    public static boolean isDigital(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) >= 48 && str.charAt(i) <= 57)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取字符串中的非零数字
     *
     * @param str 相应的字符串
     * @return 相应的数字结果
     */
    public static int obtainNonZeroDigitalInString(String str) {
        Pattern pattern = Pattern.compile("^[1-9]\\d$");
        Matcher matcher = pattern.matcher(str);
        List<Integer> matchers = new ArrayList<>();
        while (matcher.find()) {
            matchers.add(Integer.parseInt(matcher.group()));
        }
        int size = matchers.size();
        if (size == 0) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < size; i++) {
            int product = 10 * (size - 1);
            sum += matchers.get(i) * product;
        }
        return sum;
    }

    /**
     * 获取字符串中的非零数字数组
     *
     * @param str 对应的字符串
     * @return 非零数字数组
     */
    public static List<Integer> obtainNonZeroAarryInString(String str) {
        List<Integer> matchers = new ArrayList<>();
        String[] stii = str.split(",");
        for (String s : stii) {
            for (int j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                if (c == 48 && j == 0) {
                    matchers.add(s.charAt(j + 1) - 48);
                    break;
                } else {
                    matchers.add(Integer.parseInt(s));
                    break;
                }
            }
        }
        if (matchers.size() == 0) {
            return null;
        }
        return matchers;
    }


}
