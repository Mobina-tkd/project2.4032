package ir.ac.kntu;

public class Book extends Product {
    
    String writerName;
    int pageNumber;
    String genre;
    String ageGroup;
    String ISBN;

    public Book(String name, double price, int inventory, String writerName, int pageNumber, String genre, String ageGroup, String ISBN) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.writerName = writerName;
        this.pageNumber = pageNumber;
        this.genre = genre;
        this.ageGroup = ageGroup;
        this.ISBN = ISBN;
    }

    public Book() {}

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


    @Override
    public Book readData() {
        System.out.print("Enter name: ");
        String name = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter writer name: ");
        String writerName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter page number: ");
        int pageNumber = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter genre: ");
        String genre = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter age group (ADULT, TEENAGER, CHILDREN): ");
        String ageGroup = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter ISBN: ");
        String ISBN = ScannerWrapper.getInstance().nextLine();

        return new Book(name, price, inventory, writerName, pageNumber, genre, ageGroup, ISBN);
    }
}
