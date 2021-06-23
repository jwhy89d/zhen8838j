package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
            //2.处理请求

        }catch (IOException e){
            e.printStackTrace();
        }

    }


}
