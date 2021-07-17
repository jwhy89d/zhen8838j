package com.webserver.controller;

import com.webserver.core.DispatcherServlet;
import com.webserver.entity.Article;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

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
     *
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
        if (artFile.exists()){
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
}
