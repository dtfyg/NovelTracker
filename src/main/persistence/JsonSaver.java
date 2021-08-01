package persistence;

import model.Library;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//Takes Reference from CPSC210/JsonSerializationDemo

public class JsonSaver {

    public static final int SPACING = 4;
    private PrintWriter writer;
    private String destination;

    //Effects: Creates a saver that writes files at destination
    public JsonSaver(String destination) {
        this.destination = destination;
    }

    //Modifies: this
    //Effects: opens writer, throws error if file can't be found
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    //Modifies: this
    //Effects: Converts library to jsonObject
    public void write(Library lib) {
        JSONObject json = lib.toJson();
        saveToFile(json.toString(SPACING));
    }

    //Modifies: this
    //Effects: Closes writer
    public void close() {
        writer.close();
    }

    //Modifies: this
    //Effects: Saves data to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}
