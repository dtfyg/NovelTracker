package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NovelTest {

    Novel n1;
    Novel n2;
    Novel n3;
    ArrayList<String> s;

    @BeforeEach
    public void setup(){
        n1 = new Novel("Harry Potter and the Philosopher's Stone");
        n2 = new Novel("Mother of Learning");
        n3 = new Novel("Beware of Chicken");
    }

    @Test
    public void testAddRating(){
        n1.rateNovel(3);
        assertEquals(3, n1.getRating());
    }


    @Test
    public void testAddGenre(){
        n3.addGenre(2);
        s = new ArrayList<>();
        s.add(Novel.genre2);
        assertEquals(s, n3.getGenre());
    }

    @Test
    public void testAddMultipleGenre(){
        n3.addGenre(4);
        n3.addGenre(1);
        n3.addGenre(5);
        n3.addGenre(6);
        n3.addGenre(3);
        n3.addGenre(7);
        n3.addGenre(8);
        s = new ArrayList<>();
        s.add(Novel.genre4);
        s.add(Novel.genre1);
        s.add(Novel.genre5);
        s.add(Novel.genre6);
        s.add(Novel.genre3);
        s.add(Novel.genre7);
        s.add(Novel.genre8);
        assertEquals(s, n3.getGenre());
    }

    @Test
    public void testAddOtherGenre(){
        n3.addGenre(12);
        s = new ArrayList<>();
        assertEquals(s, n3.getGenre());
    }

    @Test
    public void testRemoveGenreSuccess(){
        n3.addGenre(4);
        assertTrue(n3.removeGenre("Comedy"));
        s = new ArrayList<>();
        assertEquals(s, n3.getGenre());
    }

    @Test
    public void testRemoveGenreFail(){
        n3.addGenre(4);
        assertFalse(n3.removeGenre("Adventure"));
    }

    @Test
    public void testNameChange(){
        n3.changeName("Chicken Attack!");
        assertEquals("Chicken Attack!", n3.getName());
    }

    @Test
    public void testToString(){
        assertEquals("[Mother of Learning]", n2.toString());
    }

}