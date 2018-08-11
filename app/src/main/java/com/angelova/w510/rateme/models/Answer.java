package com.angelova.w510.rateme.models;

import java.io.Serializable;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class Answer implements Serializable {
    private String author;
    private String content;

    public Answer() {}

    public Answer (String author, String content) {
        this.author = author;
        this.content = content;
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
}
