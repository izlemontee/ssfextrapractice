package sg.nus.vttp.ssfworkshoptrial.model;

public class Article {

    private String title;
    private String author;
    private String image;
    private String description;
    private String timestamp;
    private String url;

    public Article(String title, String author, String image, String description, String timestamp, String url) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.description = description;
        this.timestamp = timestamp;
        this.url = url;
    }


    public Article(){

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
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    
    
}
