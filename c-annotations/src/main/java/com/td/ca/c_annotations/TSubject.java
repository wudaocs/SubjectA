package com.td.ca.c_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description : 订阅关系
 * Created by Wang Yue on 2018/10/29.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TSubject {

    // 默认值
    int value() default TConfigs.TYPE_DEFAULT_VALUE;

    // 订阅类型
    String type() default "";

    // 订阅业务 business
    String bs() default "business";

}
