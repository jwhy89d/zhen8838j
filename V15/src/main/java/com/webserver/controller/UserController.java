package com.webserver.controller;

import com.webserver.core.DispatcherServlet;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.URISyntaxException;

/**
 * @author shkstart
 * @create 2022-04-13 16:03
 *
 * 处理与用户相关的业务操作
 */
public class UserController {
    private static File userDir;
    private static File root;
    private static File staticDir;
    static {
        try {
            root = new File(DispatcherServlet.class.getClassLoader().getResource(".").toURI());
            staticDir = new File(root,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        userDir = new File("./users");
        if (!userDir.exists()){//如果要创建的目录不存在
            userDir.mkdirs();
        }
    }
    public void reg(HttpServletRequest request, HttpServletResponse response){
        //1.获取用户注册页面上输入的注册信息，获取form表单提交的内容
        /*
            getParameter传入的值必须和页面表单上对应输入框的名字一致
            即:<input name="username" type="text">
                            ^^^^^^^^
                            以它一致
         */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        int age = Integer.parseInt(ageStr);

        System.out.println(username+","+password+","+nickname+","+ageStr);

        //2.将用户信息保存
        File userFile = new File(userDir,username+".obj");
        try (
                FileOutputStream fos = new FileOutputStream(userFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            User user = new User(username,password,nickname,age);
            oos.writeObject(user);

            //注册成功了
            File file = new File(staticDir,"/myweb/reg_success.html");
            response.setContentFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3.给用户响应一个注册结果页面(注册成功或注册失败)
    }
}
