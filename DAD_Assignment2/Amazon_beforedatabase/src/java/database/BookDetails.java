package database;
public class BookDetails implements Comparable {
    private String id=null;         //id            varchar(4)
    private String title=null;      //title         varchar(50)
    private String author=null;     //author        varchar(32)
    private float price=0.0F;       //price         float
    private String currency=null;
    private int year=0;             //year          int
    private String description=null;//description   varchar(128)
    private int inventory=0;        //inventory     int
    public BookDetails() {}
    public BookDetails(String id, String title, String author,
        float price, String currency, int year,String description, int inventory) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.currency = currency;
        this.year = year;
        this.description = description;
        this.inventory = inventory;
    }
    public String getId() {return id;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public float getPrice() {return price;}
    public int getYear() {return year;}
    public String getCurrency() {return currency;}
    public String getDescription() {return description;}
    public int getInventory() {return inventory;}

    public void setId(String id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setAuthor(String author) {this.author = author; }
    public void setPrice(float price) {this.price = price; }
    public void setCurrency(String currency) {this.currency = currency; }
    public void setYear(int year) {this.year = year;}
    public void setDescription(String description) {this.description = description; }
    public void setInventory(int inventory) {this.inventory = inventory;}
    public int compareTo(Object o) {
        return title.compareTo(((BookDetails) o).title);
    }
}
