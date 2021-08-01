package persistence;

import model.Library;
import model.Novel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

//Takes Reference from CPSC210/JsonSerializationDemo

public class JsonLoader {
    private String source;
    
    //Effects: Creates a loader to read the source file
    public JsonLoader(String source) {
        this.source = source;
    }

    public String loadNames() throws IOException, JSONException {
        JSONObject object = new JSONObject(readFile(source));

        String name = object.getString("name");

        return name;
    }

    public Library read() throws IOException, JSONException {
        String data = readFile(source);
        JSONObject object = new JSONObject(data);
        return parseLibrary(object);
    }

    private Library parseLibrary(JSONObject object) {
        String name = object.getString("name");
        Library lib = new Library(name);
        addNovels(lib, object);
        return lib;
    }

    private void addNovels(Library lib, JSONObject object) {
        JSONArray array = object.getJSONArray("novels");
        for (Object json : array) {
            JSONObject nextNovel = (JSONObject) json;
            addNovel(lib, nextNovel);
        }
    }

    private void addNovel(Library lib, JSONObject nextNovel) {
        String name = nextNovel.getString("name");
        int rating = nextNovel.getInt("rating");
        JSONArray jsonGenres = nextNovel.getJSONArray("genres");

        ArrayList<String> genres = new ArrayList<>();

        for (int i = 0; i < jsonGenres.length(); i++) {
            String s = jsonGenres.getString(i);
            genres.add(s);
        }

        Novel novel = new Novel(name, rating, genres);
        lib.addNovel(novel);
    }

    private String readFile(String source) throws IOException {
        StringBuilder builder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> builder.append(s));
        }

        return builder.toString();
    }

}
