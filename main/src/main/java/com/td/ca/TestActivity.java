package com.td.ca;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.td.ca.annotation_compiler.TNotice;

import java.util.HashMap;

/**
 * Description :
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        get();
    }

    private void get() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("id",1L);
//        map.put("sex",1);
//        map.put("name","name");
//        map.put("person",133);
//        Entity entity = (Entity) TNotice.send("test", "gettest", 1, map).execute();
//        Entity entity = (Entity) TNotice.send("test", "gettest", 1).execute();
        HashMap<String, Object> notice = new HashMap<>();
//        notice.put("entity", new ChildEntity("12345"));
        notice.put("context",this);
        notice.put("name","12345");
        TNotice.send("notice", "openNotice", notice).execute();


//        String packageName = TSubjectConfigs.PACKAGE_NAME + "." + "notice";
//        System.out.println("subject= packageName " + packageName);
//        File file = new File(packageName.replace(".", "/"));
//        System.out.println("subject= 包是否存在 " + file.getAbsolutePath() + "  " + file.exists());
//        try {
//            Class c = Class.forName("com.systoon.ac.notice.TestSubjectUser");
//            System.out.println("subject= c " + c.getName());
//            System.out.println("subject= c " + c.getPackage());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

}
