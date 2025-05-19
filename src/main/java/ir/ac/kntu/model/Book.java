package ir.ac.kntu.model;

public class Book extends Product {

    String title;
    String writerName;
    int pageNumber;
    String genre;
    String ageGroup;
    String ISBN;

    public Book(String name, String title, double price, int inventory, String writerName,
                 int pageNumber, String genre, String ageGroup, String ISBN) {
        super(name, price, inventory);
        this.title = title;
        this.writerName = writerName;
        this.pageNumber = pageNumber;
        this.genre = genre;
        this.ageGroup = ageGroup;
        this.ISBN = ISBN;
    }

    public Book() {}

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

    public String getISBN() {
        return ISBN;
    }


}
