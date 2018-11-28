package com.td.ca;

import android.util.Log;

import com.td.ca.annotation_compiler.annotations.TAction;
import com.td.ca.annotation_compiler.annotations.TSubject;

/**
 * Description :
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
@TSubject(target = "notice")
public class TestSubjectNotice {

    @TAction(value = "openNotice")
    public void openNotice(Entity entity) {
        Log.v(Configs.TAG,"执行 TestSubjectNotice openNotice  " + entity.getName());
//        context.startActivity(new Intent(context, Test2Activity.class));
    }
//    @TAction(value = "openNotice")
//    public void openNotice(Context context, String name) {
//        Log.v(Configs.TAG,"执行 TestSubjectNotice openNotice");
//        context.startActivity(new Intent(context, Test2Activity.class));
//    }
}
