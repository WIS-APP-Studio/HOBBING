package com.wisappstudio.hobbing.data;

public class CommentData {
    private String postNumber;
    private String commentNumber;
    private String writer;
    private String description;
    private String date;

    public CommentData(String postNumber, String  commentNumber, String writer, String description, String date) {
        this.postNumber = postNumber;
        this.commentNumber = commentNumber;
        this.writer = writer;
        this.description = description;
        this.date = date;
    }

    public String getWriter() {
        return writer;
    }

    public String getDescription() {
        return description;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public String getDate() {
        return date;
    }

    public String getPostNumber() {
        return postNumber;
    }
}
