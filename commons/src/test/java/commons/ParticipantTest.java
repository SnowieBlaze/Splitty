package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    private Participant p1;
    private Participant p2;
    private Participant p3;
    @BeforeEach
    void testBasis() {
        long id = 5;
        p1 = new Participant("Bob");
        p2 = new Participant(id, "Alice", "alice@gmail.com", "NHJ201", "12345");
        p3 = new Participant(id, "Alice", "alice@gmail.com", "NHJ201", "12345");
    }

    @Test
    void testConstructorWithName() {
        assertNotNull(p1);
    }

    @Test
    void testConstructor() {
        assertNotNull(p2);
    }

    @Test
    void testEmptyContructor() {
        Participant p4 = new Participant();
        assertNotNull(p4);
    }

    @Test
    void testGetId() {
        assertEquals(5, p2.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Alice", p2.getName());
    }
    @Test
    void testGetEmail() {
        assertEquals("alice@gmail.com", p2.getEmail());
    }
    @Test
    void testGetEmailNull() {
        assertNull(p1.getEmail());
    }
    @Test
    void testGetIBAN() {
        assertEquals("NHJ201", p2.getIban());
    }
    @Test
    void testGetBIC() {
        assertEquals("12345", p2.getBic());
    }


    @Test
    void testSetId() {
        long id = 8;
        p1.setId(id);
        assertEquals(id, p1.getId());
    }

    @Test
    void setName() {
        p1.setName("Greg");
        assertEquals("Greg", p1.getName());
    }

    @Test
    void setEmail() {
        p1.setEmail("Greg@gmail.com");
        assertEquals("Greg@gmail.com", p1.getEmail());
    }

    @Test
    void setIBAN() {
        p1.setIban("ASJ123");
        assertEquals("ASJ123", p1.getIban());
    }

    @Test
    void setBIC() {
        p1.setBic("54321");
        assertEquals("54321", p1.getBic());
    }

    @Test
    void testEquals() {
        assertEquals(p2, p3);
        assertNotEquals(p1, p2);
    }

    @Test
    void testNotEqualsNull() {
        assertNotEquals(p1, null);
    }

    @Test
    void testHashCode() {
        assertEquals(p2.hashCode(), p3.hashCode());
    }
}
