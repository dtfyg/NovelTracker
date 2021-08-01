package persistence;

import model.Novel;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkNovel(String name, double rating, ArrayList<String> genre, Novel novel){
        assertEquals(novel.getName(), name);
        assertEquals(novel.getRating(), rating);
        assertEquals(novel.getGenre(), genre);

    }

}
