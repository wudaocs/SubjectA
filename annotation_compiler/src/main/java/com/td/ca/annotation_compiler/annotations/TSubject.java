package com.td.ca.annotation_compiler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description : 订阅关系
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TSubject {

    // 默认值
    int value() default TConfigs.TYPE_DEFAULT_VALUE;

    // 订阅类型
    String type() default "";

    // 订阅业务 business
    String target() default "business";

}
