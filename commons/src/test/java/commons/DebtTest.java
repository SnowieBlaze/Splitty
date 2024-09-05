package commons;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DebtTest {

    private Participant p1 = new Participant(1, "a");
    private Participant p2 = new Participant(2, "b");
    private Debt d1 = new Debt(123, p1, p2, 500);
    private Debt d2 = new Debt(123, p1, p2, 500);
    private Debt d3 = new Debt(321, p2, p2, 511);

    @Test
    void testBasicConstructor() {
        Debt d0 = new Debt();
        assertNotNull(d0);
    }

    @Test
    void testConstructorWithoutId() {
        Debt d4 = new Debt(p1, p2, 500);
        assertNotNull(d4);
    }

    @Test
    void testConstructor(){
        assertNotNull(d1);
    }

    @Test
    void testGetId() {
        assertEquals(123, d1.getId());
    }

    @Test
    void testGetPersonPaying() {
        assertEquals(p1, d1.getPersonPaying());
    }

    @Test
    void testGetPersonOwed() {
        assertEquals(p2, d1.getPersonOwing());
    }

    @Test
    void testGetAmount() {
        assertEquals(500, d1.getAmount());
    }

    @Test
    void testIsPaid() {
        assertFalse(d1.isPaid());
    }

    @Test
    void testGetPaidDateTimeNULL() {
        assertNotNull(d1.getPaidDateTime());
    }

    @Test
    void testSetId() {
        long id = 321;
        d2.setId(id);
        assertEquals(d2.getId(), id);
    }

    @Test
    void testSetPersonPaying() {
        d1.setPersonPaying(p2);
        assertEquals(p2, d1.getPersonPaying());
    }

    @Test
    void testPersonOwed() {
        d1.setPersonOwing(p1);
        assertEquals(p1, d1.getPersonOwing());
    }

    @Test
    void testSetAmount() {
        d2.setAmount(4000);
        assertEquals(d2.getAmount(), 4000);
    }

    @Test
    void testSetPaid() {
        d2.setPaid(true);
        assertTrue(d2.isPaid());
    }

    @Test
    void testSetPaidDateTime() {
        LocalDateTime now = LocalDateTime.now();
        d2.setPaidDateTime(now.toString());
        assertNotNull(d2.getPaidDateTime());
    }

    @Test
    void testGetPaidDateTimeNotNull() {
        LocalDateTime now = LocalDateTime.now();
        d2.setPaidDateTime(now.toString());
        assertEquals(now.toString(), d2.getPaidDateTime());
    }

    @Test
    void getPersonPaying() {
        Participant p = d1.getPersonPaying();
        assertEquals(p1, p);
    }

    @Test
    void getPersonOwed() {
        Participant p = d1.getPersonOwing();
        assertEquals(p2, p);
    }

    @Test
    void setPersonPaying() {
        Debt d = new Debt(123, p1, p2, 500);
        Participant p = d.getPersonPaying();
        d.setPersonPaying(p2);
        assertNotEquals(p, d.getPersonPaying());
        assertEquals(p2, d.getPersonPaying());
    }

    @Test
    void setPersonOwed() {
        Debt d = new Debt(123, p1, p2, 500);
        Participant p = d.getPersonOwing();
        d.setPersonOwing(p1);
        assertNotEquals(p, d.getPersonOwing());
        assertEquals(p1, d.getPersonOwing());
    }

    @Test
    void testEqualsEqual() {
        assertTrue(d1.equals(d2));
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(d1.equals(d1));
    }

    @Test
    void testEqualsDifferent() {
        assertFalse(d1.equals(d3));
    }

    @Test
    void testEqualsNull() {
        assertFalse(d1.equals(null));
    }

    @Test
    void testHashCodeSame() {
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testHashCodeDifferent() {
        assertNotEquals(d1.hashCode(), d3.hashCode());
    }
}