package com.td.ca.annotation_compiler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description : 订阅关系的进一步行为
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TAction {

    // 行为值
    String value() default "default";

    // 行为类型
    int type() default TConfigs.TYPE_DEFAULT_VALUE;

}
