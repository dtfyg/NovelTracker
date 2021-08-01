package model;

import model.exceptions.BookNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

public class Library implements Writable {

    private String name;
    private ArrayList<Novel> lib;

    //Effects: Creates a new library
    public Library(String name) {
        this.lib = new ArrayList<>();
        this.name = name;
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
            if (n.getName().equalsIgnoreCase(name)) {
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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("novels", novelsToJson());
        return json;
    }

    private JSONArray novelsToJson() {
        JSONArray jsonarray = new JSONArray();

        for (Novel n : lib) {
            jsonarray.put(n.toJson());
        }
        return jsonarray;
    }
}
