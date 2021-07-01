package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2022-04-07 15:42
 *
 * 该线程任务负责与指定客户端完成HTTP交互
 * 与客户端交流的流程分成三步：
 * 1:解析请求
 * 2:处理请求
 * 3:发送响应
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            //读取客户端发过来的消息
            //1.解析请求
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);
            //2.处理请求
            DispatcherServlet servlet = new DispatcherServlet();
            servlet.service(request,response);
            //3.发送响应
            response.response();

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            //HTTP协议要求，响应完客户端后就要断开TCP连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
