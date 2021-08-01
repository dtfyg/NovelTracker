package persistence;

import model.Library;
import model.Novel;
import model.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSaverTest extends JsonTest{

    @Test
    void testWriterInvalidInput() {
        Library lib = new Library("My Librart");
        JsonSaver saver = new JsonSaver("./dat/my\filename.json");
        try {
            saver.open();
            fail("IOexception expected");
        } catch (FileNotFoundException e) {
            //test success
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
    void saveLibraryNovels() {
        try {
            Library lib = new Library("The Library");
            lib.addNovel(new Novel("Lord of the rings"));
            lib.addNovel(new Novel("Harry potter"));
            lib.getNovel("Harry potter").rateNovel(4.6);
            JsonSaver json = new JsonSaver("./data/tester.json");
            json.open();
            json.write(lib);
            json.close();

            JsonLoader loader = new JsonLoader("./data/tester.json");
            lib = loader.read();
            ArrayList<Novel> novels = lib.getLib();
            assertEquals("The Library", lib.getName());
            checkNovel("Lord of the rings", -1.0, new ArrayList<String>()
                    ,lib.getNovel("Lord of the rings") );
            checkNovel("Harry potter", 4.6, new ArrayList<String>()
                    ,lib.getNovel("Harry potter") );

        } catch (BookNotFoundException e) {
            fail("Book not found");
        } catch (IOException i) {
            fail("IO exception caught");
        }
    }

}
