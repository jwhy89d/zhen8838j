package com.webserver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author shkstart
 * @create 2022-04-14 17:07
 */
public class TestDecode {
    public static void main(String[] args) {
        String line = "/myweb/login?username=%E5%91%A8%E8%B4%A4%E5%88%A9&password=123456";
        try {
            line = URLDecoder.decode(line, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(line);

    }

}
