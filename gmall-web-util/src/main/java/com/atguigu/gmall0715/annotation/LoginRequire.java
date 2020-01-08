package com.atguigu.gmall0715.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加该注解的Controller方法，用来指定是否每次调用时检测是否登录
 * @author sujie
 * @date 2020-01-07-22:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequire {
    //是否需要登录标识
    boolean autoRedirect() default true;
}
