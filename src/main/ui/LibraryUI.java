package ui;

import model.exceptions.BookNotFoundException;
import model.Library;
import model.Novel;
import org.json.JSONException;
import persistence.JsonLoader;
import persistence.JsonSaver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LibraryUI {

    private Scanner scan;
    private Library list;

    private JsonLoader loader1;
    private JsonSaver saver1;

    private JsonLoader loader2;
    private JsonSaver saver2;

    private JsonLoader loader3;
    private JsonSaver saver3;

    private static final String JSON_DIR_1 = "./data/library.json";
    private static final String JSON_DIR_2 = "./data/library2.json";
    private static final String JSON_DIR_3 = "./data/library3.json";

    private boolean loaded = false;

    //Effects: runs the library application
    public LibraryUI() {
        runLibrary();
    }

    //Effects: The main loop for the game, will keep running unless Q is entered
    private void runLibrary() {
        boolean running = true;
        String action;
        init();
        while (running) {
            askQuestion();
            action = scan.nextLine();
            if (action.equalsIgnoreCase("Q")) {
                running = false;
            } else {
                doAction(action);
            }
        }
    }

    //Effects: Does the action inputted by the player
    private void doAction(String action) {
        String book;
        switch (action.toUpperCase()) {
            case "A":
                System.out.println("Please enter the name of a novel.");
                book = scan.nextLine();
                addBook(book);
                break;
            case "L":
                displayList();
                break;
            case "SAVE":
                chooseSaveSlot();
                break;
            case "LOAD":
                chooseLoadSlot(false);
                break;
            default:
                checkForBook(action);
                break;
        }

    }

    //Effects: Checks the list to see if the entered book exists, if it does, it gives you more options to chooses from
    //otherwise, it will give you an error message
    private void checkForBook(String novel) {
        String bookAction;
        boolean contains = false;
        int position = 0;

        Novel nu;

        try {
            nu = list.getNovel(novel);
            System.out.println("What would you like to do with the book?");
            System.out.println("R --> add rating        G to --> add genre          N --> Rename novel,");
            System.out.println("1 --> Get info about the novel          2 --> Remove a Genre");
            bookAction = scan.nextLine();
            useBook(bookAction, nu);
        } catch (BookNotFoundException b) {
            System.err.println("Sorry, " + novel + " was not found.");
        }

    }

    //Modifies: This
    //Effects: Uses one of the actions listed depending on the user input
    private void useBook(String bookAction, Novel novel) {
        switch (bookAction.toUpperCase()) {
            case "R":
                setRating(novel);
                break;
            case "G":
                giveGenre(novel);
                break;
            case "N":
                rename(novel);
                break;
            case "1":
                System.out.println("Name: " + novel.getName());
                System.out.println("Rating: " + novel.getRating());
                System.out.println("Genres: " + novel.getGenre());
                break;
            case "2":
                removeGen(novel);
                break;
            default:
                System.out.println("Your command was not recognized");
                break;
        }
    }

    //Modifies: This
    //Effects: Allows user to pick a rating for selected novel
    public void setRating(Novel novel) {
        double n;
        System.out.println("Please enter a rating to give the novel from 1 to 5.");
        n = scan.nextDouble();
        novel.rateNovel(n);
        System.out.println(novel.getName() + " has been rated " + n);
        scan.nextLine();
    }

    //Modifies: This
    //Effects: Allows user to pick a new name for selected novel
    public void rename(Novel novel) {
        System.out.println("Please enter a new name");
        String name = scan.nextLine();
        novel.changeName(name);
        System.out.println("Name has been changed to " + name);
    }

    //Modifies: This
    //Effects: Allows user to pick a genre for selected novel
    public void giveGenre(Novel novel) {
        int n;
        System.out.println("Please choose genre: press 1 for fantasy, 2 for sci-fi, 3 for romance, ");
        System.out.println("4 for comedy, 5 for adventure, 6 for yaoi/yuri, 7 for litrpg and 8 for tragedy");
        n = scan.nextInt();
        novel.addGenre(n);
        System.out.println("The genre has been added");
        scan.nextLine();
    }

    //Effects: Asks question based on whether there is book in the list or not
    private void askQuestion() {
        if (list.empty() && !loaded) {
            System.out.println("Welcome to the novel progress tracker, what would you like to do?");
            System.out.println("A --> Add book");
            System.out.println("Save --> Save           Load --> Load");
            System.out.println("Q --> Quit");
        } else {
            System.out.println("A --> Add another book     (book name) --> Select a book      L --> List of Books");
            System.out.println("Save --> Save           Load --> Load");
            System.out.println("Q --> Quit");
        }
    }

    //Modifies: This
    //Effects Initializes variables
    private void init() {
        scan = new Scanner(System.in);
        saver1 = new JsonSaver(JSON_DIR_1);
        loader1 = new JsonLoader(JSON_DIR_1);
        saver2 = new JsonSaver(JSON_DIR_2);
        loader2 = new JsonLoader(JSON_DIR_2);
        saver3 = new JsonSaver(JSON_DIR_3);
        loader3 = new JsonLoader(JSON_DIR_3);
        createLibraries();
    }

    //Effects: Displays load slots as either empty or full
    private void createLibraries() {
        System.out.println("Please create or load a library!");
        try {
            System.out.println("File 1: " + loader1.loadNames());
        } catch (JSONException n) {
            System.out.println("File 1: Empty");
        } catch (IOException i) {
            System.err.println("Error in Loading file");
        }
        try {
            System.out.println("File 2: " + loader2.loadNames());
        } catch (JSONException n) {
            System.out.println("File 2: Empty");
        } catch (IOException i) {
            System.err.println("Error in Loading file");
        }
        try {
            System.out.println("File 3: " + loader3.loadNames());
        } catch (JSONException n) {
            System.out.println("File 3: Empty");
        } catch (IOException i) {
            System.err.println("Error in Loading file");
        }
        chooseLoadSlot(true);
    }

    //Effects: Allows user to choose a save slot
    private void chooseSaveSlot() {
        System.out.println("Type 1, 2 or 3 to choose a file");
        int n = scan.nextInt();
        scan.nextLine();
        switch (n) {
            case 1: saveLibrary(saver1);
            break;
            case 2: saveLibrary(saver2);
            break;
            case 3: saveLibrary(saver3);
            break;
            default:
                chooseSaveSlot();
                break;
        }
    }

    //Effects: Allows user to choose a load slot
    private void chooseLoadSlot(boolean create) {
        System.out.println("Type 1, 2 or 3 to choose a file");
        int n = scan.nextInt();
        scan.nextLine();
        String s;
        if (create) {
            System.out.println("C --> Create new        L --> Load file");
            s = scan.nextLine();
        } else {
            s = "L";
        }
        switch (n) {
            case 1: createLoad(s, 1);
                break;
            case 2: createLoad(s, 2);
                break;
            case 3: createLoad(s, 3);
                break;
            default:
                System.err.println("Error slot not found");
                createLibraries();
                break;
        }
    }

    //Effects: Allows user to choose between creating a slot or loading a slot
    private void createLoad(String choice, int slot) {
        switch (choice.toUpperCase()) {
            case "C":
                System.out.println("Please enter a name for the library");
                String name = scan.nextLine();
                list = new Library(name);
                if (slot == 1) {
                    saveLibrary(saver1);
                } else if (slot == 2) {
                    saveLibrary(saver2);
                } else {
                    saveLibrary(saver3);
                }
                break;
            case "L":
                if (slot == 1) {
                    loadLibrary(loader1);
                } else if (slot == 2) {
                    loadLibrary(loader2);
                } else {
                    loadLibrary(loader3);
                }
                break;
        }
    }


    //Modifies: This
    //Effects: Creates a new book of title and adds it to the list
    private void addBook(String title) {
        Novel n = new Novel(title);
        list.addNovel(n);
        System.out.println("Book added successfully!");
    }

    //Effects: Prints out the list of current books in the library
    public void displayList() {
        System.out.println("The current books in the library are:");
        for (Novel n : list.getLib()) {
            System.out.print(n);
            System.out.println(" ");
        }
    }

    //Effects: Removes specified genre from given novel
    public void removeGen(Novel n) {
        String s;
        System.out.println("The current Genres of the book are:");
        System.out.println(n.getGenre());
        System.out.println("Please type the name of the one you want to remove.");
        s = scan.nextLine();
        n.removeGenre(s);
    }

    //Effects: Saves the library of given save slot
    public void saveLibrary(JsonSaver slot) {
        try {
            slot.open();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }
        slot.write(list);
        slot.close();
        System.out.println("Library saved to" + JSON_DIR_1);
    }

    //Effects: Loads the library of given save slot
    public void loadLibrary(JsonLoader slot) {
        try {
            list = slot.read();
            System.out.println("Library has been loaded.");
        } catch (IOException e) {
            System.err.println("Unable to read file.");
        } catch (JSONException j) {
            System.err.println("No file to load!");
            chooseLoadSlot(true);
        }
        loaded = true;
    }

}
