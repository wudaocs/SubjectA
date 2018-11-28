package com.td.ca.annotation_compiler.interfaces;

/**
 * Description : 订阅错误回调
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public interface ISError {
    void done(String message, Exception e);
}
