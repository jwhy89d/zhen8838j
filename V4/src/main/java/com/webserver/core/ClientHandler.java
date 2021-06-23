package com.webserver.core;

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
            //1.  解析请求
            //1.1 解析请求行

            String line = readLine();
            System.out.println(line);

            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议版本
//            method = line.substring(0,3);
//            uri = line.substring(3,21);
//            protocol = line.substring(21,30);
            String[] array = line.split("\\s");//按空格拆分
            method = array[0];
            uri = array[1];//这里可能会出现数组下标越界异常!原因:浏览器空请求，后期会解决
            protocol = array[2];
            //http://localhost:8088/myweb/index.html
            System.out.println("method:"+method);    //GET
            System.out.println("uri:"+uri);          // /myweb/index.html
            System.out.println("protocol:"+protocol);//HTTP/1.1

            //1.2 解析消息头
//            while (true){
//                line = readLine();
//                if (line.isEmpty()){
//                    break;
//                }
//            }
            Map<String,String> headers = new HashMap<>();

            while (!(line = readLine()).isEmpty()) {
                System.out.println("消息头:" + line);
                //将消息头按照"冒号空格"拆分为消息头的名字和值并以key,value存入headers
//                String key = line.substring(0,line.indexOf(":"));
//                String value = line.substring(line.indexOf(":")+1);
                array = line.split(":\\s");//按冒号空格拆分
                headers.put(array[0],array[1]);
            }
            System.out.println("headers:"+headers);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 通过socket获取的输入流读取客户端发送过来的一行字符串
     * @return
     */
    private String readLine() throws IOException {
        //读取客户端发过来的消息
        InputStream in = socket.getInputStream();
        char pre = 'a',cur = 'a';//pre上次读取的字符，cur本次读取的字符
        StringBuilder builder = new StringBuilder();
        int d;
        while ((d = in.read())!=-1){
            cur = (char) d;//本次读取到的字符
            if (pre==13&&cur==10){//判断是否连续读取到了回车和换行符
                break;
            }
            builder.append(cur);
            pre = cur;//在进行下次读取字符前将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();
    }
}
