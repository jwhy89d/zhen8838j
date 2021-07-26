package com.webserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shkstart
 * @create 2022-04-19 15:38
 *
 * 该注解用于标注哪些是处理业务的Controller类，会被DispatcherServlet识别并扫描其中的业务方法
 * 来处理对应的请求
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
}
