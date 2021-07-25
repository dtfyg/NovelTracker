package model;

import java.util.ArrayList;

public class Novel {

    public static final String genre1 = "Fantasy";
    public static final String genre2 = "Sci-Fi";
    public static final String genre3 = "Romance";
    public static final String genre4 = "Comedy";
    public static final String genre5 = "Adventure";
    public static final String genre6 = "Yaoi/Yuri";
    public static final String genre7 = "LitRPG";
    public static final String genre8 = "Tragedy";

    private String name;

    private ArrayList<String> genre;

    private double rating;

    //Effects: Create a new novel with a name
    public Novel(String name) {
        this.name = name;
        this.rating = -1;
        this.genre = new ArrayList<>();
    }

    //Modifies: This
    //Effects: Adds a rating for the novel
    public void rateNovel(double rating) {
        this.rating = rating;
    }


    //Effects: Gets the rating of the novel
    public double getRating() {
        return this.rating;
    }


    //Modifies: This
    //Effects: Adds a genre to the novel
    public void addGenre(int genre) {
        switch (genre) {
            case 1: this.genre.add(genre1);
                break;
            case 2: this.genre.add(genre2);
                break;
            case 3: this.genre.add(genre3);
                break;
            case 4: this.genre.add(genre4);
                break;
            case 5: this.genre.add(genre5);
                break;
            case 6: this.genre.add(genre6);
                break;
            case 7: this.genre.add(genre7);
                break;
            case 8: this.genre.add(genre8);
                break;
            default:
                break;
        }
    }

    //Requires: this novel has a genre
    //Modifies: this
    //Effects: Removes the genre from this novel and return true, otherwise return false if the genre does not exist
    public boolean removeGenre(String genre) {
        if (this.genre.contains(genre)) {
            this.genre.remove(genre);
            return true;
        }
        return false;
    }

    //Requires: this novel has a genre
    //Effects: Returns all the genres of the Novel
    public ArrayList<String> getGenre() {
        return this.genre;
    }

    //Modifies: This
    //Effects: Changes the name of the novel
    public void changeName(String newName) {
        this.name = newName;
    }

    //Effects: Gets the name
    public String getName() {
        return this.name;
    }
}
