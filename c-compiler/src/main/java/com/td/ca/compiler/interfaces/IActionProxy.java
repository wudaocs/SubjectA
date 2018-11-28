package com.td.ca.compiler.interfaces;

/**
 * Description :
 * Created by Wang Yue on 2018/10/30.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public interface IActionProxy<T> {
    // 异步操作
    void call();
    // 异步操作
    void call(ISCall<T> isCall);
    // 异步操作
    void call(ISCall<T> isCall, ISError isError);
    // 同步操作
    Object execute();
}
