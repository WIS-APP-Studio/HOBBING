package com.wisappstudio.hobbing.data;

public class PostData {
    private String writer;
    private String title;
    private String description;
    private String likes;
    private String views;
    private String shares;

    public PostData(/*int image,*/String writer, String title, String description, String likes, String views, String shares){
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.likes = likes;
        this.views = views;
        this.shares = shares;
    }

//    public int getImage()
//    {
//        return this.image;
//    }

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
}