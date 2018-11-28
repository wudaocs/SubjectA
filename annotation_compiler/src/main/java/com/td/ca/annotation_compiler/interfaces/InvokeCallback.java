package com.td.ca.annotation_compiler.interfaces;

/**
 * Description : 反射回调
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public interface InvokeCallback {

    void success(Object o);

    void error(String message, Exception e);
}
