package com.td.ca.annotation_compiler.utils;

/**
 * Description : 字符处理工具类
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public class StringUtil {

    public static String generatedKey(String action, String type) {
        return action + "_" + type;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean equalsClass(Class type, Class param) {
        // 检查基本类型
        CharSequence a = type.getSimpleName().toLowerCase();
        CharSequence b = param.getSimpleName().toLowerCase();
        if (equals(a, b.toString().toLowerCase()) || equals(a, "object") || equals(b, "object") || (a.toString().startsWith("int") && b.toString().startsWith("int"))) {
            return true;
        }
        // 非基本类型,循环4层
        int index = 0;
        Class superClass = null;
        CharSequence typeName = type.getName();
        boolean isEqual = false;
        while (index < 4) {
            if (index == 0) {
                superClass = param.getSuperclass();
            } else {
                superClass = superClass.getSuperclass();
            }
            if (equals(typeName, superClass.getName())) {
                isEqual = true;
                break;
            }
            index++;
        }
        return isEqual;
    }
}
