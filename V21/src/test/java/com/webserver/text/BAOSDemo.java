package com.webserver.text;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author shkstart
 * @create 2022-04-15 14:14
 *
 * java.io.ByteArrayOutputStream和ByteArrayInputStream
 * 字节数组输出与输入流
 * 它们是一对低级流,内部维护一个字节数组
 *
 * ByteArrayOutputStream通过该流写出的数据都会保存在内部维护的字节数组中
 *
 */
public class BAOSDemo {
    public static void main(String[] args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);
        PrintWriter pw = new PrintWriter(bw,true);
        /*
            ByteArrayOutputStream内部提供的方法:
            Byte[] toByteArray()
            该方法返回的字节数组包含这目前通过这个流写出的所有字节

            int size()
            返回的数字表示已经通过当前流写出了多少字节(在流自行维护的字节数组中实际
            保存了多少字节)
         */
        byte[] data = baos.toByteArray();
        System.out.println("内部数组长度:"+data.length);
        System.out.println("内部数组内容:"+ Arrays.toString(data));
        System.out.println("内部缓存大小:"+baos.size());

        pw.println("helloworld");
        data = baos.toByteArray();
        System.out.println("内部数组长度:"+data.length);
        System.out.println("内部数组内容:"+ Arrays.toString(data));
        System.out.println("内部缓存大小:"+baos.size());

        pw.println("think in java");
        data = baos.toByteArray();
        System.out.println("内部数组长度:"+data.length);
        System.out.println("内部数组内容:"+ Arrays.toString(data));
        System.out.println("内部缓存大小:"+baos.size());

        pw.close();
    }
}
