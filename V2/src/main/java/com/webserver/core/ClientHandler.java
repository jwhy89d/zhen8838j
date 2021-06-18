package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author shkstart
 * @create 2022-04-07 15:42
 *
 * 该线程任务负责与指定客户端完成HTTP交互
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
            InputStream in = socket.getInputStream();
            int d;
            while ((d = in.read())!= -1){
                System.out.print((char) d);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
