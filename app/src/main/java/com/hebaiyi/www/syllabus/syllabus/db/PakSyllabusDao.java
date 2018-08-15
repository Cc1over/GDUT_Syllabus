package com.hebaiyi.www.syllabus.syllabus.db;

import com.hebaiyi.www.syllabus.app.SyllabusApplication;
import com.hebaiyi.www.syllabus.syllabus.bean.DaoMaster;
import com.hebaiyi.www.syllabus.syllabus.bean.DaoSession;
import com.hebaiyi.www.syllabus.syllabus.bean.Syllabus;
import com.hebaiyi.www.syllabus.syllabus.bean.SyllabusDao;
import com.hebaiyi.www.syllabus.util.ContainerUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class PakSyllabusDao {

    private SyllabusDao mSyllabusDao;

    private static class SinglePakSyllabusDao {
        private static PakSyllabusDao instance = new PakSyllabusDao();
    }

    public static PakSyllabusDao getInstance() {
        return SinglePakSyllabusDao.instance;
    }

    private PakSyllabusDao() {
        // 初始化
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                SyllabusApplication.getContext(), "syllabus.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession session = daoMaster.newSession();
        mSyllabusDao = session.getSyllabusDao();
    }

    /**
     * 插入数据
     *
     * @param syllabuses 课程信息列表
     */
    public void insertAll(List<Syllabus> syllabuses) {
        if (ContainerUtil.isEmpty(syllabuses)) {
            throw new IllegalStateException("syllabuses must not be empty");
        }
        for (Syllabus syllabus : syllabuses) {
            // 对每个标记进行处理
            syllabus.treatMask();
            // 插入数据
            mSyllabusDao.insert(syllabus);
        }
    }

    /**
     *  插入单条数据
     * @return 是否成功
     */
    public long insert(Syllabus syllabus){
        // 对标记mask进行处理
        syllabus.treatMask();
        // 插入数据
        return mSyllabusDao.insert(syllabus);
    }

    /**
     * 更新数据
     *
     * @param syllabus 课程信息
     */
    public void update(Syllabus syllabus) {
        mSyllabusDao.update(syllabus);
    }

    /**
     * 删除全部
     */
    public void deleteAll() {
        mSyllabusDao.deleteAll();
    }

    /**
     * 查询课表信息列表
     *
     * @return 课表信息列表
     */
    public List<Syllabus> queryAll() {
        QueryBuilder<Syllabus> qb = mSyllabusDao.queryBuilder();
        return qb.list();
    }

}
