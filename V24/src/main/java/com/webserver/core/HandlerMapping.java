package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2022-04-20 10:09
 *
 * 该类维护所有请求与对应的业务处理类的关系
 */
public class HandlerMapping {
    /*
        key:请求路径
        value:处理该请求的Controller和对应的业务方法
     */
    private static Map<String,MethodMapping> mapping = new HashMap<>();

    static {
       initMapping();
    }
    private static void initMapping(){
        try {
            File dir = new File(
                    HandlerMapping.class.getClassLoader().
                            getResource("./com/webserver/controller").toURI()
            );
            File[] subs = dir.listFiles(f -> f.getName().endsWith(".class"));
            for (File f : subs){
                String className = f.getName().substring(0, f.getName().indexOf("."));
                Class cls = Class.forName("com.webserver.controller."+className);
                //判断该类是否被@Controller标注了
                if (cls.isAnnotationPresent(Controller.class)){
                    Object controller = cls.newInstance();//将该Controller实例化
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods){
                        //判断该方法是否被@RequestMapping标注了
                        if (method.isAnnotationPresent(RequestMapping.class)){
                            //获取该注解
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            //获取该注解的参数(该方法处理的请求路径)
                            String value = rm.value();
                            MethodMapping methodMapping = new MethodMapping(controller,method);
                            mapping.put(value,methodMapping);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MethodMapping mm = mapping.get("/myweb/reg");
        System.out.println(mm);
    }

    /**
     * 根据给定的请求路径获取处理该请求的Controller及对应的业务方法
     * @param path
     * @return
     */
    public static MethodMapping getMethod(String path){
        return mapping.get(path);
    }
    /**
     * 每个实例用于记录一个业务方法以及该方法所属的Controller对象
     */
    public static class MethodMapping{
        private Object controller;
        private Method method;

        public MethodMapping(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public Object getController() {
            return controller;
        }


        public Method getMethod() {
            return method;
        }

    }
}
