package model;

import model.exceptions.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    Library lib;
    Novel har;
    Novel beam;

    @BeforeEach
    public void setup(){
        lib = new Library("Beemo's Library");
        har = new Novel("Harry");
        beam = new Novel("beemo");
    }

    @Test
    public void novelFoundTest(){
        lib.addNovel(har);

        try {
            assertEquals(har, lib.getNovel("Harry"));
        } catch (BookNotFoundException e) {
            fail("Caught booknotfound exception");
        }
    }

    @Test
    public void novelNotFoundTest(){
        lib.addNovel(har);

        try {
            lib.getNovel("Beemo");
            fail("BookNotFoundException expected");
        } catch (BookNotFoundException e) {

        }
    }
    @Test
    void testGetLib(){
        ArrayList<Novel> r = lib.getLib();
        assertEquals(r, lib.getLib());
    }

    @Test
    void testEmpty(){
        assertTrue(lib.empty());
    }

}
