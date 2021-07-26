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
        try {
            HandlerMapping.MethodMapping mm = HandlerMapping.getMethod(path);
            //判断是否为null因为有可能获取的path的value没有值，返回的则是null
            if (mm!=null){
//                mm.getMethod().invoke(mm.getController(),request,response);
                Object controller = mm.getController();
                Method method = mm.getMethod();
                method.invoke(controller,request,response);
                return;
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
