
import java.io.*;
import java.util.*;

//Lab 10
abstract class LibraryItem implements Serializable{
    private static final long serialVersionUID = 1L;

    protected String itemID;
    protected String title;
    protected boolean isAvailable;

    //Lab 2 Constructor Overloading
    public LibraryItem(){
        this.itemID    = "UNKNOWN";
        this.title     = "Untitled";
        this.isAvailable = true;
    }

    public LibraryItem(String itemID, String title){
        this.itemID    = itemID;
        this.title     = title;
        this.isAvailable = true;
    }

    public LibraryItem(String itemID, String title, boolean isAvailable){
        this.itemID      = itemID;
        this.title       = title;
        this.isAvailable = isAvailable;
    }

    // Abstract methods enforced by subclasses (Lab 10)
    public abstract String getType();
    public abstract String getSummary();

    // Getters / setters
    public String  getItemID()               { return itemID; }
    public String  getTitle()                { return title; }
    public boolean isAvailable()             { return isAvailable; }
    public void    setAvailable(boolean v)   { this.isAvailable = v; }

    //Lab 1 & 5 toString, equals, hashCode
    @Override
    public String toString(){
        return String.format("[%-8s] ID:%-5s  %-40s  Available: %s",
                getType(), itemID, title, isAvailable ? "YES" : "NO");
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LibraryItem other = (LibraryItem) obj;
        return Objects.equals(itemID, other.itemID);
    }

    @Override
    public int hashCode(){
        return Objects.hash(itemID);
    }
}

//Lab 10 + Lab 1  Concrete subclasses
class Book extends LibraryItem{
    private String author;
    private int    year;
    private String genre;

    //Lab 2 Three Book constructors
    public Book(String itemID, String title){
        super(itemID, title);
        this.author = "Unknown";
        this.year   = 0;
        this.genre  = "General";
    }

    public Book(String itemID, String title, String author){
        super(itemID, title);
        this.author = author;
        this.year   = 0;
        this.genre  = "General";
    }

    public Book(String itemID, String title, String author, int year, String genre){
        super(itemID, title);
        this.author = author;
        this.year   = year;
        this.genre  = genre;
    }

    @Override public String getType()    { return "Book"; }
    @Override public String getSummary() {
        return title + " by " + author + " (" + year + ") [" + genre + "]";
    }

    public String getAuthor() { return author; }
    public int    getYear()   { return year; }
    public String getGenre()  { return genre; }
}

class Magazine extends LibraryItem{
    private String publisher;
    private int    issueNumber;

    public Magazine(String itemID, String title, String publisher, int issueNumber){
        super(itemID, title);
        this.publisher   = publisher;
        this.issueNumber = issueNumber;
    }

    @Override public String getType()    { return "Magazine"; }
    @Override public String getSummary() {
        return title + " | Issue #" + issueNumber + " | " + publisher;
    }
}

class EBook extends LibraryItem{
    private String format;       // PDF, EPUB …
    private double fileSizeMB;

    public EBook(String itemID, String title, String format, double fileSizeMB){
        super(itemID, title);
        this.format     = format;
        this.fileSizeMB = fileSizeMB;
    }

    @Override public String getType()    { return "EBook"; }
    @Override public String getSummary() {
        return title + " [" + format + ", " + fileSizeMB + " MB]";
    }
}

//Lab 9 + Lab 1  Member hierarchy from UML design 
class Member implements Serializable{
    private static final long serialVersionUID = 1L;

    protected String      memberID;
    protected String      name;
    protected List<String> borrowedItemIDs;

    //Lab 2 Constructor overloading
    public Member(){
        this.memberID        = "M000";
        this.name            = "Unknown";
        this.borrowedItemIDs = new ArrayList<>();
    }

    public Member(String memberID, String name){
        this.memberID        = memberID;
        this.name            = name;
        this.borrowedItemIDs = new ArrayList<>();
    }

    public void borrowItem(String itemID) { borrowedItemIDs.add(itemID); }
    public void returnItem(String itemID) { borrowedItemIDs.remove(itemID); }

    public String      getMemberID()        { return memberID; }
    public String      getName()            { return name; }
    public List<String> getBorrowedItemIDs() { return borrowedItemIDs; }

    //Lab 1 & 5 toString, equals, hashCode
    @Override
    public String toString(){
        return "Member[" + memberID + "]: " + name +
               " | Borrowed: " + borrowedItemIDs.size() + " item(s)";
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member other = (Member) obj;
        return Objects.equals(memberID, other.memberID);
    }

    @Override
    public int hashCode() { return Objects.hash(memberID); }
}

//Lab 1 & 9 Librarian extends Member inheritance + polymorphism
class Librarian extends Member{
    private String employeeID;
    private String section;

    public Librarian(String memberID, String name, String employeeID, String section){
        super(memberID, name);
        this.employeeID = employeeID;
        this.section    = section;
    }

    public void addToInventory(LibraryItem item){
        System.out.println("  Librarian " + name + " added: " + item.getSummary());
    }

    public void removeFromInventory(String itemID){
        System.out.println("  Librarian " + name + " removed item ID: " + itemID);
    }

    @Override
    public String toString(){
        return "Librarian[" + employeeID + "]: " + name + " | Section: " + section;
    }
}

//Lab 8  Generic Catalog<T>
class Catalog<T> implements Serializable{
    private static final long serialVersionUID = 1L;

    private List<T>  items;
    private String   catalogName;

    public Catalog(String catalogName){
        this.catalogName = catalogName;
        this.items       = new ArrayList<>();
    }

    public void    add(T item)    { items.add(item); }
    public boolean remove(T item) { return items.remove(item); }
    public List<T> getAll()       { return Collections.unmodifiableList(items); }
    public int     size()         { return items.size(); }

    // Generic static method – symmetric difference (identical logic to Lab 8)
    public static <T> Set<T> symmetricDifference(T[] group1, T[] group2){
        Set<T> set1   = new HashSet<>(Arrays.asList(group1));
        Set<T> set2   = new HashSet<>(Arrays.asList(group2));
        Set<T> result = new HashSet<>();
        for (T item : set1) if (!set2.contains(item)) result.add(item);
        for (T item : set2) if (!set1.contains(item)) result.add(item);
        return result;
    }

    @Override
    public String toString(){
        return "Catalog[" + catalogName + "] → " + items.size() + " items";
    }
}

//Lab 6 Text / Sentence Analyzer
class TextAnalyzer{
    private static final String VOWELS =
            "aeiouAEIOUаеёиоуыэюяАЕЁИОУЫЭЮЯ";

    public static void analyzeText(String text){
        System.out.println("\nLab 6 Sentence Analysis");
        String[] sentences = text.split("(?<=[.!?])\\s*");
        for (int i = 0; i < sentences.length; i++) {
            analyzeSentence(sentences[i].trim(), i + 1);
        }
    }

    private static void analyzeSentence(String sentence, int idx){
        int vowels = 0, consonants = 0;
        for (char c : sentence.toCharArray()){
            if (Character.isLetter(c)) {
                if (VOWELS.indexOf(c) >= 0) vowels++;
                else consonants++;
            }
        }
        String verdict = vowels > consonants ? "More Vowels"
                       : consonants > vowels ? "More Consonants"
                       : "Equal";
        System.out.printf("  #%d  \"%s\"%n", idx, sentence);
        System.out.printf("      Vowels: %d | Consonants: %d → %s%n",
                vowels, consonants, verdict);
        System.out.println("      " + "-".repeat(60));
    }
}

//Lab 3 Array Processing (ratings as doubles)
class ArrayStats{
    public static void processRatings(double[] ratings){
        System.out.println("\nLab 3 Array Processing – Book Ratings");
        double productOfNegatives = 1.0;
        double sumOfPositives     = 0.0;
        boolean hasNegatives      = false;

        for (double v : ratings){
            if (v < 0) { productOfNegatives *= v; hasNegatives = true; }
            else if (v > 0) { sumOfPositives += v; }
        }

        System.out.println("  Ratings: " + Arrays.toString(ratings));
        if (hasNegatives)
            System.out.printf("  Product of negative ratings : %.4f%n", productOfNegatives);
        else
            System.out.println("  No negative ratings found.");
        System.out.printf("  Sum of positive ratings     : %.4f%n", sumOfPositives);
    }
}

//Lab 4  5×5 Matrix – Shelf Occupancy Grid
class ShelfMatrix{
    private static final int SIZE = 5;
    private final double[][] grid;

    public ShelfMatrix(double[][] grid) { this.grid = grid; }

    public void display(){
        System.out.println("\nLab 4 5×5 Shelf Grid (books per cell)");
        System.out.println("       S0      S1      S2      S3      S4");
        for (int i = 0; i < SIZE; i++) {
            System.out.printf("  R%d ", i);
            for (int j = 0; j < SIZE; j++)
                System.out.printf("%7.1f", grid[i][j]);
            System.out.println();
        }
    }

    public double[] calculateX() {
        System.out.println("\n  Calculating X-vector (Variant 20 logic)…");
        double maxVal    = grid[0][0],   absMaxVal = Math.abs(grid[0][0]);
        int    maxCol    = 0,            absMaxCol = 0;

        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j]          > maxVal)    { maxVal    = grid[i][j];          maxCol    = j; }
                if (Math.abs(grid[i][j]) > absMaxVal) { absMaxVal = Math.abs(grid[i][j]); absMaxCol = j; }
            }

        double[] x = new double[SIZE];
        if (maxCol == absMaxCol) {
            for (int i = 0; i < SIZE; i++) x[i] = grid[i][maxCol];
            System.out.println("  Max and |Max| share column " + maxCol + " → column extracted.");
        } else {
            for (int i = 0; i < SIZE; i++) x[i] = grid[i][SIZE - 1 - i];
            System.out.println("  Max (col " + maxCol + ") ≠ |Max| (col " + absMaxCol + ") → anti-diagonal extracted.");
        }
        System.out.println("  X = " + Arrays.toString(x));
        return x;
    }
}

//Lab 7 + Lab 9 - Core Library System 
class LibrarySystem implements Serializable{
    private static final long serialVersionUID = 1L;

    private String                       libraryName;
    private String                       location;
    //Lab 7 HashMap registries
    private HashMap<String, LibraryItem> itemRegistry;
    private HashMap<String, Member>      memberRegistry;
    //Lab 8 Generic catalogs
    private Catalog<Book>                bookCatalog;
    private Catalog<Magazine>            magazineCatalog;

    public LibrarySystem(String name, String location){
        this.libraryName      = name;
        this.location         = location;
        this.itemRegistry     = new HashMap<>();
        this.memberRegistry   = new HashMap<>();
        this.bookCatalog      = new Catalog<>("Books");
        this.magazineCatalog  = new Catalog<>("Magazines");
    }

    // Item management 
    public void addItem(LibraryItem item){
        itemRegistry.put(item.getItemID(), item);
        if      (item instanceof Book)     bookCatalog.add((Book) item);
        else if (item instanceof Magazine) magazineCatalog.add((Magazine) item);
        System.out.println("  Added  → " + item.getSummary());
    }

    public void addMember(Member member) {
        memberRegistry.put(member.getMemberID(), member);
        System.out.println("  Registered → " + member);
    }

    public boolean borrowItem(String memberID, String itemID){
        Member      m = memberRegistry.get(memberID);
        LibraryItem i = itemRegistry.get(itemID);
        if (m == null || i == null || !i.isAvailable()){
            System.out.println(" Borrow failed (item unavailable or ID wrong).");
            return false;
        }
        i.setAvailable(false);
        m.borrowItem(itemID);
        System.out.println(m.getName() + " borrowed: " + i.getTitle());
        return true;
    }

    public boolean returnItem(String memberID, String itemID){
        Member      m = memberRegistry.get(memberID);
        LibraryItem i = itemRegistry.get(itemID);
        if (m == null || i == null){
            System.out.println(" Return failed (invalid ID).");
            return false;
        }
        i.setAvailable(true);
        m.returnItem(itemID);
        System.out.println(m.getName() + " returned: " + i.getTitle());
        return true;
    }

    //Lab 7 Letter frequency on library name
    public void analyzeNameFrequency(){
        System.out.println("\nLab 7 - Letter Frequency in Library Name");
        HashMap<Character, Integer> freq = new HashMap<>();
        for (char c : libraryName.toLowerCase().toCharArray())
            if (Character.isLetter(c))
                freq.put(c, freq.getOrDefault(c, 0) + 1);
        System.out.println("  Text: \"" + libraryName + "\"");
        for (Map.Entry<Character, Integer> e : freq.entrySet())
            System.out.println("  '" + e.getKey() + "' → " + e.getValue());
    }

    //Lab 10 Serialization
    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("  Saved to: " + fileName);
        } catch (IOException e) {
            System.out.println("  Save error: " + e.getMessage());
        }
    }

    public static LibrarySystem loadFromFile(String fileName) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(fileName))) {
            System.out.println("  Loaded from: " + fileName);
            return (LibrarySystem) ois.readObject();
        } catch (Exception e) {
            System.out.println("  Load error: " + e.getMessage());
            return null;
        }
    }

    // Display helpers ---
    public void printAllItems() {
        System.out.println("\nAll Items in " + libraryName);
        for (LibraryItem item : itemRegistry.values())
            System.out.println("  " + item);
    }

    public void printAllMembers() {
        System.out.println("\nAll Members");
        for (Member m : memberRegistry.values())
            System.out.println("  " + m);
    }

    @Override
    public String toString() {
        return libraryName + " @ " + location +
               "  [Items: " + itemRegistry.size() +
               " | Members: " + memberRegistry.size() + "]";
    }
}

// Main – runs all lab demonstrations in sequence 
public class FinalProject {

    public static void main(String[] args) {

        printBanner("FINAL PROJECT: Smart Library Management System");

        //Lab 9 Create the library (from UML design) 
        section("Lab 9 – Initializing Library System (UML design)");
        LibrarySystem library =
                new LibrarySystem("Yessenov Central Library", "Aktau, Kazakhstan");
        System.out.println("  " + library);

        //Lab 10 + Lab 2 Abstract hierarchy + constructor overloading
        section("Lab 10 + Lab 2 – Adding Items (abstract hierarchy + overloaded constructors)");
        Book b1 = new Book("B001", "Clean Code", "Robert C. Martin", 2008, "Programming");
        Book b2 = new Book("B002", "Design Patterns", "Gang of Four", 1994, "Architecture");
        Book b3 = new Book("B003", "The Pragmatic Programmer");          // 2-arg constructor
        Magazine mag = new Magazine("M001", "IEEE Spectrum", "IEEE", 142);
        EBook    eb  = new EBook("E001", "Java in a Nutshell", "PDF", 12.5);
        library.addItem(b1);
        library.addItem(b2);
        library.addItem(b3);
        library.addItem(mag);
        library.addItem(eb);

        //Lab 1 + Lab 9 Member hierarchy – inheritance & polymorphism 
        section("Lab 1 + Lab 9 – Member Registration (inheritance / polymorphism)");
        Member    s1 = new Member();                              // default constructor
        Member    s2 = new Member("M001", "Aisha Bekova");
        Member    s3 = new Member("M002", "Daniyar Serov");
        Librarian li = new Librarian("M003", "Zarina Nurova", "EMP-01", "Science");
        library.addMember(s2);
        library.addMember(s3);
        library.addMember(li);
        li.addToInventory(b1);   // Librarian-only method

        //Lab 9 Borrow / return workflow 
        section("Lab 9 – Borrow & Return Operations");
        library.borrowItem("M001", "B001");
        library.borrowItem("M002", "B002");
        library.borrowItem("M001", "E001");
        library.returnItem("M001", "B001");   // return one book

        library.printAllItems();
        library.printAllMembers();

        //Lab 1 + Lab 5 equals() and hashCode()
        section("Lab 1 & 5 – equals() and hashCode()");
        Book copyB1 = new Book("B001", "Clean Code", "Robert C. Martin");
        System.out.println("  b1.equals(copyB1) : " + b1.equals(copyB1));
        System.out.println("  b1.hashCode()     : " + b1.hashCode());
        System.out.println("  copyB1.hashCode() : " + copyB1.hashCode());
        System.out.println("  (hashCodes match because only itemID is used)");

        //Lab 7 HashMap letter frequency
        library.analyzeNameFrequency();

        //Lab 6 Sentence analysis
        String description =
            "The library is a wonderful place for learning. " +
            "Students enjoy borrowing books every week! " +
            "Do you visit the reading room regularly?";
        TextAnalyzer.analyzeText(description);

        //Lab 3 Array processing
        double[] ratings = { 4.5, -1.2, 3.8, -0.5, 5.0, 2.1, -2.0, 4.0 };
        ArrayStats.processRatings(ratings);

        //Lab 4 5×5 Matrix 
        double[][] shelfGrid = {
            { 12,  8, 15,  3,  7 },
            {  5, 20,  9, 11,  6 },
            { 18,  4,  7, 25,  2 },
            {  9, 13,  6,  8, 14 },
            {  3,  7, 11,  5, 16 }
        };
        ShelfMatrix shelf = new ShelfMatrix(shelfGrid);
        shelf.display();
        shelf.calculateX();

        // Lab 8 Generics – symmetric difference
        section("Lab 8 - Generic Symmetric Difference");
        String[] oldIDs = { "B001", "B002", "B003", "M001" };
        String[] newIDs = { "B002", "B003", "E001", "E002" };
        Set<String> diff = Catalog.symmetricDifference(oldIDs, newIDs);
        System.out.println("  Old catalog : " + Arrays.toString(oldIDs));
        System.out.println("  New catalog : " + Arrays.toString(newIDs));
        System.out.println("  Unique items (added or removed): " + diff);

        // Lab 10 Serialization – save & reload 
        section("Lab 10 – Serialization: Save and Load");
        library.saveToFile("library_data.bin");
        LibrarySystem reloaded = LibrarySystem.loadFromFile("library_data.bin");
        if (reloaded != null)
            System.out.println("  Reloaded: " + reloaded);

        // Summary
        printBanner(
            "All 10 Lab Concepts Successfully Demonstrated",
            "Lab1(inherit) | Lab2(ctors) | Lab3(array) | Lab4(matrix) | Lab5(equals)",
            "Lab6(string)  | Lab7(map)   | Lab8(gen.)  | Lab9(UML)    | Lab10(serial)"
        );
    }

    // Helpers
    private static void section(String title){
        System.out.println("\n" + title);
    }

    private static void printBanner(String... lines){
        int w = 0;
        for (String l : lines) w = Math.max(w, l.length());
        String bar = "╔" + "═".repeat(w + 4) + "╗";
        System.out.println("\n" + bar);
        for (String l : lines)
            System.out.printf("║  %-" + w + "s  ║%n", l);
        System.out.println("╚" + "═".repeat(w + 4) + "╝");
    }
}
