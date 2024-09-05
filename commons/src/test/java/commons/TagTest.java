package commons;

import commons.Tag;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {
    private commons.Tag t1;
    private commons.Tag t2;
    private commons.Tag t3;
    @BeforeEach
    void testBasis() {
        t1 = new Tag("food", "red");
        t2 = new commons.Tag("drinks", "blue");
        t3 = new commons.Tag("drinks", "blue");
    }

    @Test
    void testConstructor() {
        assertNotNull(t1);
    }

    @Test
    void testEmptyConstructor() {
        Tag t4 = new Tag();
        assertNotNull(t4);
    }

    @Test
    void testGetType() {
        assertEquals("food", t1.getType());
    }

    @Test
    void testGetColor() {
        assertEquals("red", t1.getColor());
    }

    @Test
    void testSetType() {
        t1.setType("decoration");
        assertEquals("decoration", t1.getType());
    }

    @Test
    void testSetColor() {
        t1.setColor("purple");
        assertEquals("purple", t1.getColor());
    }

    @Test
    void testEquals() {
        assertEquals(t2, t3);
        assertNotEquals(t1, t2);
    }

    @Test
    void testEqualsSameReference() {
        assertEquals(t2, t2);
    }

    @Test
    void testNotEqualsNullReference() {
        assertNotEquals(t2, null);
    }

    @Test
    void testHashCode() {
        assertEquals(t2.hashCode(), t3.hashCode());
    }

    @Test
    void getId() {
        assertNotNull(t1.getId());
    }
}
