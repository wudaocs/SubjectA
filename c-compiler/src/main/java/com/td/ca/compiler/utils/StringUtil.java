package com.td.ca.compiler.utils;

/**
 * Description :
 * Created by Wang Yue on 2018/11/8.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public class StringUtil {

    public static String generatedKey(String action,String type){
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
}
