package edu.scsa.android.moviemgr;

public class Movie {
    private String mTitle;
    private String mContent;
    private int mImg;

    public Movie(String mTitle, String mContent, int mImg) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mImg = mImg;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public int getmImg() {
        return mImg;
    }

    public void setmImg(int mImg) {
        this.mImg = mImg;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mTitle='" + mTitle + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mImg=" + mImg +
                '}';
    }
}
