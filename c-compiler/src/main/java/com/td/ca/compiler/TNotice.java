package com.td.ca.compiler;


import com.td.ca.c_annotations.TConfigs;

import java.util.HashMap;

/**
 * Description : 执行通知
 * Created by Wang Yue on 2018/10/30.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public class TNotice {

    public static <T> ActionProxy<T> send(String subjectBusiness, String action) {
        return send(subjectBusiness, action, TConfigs.TYPE_DEFAULT_VALUE, null);
    }

    public static <T> ActionProxy<T> send(String subjectBusiness, String action, int actionType) {
        return send(subjectBusiness, action, actionType, null);
    }

    public static <T> ActionProxy<T> send(String subjectBusiness, String action, HashMap<String, Object> map) {
        return send(subjectBusiness, action, TConfigs.TYPE_DEFAULT_VALUE, map);
    }

    public static <T> ActionProxy<T> send(String subjectBusiness, String action, int actionType, HashMap<String, Object> map) {
        return new ActionProxy<>(subjectBusiness, action, actionType, map);
    }
}
