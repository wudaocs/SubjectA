package com.td.ca;

import com.td.ca.annotation_compiler.annotations.TAction;
import com.td.ca.annotation_compiler.annotations.TSubject;

/**
 * Description :
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
@TSubject(target = "test")
public class TestNotice1 {

    @TAction(value = "gettest", type = 1)
    public void get() {
        System.out.println("getgetget");
    }

    @TAction(value = "gettest1", type = 2)
    public void set(String id,String name,Entity entity) {
        System.out.println("setsetset");
    }

}
