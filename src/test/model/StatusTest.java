package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class StatusTest {

    Status s;
    Status s1;

    @BeforeEach
    void setup() {
        s = new Status(10,false);
        s1 = new Status(0, true);
    }

    @Test
    void testIsCompleted() {
        assertTrue(s1.getCompleted());
    }

    @Test
    void testSetComp() {
        s.setCompleted(true);
        assertTrue(s.getCompleted());
    }
    @Test
    void testChap() {
        s.setChapter(50);
        assertEquals(50, s.getChapter());
    }

    @Test
    void testToString() {
        assertEquals("c10", s.toString());
    }

    @Test
    void testCompleteString() {
        assertEquals("Completed", s1.toString());
    }

}
