package com.webserver.entity;

import java.io.Serializable;

/**
 * @author shkstart
 * @create 2022-04-14 12:13
 */
public class Article implements Serializable {
    public static final long serialVersionUID = 2L;
    private String title;//标题
    private String author;//作者
    private String content;

    public Article() {
    }

    public Article(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getHeadline() {
        return title;
    }

    public void setHeadline(String headline) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "headline='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
