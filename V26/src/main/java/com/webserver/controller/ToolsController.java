package com.webserver.controller;


import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import qrcode.QRCodeUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


/**
 * @author shkstart
 * @create 2022-04-15 19:53
 */
@Controller
public class ToolsController {
//
//        String message = "http://www.baidu.com";
//
//        try {
//            //参数1:二维码上包含的文本信息 参数2:图片生成的位置
////            QRCodeUtil.encode(message,"./qr.jpg");
//            //参数1:二维码上包含的文本信息 参数2:图片生成后会通过该流写出
////            QRCodeUtil.encode(message,new FileOutputStream("./qr.jpg"));
//            //参数1:二维码上包含的文本信息 参数2:二维码中间logo图片 参数3:图片生成的位置
//            //参数4:是否需要压缩logo图片到中间大小
////            QRCodeUtil.encode(message,"123.jpg","./qr.jpg",true);
//            QRCodeUtil.encode(message,"123.jpg",new FileOutputStream("./qr.jpg"),true);
//
//            System.out.println("二维码生成完毕!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    @RequestMapping("/myweb/random.jpg")
    public void createRandomImage(HttpServletRequest request,HttpServletResponse response){
        //验证码测试
        //1.创建一张空图片，并且指定宽高。理解为:创建一张画纸
        BufferedImage image = new BufferedImage(70, 30, BufferedImage.TYPE_INT_RGB);

        //2.根据图片获取一个画笔,通过该画笔画的内容都会画到该图片上
        Graphics g = image.getGraphics();

        //3.确定验证码内容(字母与数字的组合)
        String line = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();//用于生成随机数(随机数位line的字符下标)

        //为图片背景填充一个随机颜色
        //创建Color时，需要指定三个参数，分别是，红、绿、蓝。数字范围都是(0-255)之间
        Color bgcolor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        //将画笔设置为该颜色
        g.setColor(bgcolor);
        //填充整张图片为画笔当前颜色
        g.fillRect(0, 0, 70, 30);

        //向图片上画4个字符
        for (int i = 0;i<4;i++) {
            //随机生成一个字符
            String str = line.charAt(random.nextInt(line.length())) + "";
            //生成随机颜色
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            //设置画笔颜色
            g.setColor(color);
            //设置字体
            g.setFont(new Font(null, Font.BOLD, 20));
            //将字符串画到图片指定的位置上
            g.drawString(str, i*15+5, 18+random.nextInt(11)-5);
        }

        //随机生成4条干扰线
        for (int i = 0; i < 4; i++) {
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(color);
            g.drawLine(random.nextInt(71),random.nextInt(31),
                    random.nextInt(71),random.nextInt(31));
        }
        //将图片写入文件来生成该图片文件
        try {
            ImageIO.write(image, "jpg",response.getOutputStream());
            response.setContentType("image/jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/myweb/createQR")
    public void createQr(HttpServletRequest request, HttpServletResponse response) {
        String content = request.getParameter("content");
        try {
            QRCodeUtil.encode(content, "1234.jpg", response.getOutputStream(), true);
            response.setContentType("image/jpeg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        //验证码测试
        //1.创建一张空图片，并且指定宽高。理解为:创建一张画纸
        BufferedImage image = new BufferedImage(70, 30, BufferedImage.TYPE_INT_RGB);

        //2.根据图片获取一个画笔,通过该画笔画的内容都会画到该图片上
        Graphics g = image.getGraphics();

        //3.确定验证码内容(字母与数字的组合)
        String line = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();//用于生成随机数(随机数位line的字符下标)

        //为图片背景填充一个随机颜色
        //创建Color时，需要指定三个参数，分别是，红、绿、蓝。数字范围都是(0-255)之间
        Color bgcolor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
        //将画笔设置为该颜色
        g.setColor(bgcolor);
        //填充整张图片为画笔当前颜色
        g.fillRect(0, 0, 70, 30);

        //向图片上画4个字符
        for (int i = 0;i<4;i++) {
            //随机生成一个字符
            String str = line.charAt(random.nextInt(line.length())) + "";
            //生成随机颜色
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
            //设置画笔颜色
            g.setColor(color);
            //设置字体
            g.setFont(new Font(null, Font.BOLD, 20));
            //将字符串画到图片指定的位置上
            g.drawString(str, i*15+5, 18+random.nextInt(11)-5);
        }

        //随机生成4条干扰线
        for (int i = 0; i < 4; i++) {
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(color);
            g.drawLine(random.nextInt(71),random.nextInt(31),
                    random.nextInt(71),random.nextInt(31));
        }
        //将图片写入文件来生成该图片文件
        try {
            ImageIO.write(image, "jpg", new FileOutputStream("./random.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
