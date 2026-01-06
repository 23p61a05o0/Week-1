import java.util.*;
import java.time.LocalDate;

// Custom Exception for Business Logic
class BookUnavailableException extends Exception {
    public BookUnavailableException(String message) {
        super(message);
    }
}

// Interface defining behavior
interface Searchable {
    void displayDetails();
}

// Base class demonstrating Encapsulation
abstract class LibraryItem implements Searchable {
    private String id;
    private String title;
    private boolean isAvailable;

    public LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.isAvailable = true;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}

// Subclass demonstrating Inheritance
class Book extends LibraryItem {
    private String author;
    private int pageCount;

    public Book(String id, String title, String author, int pageCount) {
        super(id, title);
        this.author = author;
        this.pageCount = pageCount;
    }

    @Override
    public void displayDetails() {
        System.out.println("[Book] ID: " + getId() + " | Title: " + getTitle() + 
                           " | Author: " + author + " | Pages: " + pageCount);
    }
}

// Manager Class to handle Logic and Collections
class LibraryManager {
    private List<LibraryItem> inventory = new ArrayList<>();
    private Map<String, String> borrowedBooks = new HashMap<>(); // ID to Member mapping

    public void addItem(LibraryItem item) {
        inventory.add(item);
    }

    public void checkoutBook(String bookId, String memberName) throws BookUnavailableException {
        for (LibraryItem item : inventory) {
            if (item.getId().equals(bookId)) {
                if (!item.isAvailable()) {
                    throw new BookUnavailableException("Error: Book '" + item.getTitle() + "' is already checked out.");
                }
                item.setAvailable(false);
                borrowedBooks.put(bookId, memberName);
                System.out.println("Success: " + memberName + " has checked out " + item.getTitle());
                return;
            }
        }
        System.out.println("Error: Book ID " + bookId + " not found.");
    }

    public void returnBook(String bookId) {
        for (LibraryItem item : inventory) {
            if (item.getId().equals(bookId)) {
                item.setAvailable(true);
                borrowedBooks.remove(bookId);
                System.out.println("Success: '" + item.getTitle() + "' has been returned.");
                return;
            }
        }
    }

    public void showInventory() {
        System.out.println("\n--- Current Library Inventory ---");
        for (LibraryItem item : inventory) {
            String status = item.isAvailable() ? "[Available]" : "[Checked Out]";
            System.out.print(status + " ");
            item.displayDetails();
        }
        System.out.println("----------------------------------\n");
    }
}

// Main Class to execute the program
public class LibrarySystem {
    public static void main(String[] args) {
        LibraryManager myLibrary = new LibraryManager();

        // 1. Setup Inventory
        myLibrary.addItem(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", 180));
        myLibrary.addItem(new Book("B002", "Effective Java", "Joshua Bloch", 412));
        myLibrary.addItem(new Book("B003", "Clean Code", "Robert C. Martin", 464));

        myLibrary.showInventory();

        // 2. Simulate User Interactions
        try {
            System.out.println("Action: Checking out B002...");
            myLibrary.checkoutBook("B002", "John Doe");

            System.out.println("Action: Attempting to check out B002 again...");
            myLibrary.checkoutBook("B002", "Jane Smith"); // This will trigger the exception

        } catch (BookUnavailableException e) {
            System.err.println("Exception Caught: " + e.getMessage());
        }

        // 3. Final State
        myLibrary.returnBook("B002");
        myLibrary.showInventory();
        
        System.out.println("System Date: " + LocalDate.now());
        System.out.println("Library System Shutdown.");
    }
}
