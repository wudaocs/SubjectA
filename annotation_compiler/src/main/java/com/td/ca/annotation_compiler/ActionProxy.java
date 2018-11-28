package com.td.ca.annotation_compiler;

import com.td.ca.annotation_compiler.interfaces.IActionProxy;
import com.td.ca.annotation_compiler.interfaces.ISCall;
import com.td.ca.annotation_compiler.interfaces.ISError;
import com.td.ca.annotation_compiler.interfaces.InvokeCallback;
import com.td.ca.annotation_compiler.threads.AThreadPoolManager;
import com.td.ca.annotation_compiler.threads.Priority;
import com.td.ca.annotation_compiler.threads.QueueRunnable;
import com.td.ca.annotation_compiler.utils.StringUtil;
import com.td.ca.annotation_compiler.utils.TSubjectConfigs;
import com.td.ca.annotation_compiler.utils.TypeParser;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Description :
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
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
        for (int i = 0; i < 1000; i++) {
            try {
                // 查找订阅类
                Class clz = Class.forName(TSubjectConfigs.PACKAGE_NAME + "." + className + "." + className + "_" + i);
                if (clz == null) {
                    // 没有找到对应的类文件
                    if (invokeCallback != null) {
                        invokeCallback.error("subject class not exist", null);
                    }
                    return null;
                }
                // 查找订阅关系对应方法
                Method method = clz.getMethod("invoke", String.class, HashMap.class);
                if (method == null) {
                    // 没有找到对应的类文件
                    if (invokeCallback != null) {
                        invokeCallback.error("subject action not exist", null);
                    }
                    return null;
                }
                // 无参调用
                Object invoke = method.invoke(clz.newInstance(), StringUtil.generatedKey(mAction, String.valueOf(mActionType)), map);
                // 需要返回才返回
                if (invoke != null && invokeCallback != null) {
                    invokeCallback.success(invoke);
                }
            } catch (ClassNotFoundException e) {
                e.getStackTrace();
                break;
            } catch (IllegalAccessException e) {
                e.getStackTrace();
                break;
            } catch (NoSuchMethodException e) {
                e.getStackTrace();
                break;
            } catch (Exception e) {
                e.getStackTrace();
                break;
            }
        }
        return null;
    }
}
