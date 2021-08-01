package model;

import model.Exceptions.BookNotFoundException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Library {

    private ArrayList<Novel> lib;

    //Effects: Creates a new library
    public Library() {
        this.lib = new ArrayList<>();
    }

    //Effects: Adds a novel to the library
    public void addNovel(Novel novel) {
        lib.add(novel);
    }

    //Effects: Returns the novel of name, otherwise throws booknotfound exception
    public Novel getNovel(String name) throws BookNotFoundException {
        boolean contains = false;
        int position = 0;
        for (Novel n : lib) {
            if (n.getName().equals(name)) {
                contains = true;
                position = lib.indexOf(n);
            }
        }
        if (contains) {
            return lib.get(position);
        } else {
            throw new BookNotFoundException();
        }

    }

    //Effects: Returns the library
    public ArrayList<Novel> getLib() {
        return lib;
    }

    //Effects: Checks if the library is empty
    public boolean empty() {
        return lib.isEmpty();
    }

}
