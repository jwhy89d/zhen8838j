package com.webserver.entity;

import java.io.Serializable;

/**
 * @author shkstart
 * @create 2022-04-14 12:13
 */
public class Article implements Serializable {
    public static final long serialVersionUID = 2L;
    private String headline;
    private String content;

    public Article() {
    }

    public Article(String headline, String content) {
        this.headline = headline;
        this.content = content;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
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
                "headline='" + headline + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
