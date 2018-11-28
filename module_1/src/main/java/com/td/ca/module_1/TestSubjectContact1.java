package com.td.ca.module_1;

import android.content.Context;
import android.util.Log;

import com.td.ca.annotation_compiler.annotations.TAction;
import com.td.ca.annotation_compiler.annotations.TSubject;


/**
 * Description :
 * Created by Wang Yue on 2018/11/20.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
@TSubject(target = "notice")
public class TestSubjectContact1 {

    @TAction(value = "openNotice")
    public void openNotice(Context context, String name) {
        Log.v("subject","执行 TestSubjectContact1 openNotice " + context + " - " + name);
    }
}
