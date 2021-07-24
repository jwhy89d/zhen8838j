package com.webserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shkstart
 * @create 2022-04-19 15:43
 *
 * 该注解用于标注那些在Controller中用于处理某个请求的业务方法使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();//用于指定标注的方法所处理的请求路径，比如:"/myweb/reg"
}
