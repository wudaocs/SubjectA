package com.td.ca.compiler;

import com.systoon.ca.annotation_compiler.interfaces.IActionProxy;
import com.systoon.ca.annotation_compiler.interfaces.ISCall;
import com.systoon.ca.annotation_compiler.interfaces.ISError;
import com.systoon.ca.annotation_compiler.interfaces.InvokeCallback;
import com.systoon.ca.annotation_compiler.threads.AThreadPoolManager;
import com.systoon.ca.annotation_compiler.threads.Priority;
import com.systoon.ca.annotation_compiler.threads.QueueRunnable;
import com.systoon.ca.annotation_compiler.utils.StringUtil;
import com.systoon.ca.annotation_compiler.utils.TSubjectConfigs;
import com.systoon.ca.annotation_compiler.utils.TypeParser;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Description :
 * Created by Wang Yue on 2018/10/30.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public class ActionProxy<T> implements IActionProxy<T> {

    // 订阅业务
    private String mSubjectBusiness;
    // 业务功能
    private String mAction;
    // 功能类型
    private int mActionType;
    // 参数
    private HashMap<String, Object> map;

    ActionProxy(String subjectBs, String actionValue, int actionType, HashMap<String, Object> params) {
        mSubjectBusiness = subjectBs;
        mAction = actionValue;
        mActionType = actionType;
        map = params;
    }

    @Override
    public void call() {
        call(null, null);
    }

    @Override
    public void call(ISCall isCall) {
        call(isCall, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void call(final ISCall<T> isCall, final ISError isError) {
        final InvokeCallback invokeCallback = new InvokeCallback() {
            @Override
            public void success(Object o) {
                if (isCall != null) {
                    isCall.done((T) TypeParser.typeParse(o, isCall));
                }
            }

            @Override
            public void error(String message, Exception e) {
                if (isError != null) {
                    isError.done(message, e);
                }
            }
        };
        AThreadPoolManager.getThreadPool().execute(new QueueRunnable(Priority.NORMAL, new Runnable() {
            @Override
            public void run() {
                invoke(mSubjectBusiness, invokeCallback);
            }
        }));
    }

    @Override
    public Object execute() {
        return invoke(mSubjectBusiness, null);
    }


    @SuppressWarnings("unchecked")
    private Object invoke(String className, InvokeCallback invokeCallback) {
        // 查找订阅类
        Class clz = null;
        try {
            System.out.println(TSubjectConfigs.PACKAGE_NAME + "." + className);
            clz = Class.forName(TSubjectConfigs.PACKAGE_NAME + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clz == null) {
            // 没有找到对应的类文件
            if (invokeCallback != null) {
                invokeCallback.error("subject class not exist", null);
            }
            return null;
        }
        // 查找订阅关系对应方法
        Method method = null;
        try {
            method = clz.getMethod("invoke", String.class, HashMap.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (method == null) {
            // 没有找到对应的类文件
            if (invokeCallback != null) {
                invokeCallback.error("subject action not exist", null);
            }
            return null;
        }
        Object invoke = null;
        try {
            // 无参调用
            invoke = method.invoke(clz.newInstance(), StringUtil.generatedKey(mAction, String.valueOf(mActionType)), map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.getStackTrace();
        }
        // 需要返回才返回
        if (invoke != null && invokeCallback != null) {
            invokeCallback.success(invoke);
        }
        return null;
    }
}
