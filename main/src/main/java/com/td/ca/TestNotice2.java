package com.td.ca;

import com.td.ca.annotation_compiler.annotations.TAction;
import com.td.ca.annotation_compiler.annotations.TSubject;

/**
 * Description :
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
@TSubject(target = "TestNotice2")
public class TestNotice2 {

    @TAction(value = "gettest1")
    public void get() {
        System.out.println("getgetget");
    }

    @TAction(value = "gettest1",type = 1)
    public void set(String id,String name,Entity entity) {
        System.out.println("setsetset");
    }

}
