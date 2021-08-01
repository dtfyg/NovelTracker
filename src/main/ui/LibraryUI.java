package ui;

import model.Exceptions.BookNotFoundException;
import model.Library;
import model.Novel;

import java.util.ArrayList;

import java.util.Scanner;

public class LibraryUI {

    private Scanner scan;
    private Library list;


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
            if (action.equals("Q") || action.equals("q")) {
                running = false;
            } else {
                doAction(action);
            }
        }
    }

    //Effects: Does the action inputted by the player
    private void doAction(String action) {
        String book;
        switch (action) {
            case "A":
            case "a":
                System.out.println("Please enter the name of a novel.");
                book = scan.nextLine();
                addBook(book);
                break;
            case "L":
            case "l":
                displayList();
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
            System.out.println("What would you like to do with the book");
            System.out.println("Press R to give it a rating, press G to add a genre, press N to rename the book,");
            System.out.println("Press 1 to get the rating, the genres and the name of the book.");
            System.out.println("Press 2 to remove a genre from the book.");
            bookAction = scan.nextLine();
            useBook(bookAction, nu);
        } catch (BookNotFoundException b) {
            System.err.println("Sorry, " + novel + " was not found.");
        }

    }

    //Modifies: This
    //Effects: Uses one of the actions listed depending on the user input
    private void useBook(String bookAction, Novel novel) {
        switch (bookAction) {
            case "R": case "r":
                setRating(novel);
                break;
            case "G": case "g":
                giveGenre(novel);
                break;
            case "N": case "n":
                rename(novel);
                break;
            case "1":
                System.out.println("The name of the novel is " + novel.getName());
                System.out.println("The rating of " + novel.getName() + " is " + novel.getRating());
                System.out.println("The genres of " + novel.getName() + " are " + novel.getGenre());
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
        if (list.empty()) {
            System.out.println("What would you like to do, press A to add a book, press Q to quit.");
        } else {
            System.out.println("To add another book, press A. To select a book,"
                    + " type its name. To view a list of your books, press L. To quit, press Q.");
        }
    }

    //Modifies: This
    //Effects Initializes variables
    private void init() {
        scan = new Scanner(System.in);
        list = new Library();
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

    public void removeGen(Novel n) {
        String s;
        System.out.println("The current Genres of the book are:");
        System.out.println(n.getGenre());
        System.out.println("Please type the name of the one you want to remove.");
        s = scan.nextLine();
        n.removeGenre(s);
    }

}
