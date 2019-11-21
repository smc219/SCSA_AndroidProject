package edu.scsa.android.androidproject_seokminchang;

public class News {

    private String newsTitle;
    private String newsDesc;
    private String newsLink;


    public News() {
    }

    public News(String newsTitle, String newsDesc, String newsLink) {
        this.newsTitle = newsTitle;
        this.newsDesc = newsDesc;
        this.newsLink = newsLink;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }
}
