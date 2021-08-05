package com.wisappstudio.hobbing.data;

public class MyPagePostData {
    private String writer;
    private String title;
    private String description;
    private String likes;
    private String views;
    private String shares;
    private String number;
    private String category;
    private String date;

    public MyPagePostData(String number, String writer, String title, String description, String likes, String views, String shares, String category, String date){
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.likes = likes;
        this.views = views;
        this.shares = shares;
        this.number = number;
        this.category = category;
        this.date = date;
    }

    public String getWriter()
    {
        return this.writer;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getLikes() { return this.likes; }

    public String getViews() { return this.views; }

    public String getShares() { return this.views; }

    public String getNumber() { return this.number; }

    public String getCategory() { return category; }

    public String getDate() { return date; }
}
