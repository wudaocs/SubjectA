package com.td.ca.compiler.interfaces;

/**
 * Description : 反射回调
 * Created by Wang Yue on 2018/11/2.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public interface InvokeCallback {

    void success(Object o);

    void error(String message, Exception e);
}
