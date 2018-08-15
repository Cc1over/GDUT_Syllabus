package com.hebaiyi.www.syllabus.syllabus.model;

import com.hebaiyi.www.syllabus.syllabus.bean.Syllabus;
import com.hebaiyi.www.syllabus.syllabus.bean.URL;
import com.hebaiyi.www.syllabus.syllabus.db.PakSyllabusDao;
import com.hebaiyi.www.syllabus.util.ContainerUtil;
import com.hebaiyi.www.syllabus.util.JsoupUtil;
import com.hebaiyi.www.syllabus.util.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyllabusModel {

   private PakSyllabusDao mPakDao;

    public SyllabusModel() {
        // 初始化
        mPakDao = PakSyllabusDao.getInstance();
    }



    /**
     * 获取课表信息
     *
     * @param cookies  cookies
     * @param callback 回调接口
     */
    public void obtainSyllabus(Map<String, String> cookies,
                               final SyllabusModelCallback callback) {
        boolean obtain = obtainSyllabusFromDb(callback);
        if (!obtain) {
            // 从网络中爬取课程表信息
            obtainSyllabusFromHttp(cookies, callback);
        }
    }

    /**
     * 从网络中爬取课程表信息
     */
    private void obtainSyllabusFromHttp(Map<String, String> cookies,
                                        final SyllabusModelCallback callback) {
        Map<String, String> datas = new HashMap<>();
        datas.put("xnxqdm", "201801");
        // 获取课表信息
        JsoupUtil.access(URL.GDUT_CURRICULUM_URL, cookies, datas,
                new JsoupUtil.AccessCallback() {
                    @Override
                    public void onFail() {
                        callback.onFail();
                    }

                    @Override
                    public void onSuccess(String html) {
                        callback.onSuccess(accessSuccess(html));
                    }
                });
    }

    private boolean obtainSyllabusFromDb(final SyllabusModelCallback callback) {
        List<Syllabus> syllabuses = mPakDao.queryAll();
        if(ContainerUtil.isEmpty(syllabuses)){
            return false;
        }
        callback.onSuccess(syllabuses);
        return true;
    }

    /**
     *  删除全部
     */
    public void deleteAll(){
        mPakDao.deleteAll();
    }

    /**
     * 网络请求成功后对html文本的处理解析
     *
     * @param html 对应的html文本
     * @return 课程表信息列表
     */
    private List<Syllabus> accessSuccess(String html) {
        // 抓获html文本中的json字符串
        String json = Utility.grabJsonInHtml(html);
        // 解析数据
        List<Syllabus> syllabuses = Utility.analyzeSyllabus(json);
        if (syllabuses == null) {
            return null;
        }
        // 插入数据
        mPakDao.insertAll(syllabuses);
        // 返回列表
        return syllabuses;
    }


}
