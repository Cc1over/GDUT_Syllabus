package com.hebaiyi.www.syllabus.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsoupUtil {

    private static final String HEADER_NAME = "User-Agent";
    private static final String HEADER_VALUES = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";

    /**
     * 登录进指定网页
     *
     * @param url       指定网页的url
     * @param studentId 学号
     * @param password  密码
     */
    public static void enter(final String url, final String studentId,
                             final String password, final EnterCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取第一次连接
                Connection con = Jsoup.connect(url);
                // 模拟浏览器
                con.header(HEADER_NAME, HEADER_VALUES);
                //获取响应
                Connection.Response rs;
                try {
                    rs = con.execute();
                    //转换为Dom树
                    Document document = Jsoup.parse(rs.body());
                    List<Element> et = document.select("#casLoginForm");
                    if (et.isEmpty()) {
                        callback.onInformationFail();
                        return;
                    }
                    // 获取cooking和表单属性，下面map存放post的数据
                    Map<String, String> datas = new HashMap<>();
                    for (Element e : et.get(0).getAllElements()) {
                        if (e.attr("name").equals("username")) {
                            e.attr("value", studentId);
                        }
                        if (e.attr("name").equals("password")) {
                            e.attr("value", password);
                        }
                        if (e.attr("name").length() > 0) {
                            //排除空值表单属性
                            datas.put(e.attr("name"), e.attr("value"));
                        }
                    }
                    //第二次请求，post表单数据，以及cookie信息
                    Connection con2 = Jsoup.connect(url);
                    con2.header(HEADER_NAME, HEADER_VALUES);
                    //设置cookie和post上面的map数据
                    Connection.Response login = con2
                            .method(Connection.Method.POST)
                            .data(datas)
                            .cookies(rs.cookies())
                            .execute();
                    Map<String, String> map = login.cookies();
                    // 对返回结果进行判断
                    if (!"{}".equals(login.cookies().toString())) {
                        callback.onSuccess(map);
                    }else{
                        callback.onInformationFail();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onSystemFail();
                }
            }
        }).start();
    }

    /**
     * 访问网页
     *
     * @param url         相应的url
     * @param cookies     cookies
     * @param requestData 请求参数
     * @param callback    回调接口
     */
    public static void access(final String url,
                              final Map<String, String> cookies,
                              final Map<String, String> requestData,
                              final AccessCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con2 = Jsoup.connect(url);
                con2.header(HEADER_NAME, HEADER_VALUES);
                //设置cookie和post上面的map数据
                try {
                    Connection.Response curriculum = con2
                            .method(Connection.Method.POST)
                            .data(requestData)
                            .cookies(cookies)
                            .execute();
                    callback.onSuccess(curriculum.body());
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onFail();
                }
            }
        }).start();
    }

    /**
     * Jsoup登录方法的回调
     */
    public interface EnterCallback {

        void onInformationFail();

        void onSystemFail();

        void onSuccess(Map<String, String> cookies);
    }

    /**
     * Jsoup访问方法的回调
     */
    public interface AccessCallback {

        void onFail();

        void onSuccess(String json);
    }


}
