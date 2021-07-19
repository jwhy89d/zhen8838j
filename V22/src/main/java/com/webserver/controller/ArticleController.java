package com.webserver.controller;

import com.webserver.core.DispatcherServlet;
import com.webserver.entity.Article;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shkstart
 * @create 2022-04-14 12:12
 */
public class ArticleController {
    private static File articlesDir;//存放所有文章的目录
    private static File root;
    private static File staticDir;
    static {
        try {
            root = new File(DispatcherServlet.class.getClassLoader().getResource(".").toURI());
            staticDir = new File(root,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        articlesDir = new File("./articles");
        if (!articlesDir.exists()){//如果要创建的目录不存在
            articlesDir.mkdirs();
        }
    }

    /**
     * 处理与文章相关的业务
     * @param request
     * @param response
     */
    public void writeArticle(HttpServletRequest request, HttpServletResponse response){
        //1.获取表单数据
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        System.out.println(title+","+author+","+content);

        if (title == null || content == null || author == null){
            File file = new File(staticDir,"/myweb/article_fail.html");
            response.setContentFile(file);
            return;
        }
        //2.保存文章
        File artFile = new File(articlesDir,title+".obj");
        if (artFile.exists()){//文件存在则说明是重复文章
            File file = new File(staticDir,"/myweb/article_fail.html");
            response.setContentFile(file);
            return;
        }
        try (
                FileOutputStream fos = new FileOutputStream(artFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            Article article = new Article(title,author,content);
            oos.writeObject(article);

            File file = new File(staticDir,"/myweb/article_success.html");
            response.setContentFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于显示文章列表的动态页面
     * @param request
     * @param response
     */
    public void showAllArticle(HttpServletRequest request,HttpServletResponse response){
        List<Article> articleList = new ArrayList<>();
        File[] files = articlesDir.listFiles(f -> f.getName().endsWith(".obj"));
        for (File f : files){
            try (
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                Article art = (Article) ois.readObject();
                articleList.add(art);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PrintWriter pw = response.getWriter();
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
        pw.println("<meta charset=\"UTF-8\">");
        pw.println("<title>文章列表</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<center>");
        pw.println("<h1>文章列表</h1>");
        pw.println("<table border=\"1\">");
        pw.println("<tr>");
        pw.println("<td align=\"center\">标题</td>");
        pw.println("<td>作者</td>");
        pw.println("</tr>");
        for (Article a : articleList) {
            pw.println("<tr>");
            pw.println("<td>"+a.getTitle()+"</td>");
            pw.println("<td>"+a.getAuthor()+"</td>");
            pw.println("</tr>");
        }
        pw.println("</table>");
        pw.println("</center>");
        pw.println("</body>");
        pw.println("</html>");

        System.out.println("页面生成完毕!");
        response.setContentType("text/html");
    }
}
