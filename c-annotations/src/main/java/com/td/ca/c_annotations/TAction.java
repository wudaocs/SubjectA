package com.td.ca.c_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description : 订阅关系的进一步行为
 * Created by Wang Yue on 2018/10/29.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TAction {

    // 行为值
    String value() default "default";

    // 行为类型
    int type() default TConfigs.TYPE_DEFAULT_VALUE;

}
