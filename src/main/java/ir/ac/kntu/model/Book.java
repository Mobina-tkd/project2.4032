package ir.ac.kntu.model;

public class Book extends Product {

    private String title;
    private String writerName;
    private int pageNumber;
    private String genre;
    private String ageGroup;
    private String isbn;

    public Book(String name, String title, double price, int inventory, String writerName,
            int pageNumber, String genre, String ageGroup, String isbn) {
        super(name, price, inventory);
        this.title = title;
        this.writerName = writerName;
        this.pageNumber = pageNumber;
        this.genre = genre;
        this.ageGroup = ageGroup;
        this.isbn = isbn;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public String getWriterName() {
        return writerName;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getGenre() {
        return genre;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getIsbn() {
        return isbn;
    }

}
