package com.hebaiyi.www.syllabus.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hebaiyi.www.syllabus.syllabus.bean.Syllabus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json数据解析类
 */
public class Utility {

    /**
     * 获取课程表信息列表
     *
     * @param json json字符串
     * @return 课程表信息列表
     */
    public static List<Syllabus> analyzeSyllabus(String json) {
        if (json.isEmpty()) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Syllabus>>() {
        }.getType());
    }

    /**
     * 从html文本中截取json字符串
     *
     * @return 相应的json字符串
     */
    public static String grabJsonInHtml(String html) {
        Document doc = Jsoup.parse(html);
        String script = doc.select("script").last().toString();
        List<String> matcherList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?<=\\[)(\\S+)(?=\\])");
        Matcher matcher = pattern.matcher(script);
        while (matcher.find()) {
            matcherList.add(matcher.group());
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matcherList.size(); i++) {
            builder.append(matcherList.get(i));
        }
        String json = builder.toString();
        String editJson = json.substring(0, json.lastIndexOf("}") + 1);
        return StringUtil.joint("[", editJson, "]");
    }


}
