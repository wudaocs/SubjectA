package com.td.ca.compiler.interfaces;

/**
 * Description : 订阅错误回调
 * Created by Wang Yue on 2018/10/31.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public interface ISError {
    void done(String message, Exception e);
}
