package com.td.ca.annotation_compiler.interfaces;

/**
 * Description : 自动生成数据类型
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public interface IActionProxy<T> {
    // 异步操作
    void call();
    // 异步操作
    void call(ISCall<T> isCall);
    // 异步操作
    void call(ISCall<T> isCall,ISError isError);
    // 同步操作
    Object execute();
}
