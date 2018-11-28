package com.td.ca.compiler.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description :
 * Created by Wang Yue on 2018/10/31.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public class TypeParser {

    public static <T> Object typeParse(Object data, T t) {
        try {
            String type = getGeneric(t);
            if ("int" .equals(type) || "java.lang.Integer" .equals(type)) {
                data = valueOfInt(data);
            } else if ("float" .equals(type) || "java.lang.Float" .equals(type)) {
                data = valueOfFloat(data);
            } else if ("double" .equals(type) || "java.lang.Double" .equals(type)) {
                data = valueOfDouble(data);
            } else if ("long" .equals(type) || "java.lang.Long" .equals(type)) {
                data = valueOfLong(data);
            } else if ("boolean" .equals(type) || "java.lang.Boolean" .equals(type)) {
                data = valueOfBoolean(data);
            } else if (type.contains("[")) {
                data = valueOfArray(data);
            } else if (type.contains("java.util.List") || type.contains("java.util.ArrayList")) {
                data = valueOfList(data);
            } else if (type.contains("java.util.Map") || type.contains("java.util.HashMap")) {
                data = valueOfMap(data);
            } else if (!"java.lang.Object" .equals(type)) {
                data = valueOfObject(data);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return data;
    }

    private static <T> String getGeneric(T t) {
        ParameterizedType pt = null;
        Type[] types = t.getClass().getGenericInterfaces();
        if (types.length != 0 && types[0] instanceof ParameterizedType) {
            pt = (ParameterizedType) types[0];
        } else {
            Type type = t.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                pt = (ParameterizedType) type;
            }
        }
        String genericString = "";
        if (pt != null) {
            types = pt.getActualTypeArguments();
            String unsafe = types[0].toString();
            if (unsafe.contains(",")) {
                genericString = unsafe;
            } else {
                String[] elms = unsafe.split(" ");
                genericString = elms.length <= 1 ? unsafe : elms[1];
            }
        }
        return genericString.length() < 4 ? "" : genericString;
    }

    private static Integer valueOfInt(Object data) {
        if (data instanceof Integer) {
            return (Integer) data;
        } else if (data instanceof Number) {
            return ((Number) data).intValue();
        } else if (data instanceof String) {
            try {
                return Integer.parseInt((String) data);
            } catch (NumberFormatException e) {
                e.getStackTrace();
            }
        }
        return 0;
    }

    private static Float valueOfFloat(Object data) {
        if (data instanceof Float) {
            return (Float) data;
        } else if (data instanceof Number) {
            return ((Number) data).floatValue();
        } else if (data instanceof String) {
            return Float.valueOf((String) data);
        }
        return 0f;
    }

    private static Double valueOfDouble(Object data) {
        if (data instanceof Double) {
            return (Double) data;
        } else if (data instanceof Number) {
            return ((Number) data).doubleValue();
        } else if (data instanceof String) {
            return Double.valueOf((String) data);
        }
        return 0d;
    }

    private static Long valueOfLong(Object data) {
        if (data instanceof Long) {
            return (Long) data;
        } else if (data instanceof Number) {
            return ((Number) data).longValue();
        } else if (data instanceof String) {
            return Long.valueOf((String) data);
        }
        return 0L;
    }

    private static Boolean valueOfBoolean(Object data) {
        if (data instanceof Boolean) {
            return (Boolean) data;
        } else if (data instanceof String) {
            return Boolean.valueOf((String) data);
        }
        return false;
    }

    private static Object valueOfObject(Object data) {
        return data;
    }

    private static Object valueOfMap(Object data) {
        return data;
    }

    private static Object valueOfList(Object data) {
        return data;
    }

    private static Object valueOfArray(Object data) {
        return data;
    }
}
