package com.td.ca;

import com.td.ca.annotation_compiler.annotations.TAction;
import com.td.ca.annotation_compiler.annotations.TSubject;

/**
 * Description :
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
@TSubject(target = "test")
public class TestNotice {

    @TAction(value = "gettest1", type = 1)
    public void get() {
        System.out.println("sssss getgetget");
    }

    @TAction(value = "gettest", type = 1)
    public void set(Long id, int sex, String name, Object person) {
        System.out.println("sssss TestNotice set 带参数");
        System.out.println("sssss TestNotice set =" + sex);
        System.out.println("sssss TestNotice name =" + name);
        System.out.println("sssss TestNotice person =" + person);
    }

}
