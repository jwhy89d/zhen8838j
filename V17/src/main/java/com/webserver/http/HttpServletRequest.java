package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author shkstart
 * @create 2022-04-08 17:12
 *
 * HTTP请求对象
 * 该类的每一个实例用于表示一个HTTP请求内容
 * 每个请求由三部分构成:
 * 请求行,消息头,消息正文
 */
public class HttpServletRequest {
    private Socket socket;
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    //保存uri中"?"左侧的内容。如果uri中没有"?"则内容与uri一致
    private String requestURI;

    //保存uri中"?"右侧内容，即:参数部分
    private String queryString;

    //用来保存客户端传递过来的每一组参数
    private Map<String,String> parameters = new HashMap<>();


    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();
    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        //1.1 解析请求行
        parseRequestLine();
        //1.2 解析消息头
        parseHeaders();
        //1.3 解析消息正文
        parseContent();
    }

    /**
     * 解析请求头
     * parse:解析
     */
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();

        if (line.isEmpty()){//如果请求行为空字符串,则说明为空请求
            throw new EmptyRequestException();
        }
        System.out.println(line);

//            method = line.substring(0,3);
//            uri = line.substring(3,21);
//            protocol = line.substring(21,30);
        String[] array = line.split("\\s");//按空格拆分
        method = array[0];
        uri = array[1];//这里可能会出现数组下标越界异常!原因:浏览器空请求，后期会解决
        protocol = array[2];
        //进一步解析uri
        parseUri();
        //http://localhost:8088/myweb/index.html
        System.out.println("method:"+method);    //GET
        System.out.println("uri:"+uri);          // /myweb/index.html
        System.out.println("protocol:"+protocol);//HTTP/1.1
    }

    /**
     * 进一步解析uri
     */
    private void parseUri(){
        /*
            uri是有两种情况的，1:不含有参数的 2:含有参数的
            例如:
            不含有参数的:/myweb/reg.html
            含有参数的:/myweb/reg?username=fanchuanqi&password=123456&nickname=chuanqi&age=22

            处理方式:
            1:若不含有参数，则直接将uri的值赋值给requestURI
            2:若含有参数
              2.1:先将uri按照"?"拆分为请求部分和参数部分
                  将请求部分赋值给requestURI
                  将参数部分赋值给queryString
              2.2:再将参数部分按照"&"拆分出每一组参数
                  每组参数再按照"="拆分为参数名和参数值
                  并将参数名作为key，参数值作为value保存到parameters这个Map中

              允许页面输入框空着，这种情况该参数的值为null存入parameters即可
         */

            String[] data = uri.split("\\?");
            requestURI = data[0];
            if (data.length > 1){//说明?后面有参数部分
                //username=wzg168&password=4324&nickname=34&age=46
                queryString = data[1];
                parseParameter(queryString);
            }

        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
    }

    /**
     * 解析消息头
     */
    private void parseHeaders() throws IOException {
//                    while (true){
//                line = readLine();
//                if (line.isEmpty()){
//                    break;
//                }
//            }
        String line;
        while (!(line = readLine()).isEmpty()) {
            System.out.println("消息头:" + line);
            //将消息头按照"冒号空格"拆分为消息头的名字和值并以key,value存入headers
//                String key = line.substring(0,line.indexOf(":"));
//                String value = line.substring(line.indexOf(":")+1);
            String[] array = line.split(":\\s");//按冒号空格拆分
            headers.put(array[0].toLowerCase(),array[1]);
        }
        System.out.println("headers:"+headers);
    }

    /**
     * 解析消息正文
     */
    private void parseContent() throws IOException {
        //当一个请求的请求方式为POST时,则说明应当会包含消息正文
        System.out.println(">>>>>>>>1:"+method);
        if ("post".equalsIgnoreCase(method)){
            System.out.println(">>>>>>>>2:"+method);
            //消息头中是否含有Content-Length;
            // containsKey():表示Map中的是否包含这个key
            if (headers.containsKey("content-length")){
                int contentLength = Integer.parseInt(
                        headers.get("content-length")
                );
                System.out.println(">>>>>>>>>>>>>"+contentLength);
                //根据Content-Length指定的长度创建字节 数组
                byte[] data = new byte[contentLength];
                InputStream in = socket.getInputStream();
                //将正文内容块读进data数组中备用，
                // 因为解析消息头，解析请求行已经读取到了，再接着读取的就是消息正文
                in.read(data);

                //根据消息头Content-Type判断正文类型并进行对应的解析
                if (headers.containsKey("content-type")){
                    String contentType = headers.get("content-type");
                    System.out.println(">>>>>>>>>>"+contentType);
                    //如果正文类型为form表单提交数据
                    if ("application/x-www-form-urlencoded".equals(contentType)){
                        String line = new String(data, StandardCharsets.ISO_8859_1);
                        System.out.println("正文内容========："+line);
                        parseParameter(line);
                    }
//                    else if ("xxx/xxx".equals(contentType)){}//后期扩展
                }
            }
        }
    }

    /**
     * 解析参数。参数的格式应当是:name1=value1&name2=value2&...
     * 将每一组参数的参数名作为key,参数值作为value存入parameters中
     * @param line
     */
    private void parseParameter(String line){
        //进一步拆分参数部分
        //[username=xxxx, password=xxxx, nickname=xxxx, age=xxxx]
        String[] data = line.split("&");
        //遍历数组,在进一步拆分每一个参数
        for (String para : data){
            //[username, xxxx]
            String[] paras = para.split("=");
            parameters.put(paras[0],paras.length>1?paras[1]:null);
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

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /*
        根据消息头的名字获取对应消息头的值
     */
    public String getHeader(String name){
        return headers.get(name.toLowerCase());
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    /**
     * 根据参数名获取对应的参数值
     * @param name
     * @return
     */
    public String getParameter(String name){
        return parameters.get(name);
    }
}
