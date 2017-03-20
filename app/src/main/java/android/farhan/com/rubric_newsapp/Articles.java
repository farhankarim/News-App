package android.farhan.com.rubric_newsapp;

public class Articles {

    private String title;
    private String author;
    private String date;
    private String sectionId;
    private String url;


    public Articles(String date, String author, String title, String sectionId, String url) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.sectionId = sectionId;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }


}