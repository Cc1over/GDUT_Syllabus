package com.hebaiyi.www.syllabus.util;

import java.util.Collection;

public class ContainerUtil {

    /**
     *  判断集合是否为空
     * @param collection 相应的集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection collection){
        return collection == null || collection.size() == 0;
    }

}
