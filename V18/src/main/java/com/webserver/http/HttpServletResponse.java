package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author shkstart
 * @create 2022-04-12 9:07
 *
 * 响应对象
 * 该类的每一个实例用于表示HTTP协议要求的响应
 * 每个响应由三部分组成:
 * 状态行，响应头，响应正文
 */
public class HttpServletResponse {
    private Socket socket;
    //状态行相关信息
    private int statusCode = 200;//状态代码
    private String statusReason = "OK";//状态描述

    //响应头相关信息
    private Map<String,String> headers = new HashMap<>();
    //响应正文相关信息
    private File contentFile;
    public HttpServletResponse(Socket socket){
        this.socket = socket;
    }

    /**
     * 将当前响应对象内容按照标准的HTTP响应格式发送客户端
     * @throws IOException
     */
    public void response() throws IOException {
        //3.1发送状态行
        sendStatusLine();
        //3.2发送响应头
        sendHeaders();
        //3.3发送响应正文(index.html页面的数据)发送给浏览器
        sendContent();
    }

    /**
     * 发送状态行
     */
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }

    /**
     * 发送响应头
     */
    private void sendHeaders() throws IOException {
//        println("Content-Type: text/html");
//        println("Content-Length:"+contentFile.length());
        Set<Map.Entry<String, String>> entry = headers.entrySet();
        for (Map.Entry<String,String> e : entry){
            String key = e.getKey();
            String value = e.getValue();
            println(key+": "+value);
        }
        println("");//单独发送回车换行表示响应头发送完毕了
    }

    /**
     * 发送响应正文
     */
    private void sendContent() throws IOException {
        if (contentFile != null) {
            //负责给浏览器发送
            OutputStream out = socket.getOutputStream();
            try(
                    FileInputStream fis = new FileInputStream(contentFile);
            ){
                //获取文件的字节
                int len;
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            }
        }
    }
    /**
     * 将一行字符串按照iso8859-1编码发送到客户端,最后会自动添加回车和换行符
     * @param line
     */
    private void println(String line) throws IOException {
        //获取输出流
        OutputStream out = socket.getOutputStream();
        //创建byte数组
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);//单独发送了回车符
        out.write(10);//单独发送了换行符
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
        String contentType = null;
        try {
            //Files.probeContentType 返回正文文件的类型:index.html(text/html)
            contentType = Files.probeContentType(contentFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
            当我们无法根据正文文件确定类型时，则不发送Content-Type
            在HTTP协议中当响应包含正文，但是不包含Content-Type时，则表示让浏览器
            自行判断响应正文内容
         */

        if (contentType != null){
            addHeader("Content-Type",contentType);
        }
        addHeader("Content-Length",contentFile.length()+"");
    }

    /**
     * 添加一个要发送的响应头
     * @param name
     * @param value
     */
    public void addHeader(String name,String value){
        //将响应头的名字和值以key-value形式存入headers这个Map中
        headers.put(name,value);
    }
}
