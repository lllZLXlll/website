

package com.wchm.website.annotation;


import java.lang.annotation.*;



/**
 * 自定义注解
 */


@Documented
@Retention(RetentionPolicy.RUNTIME)// 注解在哪个阶段执行
@Target(ElementType.METHOD)  // 注解放置的目标位置,METHOD是可注解在方法级别上
public  @interface MyLog {
    String value() default "";
}

