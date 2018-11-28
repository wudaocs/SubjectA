package com.td.ca.annotation_compiler;

import com.td.ca.annotation_compiler.annotations.TConfigs;

import java.util.HashMap;

/**
 * Description : 执行通知
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
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
