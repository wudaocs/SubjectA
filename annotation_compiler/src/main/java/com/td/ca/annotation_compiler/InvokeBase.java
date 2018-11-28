package com.td.ca.annotation_compiler;

import com.td.ca.annotation_compiler.entity.ParamEntity;
import com.td.ca.annotation_compiler.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Description : 调用具体函数基类
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public class InvokeBase {

    protected Object invoke(HashMap<String, Object> map, String action, Map<String, Object> params) {

        Object[] objects = null;
        // 执行方法次数，默认为0 未执行
        int count = 0;
        if (map != null) {
            // 循环取出当前订阅中的参数
            ParamEntity paramEntity;
            int paramSize = params != null ? params.size() : 0;
            for (Map.Entry<String, Object> m : map.entrySet()) {
                paramEntity = (ParamEntity) m.getValue();
                if (paramEntity != null) {
                    // 比对参数数量和类型
                    Class[] p = paramEntity.getParamsType();
                    if ((p == null && paramSize == 0) || (p != null && p.length == paramSize)) {
                        if (params != null && p != null && !params.isEmpty()) {
                            int size = params.size();
                            objects = new Object[size];
                            for (int i = 0; i < size; i++) {
                                if (StringUtil.equalsClass(p[i], params.get(paramEntity.getParams()[i]).getClass())) {
                                    objects[i] = params.get(paramEntity.getParams()[i]);
                                }
                            }
                            if (p.length != objects.length) {
                                continue;
                            }
                        }
                        if (map.size() > 1) {
                            invoke(paramEntity.getClasspath(), paramEntity.getMethodName(), paramEntity.getParamsType(), objects);
                        } else {
                            return invoke(paramEntity.getClasspath(), paramEntity.getMethodName(), paramEntity.getParamsType(), objects);
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * 基类调用反射
     *
     * @param classpath  类路径
     * @param methodName 方法名称
     * @param cls        方法对应参数
     * @param obj        方法对应参数值
     * @return 方法结果，如果返回类型为void则返回null
     */
    @SuppressWarnings("unchecked")
    private Object invoke(String classpath, String methodName, Class[] cls, Object[] obj) {
        // 查找订阅类
        Class clz = null;
        try {
            clz = Class.forName(classpath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clz == null) {
            return null;
        }
        // 查找订阅关系对应方法
        Method method = null;
        try {
            if (cls == null) {
                method = clz.getMethod(methodName);
            } else {
                method = clz.getMethod(methodName, cls);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (method == null) {
            // 没有找到对应的类文件
            return null;
        }
        try {
            // 无参调用
            Class c = method.getReturnType();
            if ("void".equals(c.getName())) {
                if (obj == null) {
                    method.invoke(clz.newInstance());
                } else {
                    method.invoke(clz.newInstance(), obj);
                }
                return null;
            } else {
                if (obj == null) {
                    return method.invoke(clz.newInstance());
                } else {
                    return method.invoke(clz.newInstance(), obj);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
