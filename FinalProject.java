import java.io.*;
import java.util.*;

/**
 * Final Project: Smart Library Management System
 * @author Muhammad Gasimov, Fuad Nasirli, Farhad Zamanov and Elmira Karimova
 * * This system combines all 10 labs from the Big Data Processing Methods course 
 * into a single functional library backend.
 */

// Lab 10: Abstract base class, implementing Serializable so we don't lose data on exit
abstract class LibraryItem implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String itemID;
    protected String title;
    protected boolean isAvailable;

    // Lab 2: Overloading constructors just in case we don't have full book details yet
    public LibraryItem() {
        this.itemID = "UNKNOWN";
        this.title = "Untitled";
        this.isAvailable = true;
    }

    public LibraryItem(String itemID, String title) {
        this.itemID = itemID;
        this.title = title;
        this.isAvailable = true;
    }

    public LibraryItem(String itemID, String title, boolean isAvailable) {
        this.itemID = itemID;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    // Abstract methods that all child classes must implement (Lab 10 requirement)
    public abstract String getType();
    public abstract String getSummary();

    public String getItemID() { return itemID; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean v) { this.isAvailable = v; }

    // Labs 1 & 5: Overriding toString, equals, and hashCode for proper object comparison
    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Checked Out";
        return String.format("[%s] ID:%s | %s (%s)", getType(), itemID, title, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LibraryItem other = (LibraryItem) obj;
        return Objects.equals(itemID, other.itemID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID);
    }
}

// Lab 10 & Lab 1: Concrete subclasses for different media types
class Book extends LibraryItem {
    private String author;
    private int year;
    private String genre;

    // Lab 2: More constructor overloading for Books
    public Book(String itemID, String title) {
        super(itemID, title);
        this.author = "Unknown";
        this.year = 0;
        this.genre = "General";
    }

    public Book(String itemID, String title, String author) {
        super(itemID, title);
        this.author = author;
        this.year = 0;
        this.genre = "General";
    }

    public Book(String itemID, String title, String author, int year, String genre) {
        super(itemID, title);
        this.author = author;
        this.year = year;
        this.genre = genre;
    }

    @Override 
    public String getType() { return "Book"; }
    
    @Override 
    public String getSummary() {
        return title + " by " + author + " (" + year + ") [" + genre + "]";
    }

    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }
}

class Magazine extends LibraryItem {
    private String publisher;
    private int issueNumber;

    public Magazine(String itemID, String title, String publisher, int issueNumber) {
        super(itemID, title);
        this.publisher = publisher;
        this.issueNumber = issueNumber;
    }

    @Override 
    public String getType() { return "Magazine"; }
    
    @Override 
    public String getSummary() {
        return title + " | Issue #" + issueNumber + " | Published by: " + publisher;
    }
}

class EBook extends LibraryItem {
    private String format; // e.g., PDF, EPUB
    private double fileSizeMB;

    public EBook(String itemID, String title, String format, double fileSizeMB) {
        super(itemID, title);
        this.format = format;
        this.fileSizeMB = fileSizeMB;
    }

    @Override 
    public String getType() { return "EBook"; }
    
    @Override 
    public String getSummary() {
        return title + " [" + format + ", " + fileSizeMB + " MB]";
    }
}

// Lab 9 & 1: UML Member hierarchy implementation
class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String memberID;
    protected String name;
    protected List<String> borrowedItemIDs;

    // Lab 2: Constructor overloading
    public Member() {
        this.memberID = "M000";
        this.name = "Unknown";
        this.borrowedItemIDs = new ArrayList<>();
    }

    public Member(String memberID, String name) {
        this.memberID = memberID;
        this.name = name;
        this.borrowedItemIDs = new ArrayList<>();
    }

    public void borrowItem(String itemID) { borrowedItemIDs.add(itemID); }
    public void returnItem(String itemID) { borrowedItemIDs.remove(itemID); }

    public String getMemberID() { return memberID; }
    public String getName() { return name; }
    public List<String> getBorrowedItemIDs() { return borrowedItemIDs; }

    // Labs 1 & 5 methods
    @Override
    public String toString() {
        return name + " (ID: " + memberID + ") - Items borrowed: " + borrowedItemIDs.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member other = (Member) obj;
        return Objects.equals(memberID, other.memberID);
    }

    @Override
    public int hashCode() { return Objects.hash(memberID); }
}

// Labs 1 & 9: Inheritance and Polymorphism - Librarian gets special privileges
class Librarian extends Member {
    private String employeeID;
    private String section;

    public Librarian(String memberID, String name, String employeeID, String section) {
        super(memberID, name);
        this.employeeID = employeeID;
        this.section = section;
    }

    public void addToInventory(LibraryItem item) {
        System.out.println("  [System] " + name + " added new item: " + item.getSummary());
    }

    public void removeFromInventory(String itemID) {
        System.out.println("  [System] " + name + " removed item ID: " + itemID);
    }

    @Override
    public String toString() {
        return "Librarian: " + name + " [EmpID: " + employeeID + ", Section: " + section + "]";
    }
}

// Lab 8: Generics class for handling specific catalog types safely
class Catalog<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> items;
    private String catalogName;

    public Catalog(String catalogName) {
        this.catalogName = catalogName;
        this.items = new ArrayList<>();
    }

    public void add(T item) { items.add(item); }
    public boolean remove(T item) { return items.remove(item); }
    public List<T> getAll() { return Collections.unmodifiableList(items); }
    public int size() { return items.size(); }

    // Lab 8 exact requirement: Generic static method for symmetric difference
    public static <T> Set<T> symmetricDifference(T[] group1, T[] group2) {
        Set<T> set1 = new HashSet<>(Arrays.asList(group1));
        Set<T> set2 = new HashSet<>(Arrays.asList(group2));
        Set<T> result = new HashSet<>();
        
        for (T item : set1) {
            if (!set2.contains(item)) result.add(item);
        }
        for (T item : set2) {
            if (!set1.contains(item)) result.add(item);
        }
        return result;
    }

    @Override
    public String toString() {
        return catalogName + " Catalog (" + items.size() + " items)";
    }
}

// Lab 6: String and Sentence Analysis
class TextAnalyzer {
    // Checking both English and Cyrillic vowels just in case
    private static final String VOWELS = "aeiouAEIOUаеёиоуыэюяАЕЁИОУЫЭЮЯ";

    public static void analyzeText(String text) {
        System.out.println("\n--- [Lab 6] Promo Text Analysis ---");
        // Split by punctuation to isolate sentences
        String[] sentences = text.split("(?<=[.!?])\\s*");
        for (int i = 0; i < sentences.length; i++) {
            analyzeSentence(sentences[i].trim(), i + 1);
        }
    }

    private static void analyzeSentence(String sentence, int idx) {
        int vowels = 0, consonants = 0;
        
        for (char c : sentence.toCharArray()) {
            if (Character.isLetter(c)) {
                if (VOWELS.indexOf(c) >= 0) vowels++;
                else consonants++;
            }
        }
        
        String verdict = "Equal";
        if (vowels > consonants) verdict = "Vowel-heavy";
        else if (consonants > vowels) verdict = "Consonant-heavy";
        
        System.out.printf("  Sentence %d: \"%s\"%n", idx, sentence);
        System.out.printf("  Stats: %d vowels, %d consonants -> %s%n", vowels, consonants, verdict);
        System.out.println("  --------------------------------------------------");
    }
}

// Lab 3: 1D Array processing (applying it to book ratings)
class ArrayStats {
    public static void processRatings(double[] ratings) {
        System.out.println("\n--- [Lab 3] Book Rating Analytics ---");
        double productOfNegatives = 1.0;
        double sumOfPositives = 0.0;
        boolean hasNegatives = false;

        for (double v : ratings) {
            if (v < 0) { 
                productOfNegatives *= v; 
                hasNegatives = true; 
            } else if (v > 0) { 
                sumOfPositives += v; 
            }
        }

        System.out.println("  Raw Ratings Array: " + Arrays.toString(ratings));
        if (hasNegatives) {
            System.out.printf("  Penalty multiplier (Product of negatives): %.2f%n", productOfNegatives);
        } else {
            System.out.println("  No negative ratings recorded.");
        }
        System.out.printf("  Total positive score (Sum of positives): %.2f%n", sumOfPositives);
    }
}

// Lab 4: Variant 20 logic applied to a 5x5 grid (Library shelf occupancy)
class ShelfMatrix {
    private static final int SIZE = 5;
    private final double[][] grid;

    public ShelfMatrix(double[][] grid) { 
        this.grid = grid; 
    }

    public void display() {
        System.out.println("\n--- [Lab 4] Shelf Load Grid (5x5) ---");
        System.out.println("       Col0    Col1    Col2    Col3    Col4");
        for (int i = 0; i < SIZE; i++) {
            System.out.printf("  Row%d ", i);
            for (int j = 0; j < SIZE; j++) {
                System.out.printf("%7.1f", grid[i][j]);
            }
            System.out.println();
        }
    }

    public double[] calculateX() {
        System.out.println("\n  Running Variant 20 extraction logic...");
        double maxVal = grid[0][0];
        double absMaxVal = Math.abs(grid[0][0]);
        int maxCol = 0, absMaxCol = 0;

        // Find the regular max and absolute max
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] > maxVal) { 
                    maxVal = grid[i][j]; 
                    maxCol = j; 
                }
                if (Math.abs(grid[i][j]) > absMaxVal) { 
                    absMaxVal = Math.abs(grid[i][j]); 
                    absMaxCol = j; 
                }
            }
        }

        double[] x = new double[SIZE];
        if (maxCol == absMaxCol) {
            // Same column -> extract column
            for (int i = 0; i < SIZE; i++) x[i] = grid[i][maxCol];
            System.out.println("  Result: Max and |Max| are both in column " + maxCol + ". Extracting column.");
        } else {
            // Different columns -> extract anti-diagonal
            for (int i = 0; i < SIZE; i++) x[i] = grid[i][SIZE - 1 - i];
            System.out.println("  Result: Max is in col " + maxCol + ", but |Max| is in col " + absMaxCol + ". Extracting anti-diagonal.");
        }
        
        System.out.println("  Extracted Vector X: " + Arrays.toString(x));
        return x;
    }
}

// Lab 7 & 9: The main system controller
class LibrarySystem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String libraryName;
    private String location;
    
    // Lab 7: HashMap for fast O(1) lookups instead of slow array iterations
    private HashMap<String, LibraryItem> itemRegistry;
    private HashMap<String, Member> memberRegistry;
    
    // Lab 8: Using our generic class
    private Catalog<Book> bookCatalog;
    private Catalog<Magazine> magazineCatalog;

    public LibrarySystem(String name, String location) {
        this.libraryName = name;
        this.location = location;
        this.itemRegistry = new HashMap<>();
        this.memberRegistry = new HashMap<>();
        this.bookCatalog = new Catalog<>("Books");
        this.magazineCatalog = new Catalog<>("Magazines");
    }

    public void addItem(LibraryItem item) {
        itemRegistry.put(item.getItemID(), item);
        if (item instanceof Book) bookCatalog.add((Book) item);
        else if (item instanceof Magazine) magazineCatalog.add((Magazine) item);
        System.out.println("  [Inventory] Added: " + item.getTitle());
    }

    public void addMember(Member member) {
        memberRegistry.put(member.getMemberID(), member);
        System.out.println("  [Registry] New member registered: " + member.getName());
    }

    public boolean borrowItem(String memberID, String itemID) {
        Member m = memberRegistry.get(memberID);
        LibraryItem i = itemRegistry.get(itemID);
        
        if (m == null || i == null || !i.isAvailable()) {
            System.out.println("  [Error] Borrow failed. Check if ID is correct or if item is already taken.");
            return false;
        }
        
        i.setAvailable(false);
        m.borrowItem(itemID);
        System.out.println("  [Success] " + m.getName() + " checked out '" + i.getTitle() + "'");
        return true;
    }

    public boolean returnItem(String memberID, String itemID) {
        Member m = memberRegistry.get(memberID);
        LibraryItem i = itemRegistry.get(itemID);
        
        if (m == null || i == null) {
            System.out.println("  [Error] Return failed. Invalid member or item ID.");
            return false;
        }
        
        i.setAvailable(true);
        m.returnItem(itemID);
        System.out.println("  [Success] " + m.getName() + " returned '" + i.getTitle() + "'");
        return true;
    }

    // Lab 7 part 2: Letter frequency analysis using HashMaps
    public void analyzeNameFrequency() {
        System.out.println("\n--- [Lab 7] Library Name Letter Frequency ---");
        HashMap<Character, Integer> freq = new HashMap<>();
        
        for (char c : libraryName.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }
        }
        
        System.out.println("  Target Text: \"" + libraryName + "\"");
        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            System.out.println("  '" + e.getKey() + "' appears " + e.getValue() + " times");
        }
    }

    // Lab 10: Saving system state to a binary file
    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("  [Save] System state backed up to " + fileName);
        } catch (IOException e) {
            System.out.println("  [Save Error] " + e.getMessage());
        }
    }

    // Lab 10: Loading state back into memory
    public static LibrarySystem loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            System.out.println("  [Load] Restoring system from " + fileName + "...");
            return (LibrarySystem) ois.readObject();
        } catch (Exception e) {
            System.out.println("  [Load Error] Could not load file: " + e.getMessage());
            return null;
        }
    }

    public void printAllItems() {
        System.out.println("\n  >> Current Inventory for " + libraryName + " <<");
        for (LibraryItem item : itemRegistry.values()) {
            System.out.println("  " + item);
        }
    }

    public void printAllMembers() {
        System.out.println("\n  >> Active Members <<");
        for (Member m : memberRegistry.values()) {
            System.out.println("  " + m);
        }
    }

    @Override
    public String toString() {
        return libraryName + " (" + location + ") | Database size: " + 
               itemRegistry.size() + " items, " + memberRegistry.size() + " members";
    }
}

// Main execution class
public class FinalProject {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("  FINAL PROJECT: SMART LIBRARY SYSTEM");
        System.out.println("==================================================");

        // Lab 9: Setting up the core architecture
        System.out.println("\n>>> Phase 1: Booting up Library (Lab 9) <<<");
        LibrarySystem library = new LibrarySystem("Yessenov Central Library", "Aktau, Kazakhstan");
        System.out.println("  " + library);

        // Labs 10 & 2: Testing our subclasses and overloaded constructors
        System.out.println("\n>>> Phase 2: Populating Data (Labs 10 & 2) <<<");
        Book b1 = new Book("B001", "Clean Code", "Robert C. Martin", 2008, "Programming");
        Book b2 = new Book("B002", "Design Patterns", "Gang of Four", 1994, "Architecture");
        Book b3 = new Book("B003", "The Pragmatic Programmer"); // Missing info, using overloaded constructor
        Magazine mag = new Magazine("M001", "IEEE Spectrum", "IEEE", 142);
        EBook eb = new EBook("E001", "Java in a Nutshell", "PDF", 12.5);
        
        library.addItem(b1);
        library.addItem(b2);
        library.addItem(b3);
        library.addItem(mag);
        library.addItem(eb);

        // Labs 1 & 9: Testing member roles
        System.out.println("\n>>> Phase 3: Registering Users (Labs 1 & 9) <<<");
        Member s1 = new Member(); // Default constructor test
        Member s2 = new Member("M001", "Aisha Bekova");
        Member s3 = new Member("M002", "Daniyar Serov");
        Librarian li = new Librarian("M003", "Zarina Nurova", "EMP-01", "Science");
        
        library.addMember(s2);
        library.addMember(s3);
        library.addMember(li);
        li.addToInventory(b1); // Verifying librarian-specific methods work

        // Core workflow test
        System.out.println("\n>>> Phase 4: Simulating Day-to-Day Operations <<<");
        library.borrowItem("M001", "B001");
        library.borrowItem("M002", "B002");
        library.borrowItem("M001", "E001");
        library.returnItem("M001", "B001"); // Aisha returns Clean Code

        library.printAllItems();
        library.printAllMembers();

        // Labs 1 & 5: Identity tests
        System.out.println("\n>>> Phase 5: Object Identity Check (Labs 1 & 5) <<<");
        Book copyB1 = new Book("B001", "Clean Code", "Robert C. Martin");
        System.out.println("  Checking if system recognizes duplicate items...");
        System.out.println("  b1.equals(copyB1) is: " + b1.equals(copyB1));
        System.out.println("  b1 hash: " + b1.hashCode() + " | copyB1 hash: " + copyB1.hashCode());

        // Lab 7: String mapping
        library.analyzeNameFrequency();

        // Lab 6: Regex and string manipulation
        String promoDescription = 
            "The library is a wonderful place for learning. " +
            "Students enjoy borrowing books every week! " +
            "Do you visit the reading room regularly?";
        TextAnalyzer.analyzeText(promoDescription);

        // Lab 3: Array math
        double[] userRatings = { 4.5, -1.2, 3.8, -0.5, 5.0, 2.1, -2.0, 4.0 };
        ArrayStats.processRatings(userRatings);

        // Lab 4: Variant 20 Matrix execution
        double[][] shelfGridData = {
            { 12,  8, 15,  3,  7 },
            {  5, 20,  9, 11,  6 },
            { 18,  4,  7, 25,  2 },
            {  9, 13,  6,  8, 14 },
            {  3,  7, 11,  5, 16 }
        };
        ShelfMatrix shelves = new ShelfMatrix(shelfGridData);
        shelves.display();
        shelves.calculateX();

        // Lab 8: Generics and set theory
        System.out.println("\n--- [Lab 8] Generic Symmetric Difference Demo ---");
        String[] oldBackupIDs = { "B001", "B002", "B003", "M001" };
        String[] currentIDs = { "B002", "B003", "E001", "E002" };
        Set<String> diff = Catalog.symmetricDifference(oldBackupIDs, currentIDs);
        System.out.println("  Old DB snapshot: " + Arrays.toString(oldBackupIDs));
        System.out.println("  Current DB state: " + Arrays.toString(currentIDs));
        System.out.println("  Delta (Items that changed): " + diff);

        // Lab 10: Persistent storage test
        System.out.println("\n>>> Phase 6: Shutting down & Saving (Lab 10) <<<");
        library.saveToFile("library_data.bin");
        LibrarySystem reloadedLib = LibrarySystem.loadFromFile("library_data.bin");
        if (reloadedLib != null) {
            System.out.println("  Verification passed: Reloaded data matches.");
        }

        System.out.println("\n==================================================");
        System.out.println("  EXECUTION COMPLETE - ALL LABS INTEGRATED.");
        System.out.println("==================================================");
    }
}