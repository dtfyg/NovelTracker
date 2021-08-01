package persistence;

import model.Library;
import model.Novel;
import model.exceptions.BookNotFoundException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonLoaderTest extends JsonTest{

    @Test
    void testWriterInvalidInput() {
        Library lib = new Library("My Library");
        JsonSaver saver = new JsonSaver("./data/test.json");
        try {
            saver.open();
            saver.write(lib);
            saver.close();

            JsonLoader loader = new JsonLoader("./dataest.json");
            loader.read();
            fail("IOexception expected");
        } catch (IOException e) {
            //test success
        } catch (JSONException j) {
            fail("Caught Json exception");
        }

    }

    @Test
    void emptyLibraryTest(){
        Library lib = new Library("Mine");
        JsonSaver saver = new JsonSaver("./data/test.json");
        try {
            saver.open();
            saver.write(lib);
            saver.close();

            JsonLoader loader = new JsonLoader("./data/test.json");
            lib = loader.read();
            assertEquals("Mine", lib.getName());
            assertTrue(lib.empty());
        } catch (IOException e) {
            fail("IOException thrown");
        }

    }

    @Test
    void loadLibraryNovels() {
        try {
            Library lib = new Library("The Library");
            lib.addNovel(new Novel("Lord of the rings"));
            lib.addNovel(new Novel("Harry potter"));
            lib.getNovel("Harry potter").rateNovel(4.6);
            lib.getNovel("Harry potter").addGenre(4);
            lib.getNovel("Harry potter").addGenre(5);

            JsonSaver json = new JsonSaver("./data/tester2.json");
            json.open();
            json.write(lib);
            json.close();

            JsonLoader loader = new JsonLoader("./data/tester2.json");
            lib = loader.read();
            ArrayList<Novel> novels = lib.getLib();
            assertEquals("The Library", lib.getName());
            checkNovel("Lord of the rings", -1.0, new ArrayList<String>()
                    ,lib.getNovel("Lord of the rings") );
            ArrayList<String> a = new ArrayList<>();
            a.add("Comedy");
            a.add("Adventure");
            checkNovel("Harry potter", 4.6, a, lib.getNovel("Harry potter") );

        } catch (BookNotFoundException e) {
            fail("Book not found");
        } catch (IOException i) {
            fail("IO exception caught");
        }
    }

    @Test
    void testGetName(){
        JsonLoader loader = new JsonLoader("./data/testingName.json");
        try {
            Library lib = new Library("hi");
            JsonSaver saver = new JsonSaver("./data/testingName.json");
            saver.open();
            saver.write(lib);
            saver.close();

            assertEquals("hi", loader.loadNames());
        } catch (IOException e) {
            fail ("Caught IO Exception");
        } catch (JSONException j) {
            fail("Caught Json Exception");
        }

    }

    @Test
    void testNameFail(){
        JsonLoader loader = new JsonLoader("./data/testingName2.json");
        try {
            loader.loadNames();
            fail("JSONException not caught");
        } catch (IOException e) {
            fail ("Caught IO Exception");
        } catch (JSONException j) {
           // expected
        }

    }


}
