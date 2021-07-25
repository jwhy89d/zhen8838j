package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.controller.ArticleController;
import com.webserver.controller.ToolsController;
import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * @author shkstart
 * @create 2022-04-12 10:33
 * <p>
 * 用于处理请求
 */
public class DispatcherServlet {
    private static File root;
    private static File staticDir;

    static {
        try {
            root = new File(DispatcherServlet.class.getClassLoader().getResource(".").toURI());
            staticDir = new File(root, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();//path: /myweb/index.html

        System.out.println("======================>" + path);

        //首先判断该请求是否为请求一个业务
        /*
            1.扫描controller包下所有的类，利用反射机制加载，并判断是否被@Controller标注了
            2.如果被@Controller标注了，则获取该类中所有本类中定义的方法，并判断是否被
              @RequestMapping标注了
            3.如果被@RequestMapping标注了，则获取该方法上这个注解中指定的参数value,这个参数
              表示的就是该方法处理的是哪个请求。因此得到该参数后判断是否为path的值，如果是，则
              说明该方法就是处理本次请求的业务方法了，从而调用该方法即可

            如果扫描了所有的Controller以及里面所有的业务方法都没有与path匹配的，则说明本次
            请求不是处理业务，则执行下面原有的响应文件或404的操作
         */
        try {
            File dir = new File(
                    DispatcherServlet.class.getClassLoader().
                            getResource("./com/webserver/controller").toURI()
            );
            File[] subs = dir.listFiles(f -> f.getName().endsWith(".class"));
            for (File f : subs){
                String className = f.getName().substring(0, f.getName().indexOf("."));
                Class cls = Class.forName("com.webserver.controller."+className);
                //判断该类是否被@Controller标注了
                if (cls.isAnnotationPresent(Controller.class)){
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods){
                        //判断该方法是否被@RequestMapping标注了
                        if (method.isAnnotationPresent(RequestMapping.class)){
                            //获取该注解
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            //获取该注解的参数(该方法处理的请求路径)
                            String value = rm.value();
                            if (path.equals(value)){//判断当前请求是否为该方法处理的请求
                                //实例化该Controller
                                Object obj = cls.newInstance();
                                //执行该方法
                                method.invoke(obj,request,response);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(staticDir, path);
        System.out.println("资源是否存在:" + file.exists());
        if (file.isFile()) {//当file表示的文件真实存在且是一个文件时返回true
            response.setContentFile(file);

        } else {//要么file表示的是一个目录，要么不存在
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "root/404.html");
            response.setContentFile(file);
        }

        //测试添加一个额外的响应头
        response.addHeader("Server", "WebServer");
    }
}
