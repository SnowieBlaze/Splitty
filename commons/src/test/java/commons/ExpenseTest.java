package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    private Participant a = new Participant("Ana");
    private Participant b = new Participant("Are");
    private Participant c = new Participant("Mere");

    private List<Participant> list;

    private Expense exp1;
    private Expense exp2;
    private Expense exp3;

    @BeforeEach
    void first() {
        list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        exp1 = new Expense("party", a, 100, "EUR", list, "2/25/2024");
        exp2 = new Expense("party", a, 100, "EUR", list, "2/25/2024");
        exp3 = new Expense("partyy", b, 56, list);
    }

    @Test
    void testEmptyConstructor() {
        Expense expense = new Expense();
        assertNotNull(expense);
    }

    @Test
    void testPayingParticipantConstructor() {
        Participant bob = new Participant("Bob");
        Expense expense = new Expense(bob);
        assertNotNull(expense);
        assertEquals(expense.getPayingParticipant(), bob);

    }

    @Test
    void testGetId() {
        assertEquals(exp1.getId(), exp1.getId());
    }

    @Test
    void testSetId() {
        long id = 5;
        exp1.setId(id);
        assertEquals(exp1.getId(), id);
    }

    @Test
    void getTitle() {
        assertEquals("party", exp1.getTitle());
    }

    @Test
    void testSecondConstructor(){
        Expense exp4 = new Expense(a);
        assertNotNull(exp4);
        assertEquals(exp4.getPayingParticipant(), a);
    }

    @Test
    void setTitle() {
        exp1.setTitle("Party");
        assertEquals("Party", exp1.getTitle());
    }

    @Test
    void getPayingParticipant() {
        assertEquals(a, exp1.getPayingParticipant());
    }

    @Test
    void setPayingParticipant() {
        exp1.setPayingParticipant(b);
        assertEquals(b, exp1.getPayingParticipant());
    }

    @Test
    void getAmount() {
        assertEquals(100.0d, exp1.getAmount());
    }

    @Test
    void setAmount() {
        exp1.setAmount(150.0d);
        assertEquals(150.0d, exp1.getAmount());
    }

    @Test
    void getParticipants() {
        assertEquals(list, exp1.getParticipants());
    }

    @Test
    void setParticipants() {
        List<Participant> list2 = new ArrayList<>();
        list2.add(a);

        exp1.setParticipants(list2);
        assertEquals(list2, exp1.getParticipants());
    }

    @Test
    void testAddDebt() {
        Debt debt = new Debt(a, b, 100);
        exp1.add(debt);
        assertEquals(exp1.getDebts(), List.of(debt));
    }

    @Test
    void testGetDebts() {
        Debt debt = new Debt(a, b, 100);
        exp1.setDebts(List.of(debt));
        assertEquals(exp1.getDebts(), List.of(debt));
    }

    @Test
    void testSetDebts() {
        Debt debt = new Debt(a, b, 100);
        exp1.setDebts(List.of(debt));
        assertEquals(exp1.getDebts(), List.of(debt));
        Debt debt2 = new Debt(b, a, 50);
        exp1.setDebts(List.of(debt2, debt));
        assertEquals(exp1.getDebts(), List.of(debt2, debt));
    }

    @Test
    void getDateTime() {
        assertEquals("2/25/2024", exp1.getDateTime());
    }

    @Test
    void setDateTime() {
        exp1.setDateTime("2/26/2024");
        assertEquals("2/26/2024", exp1.getDateTime());
    }

    @Test
    void getActivity() {
        String[] activity = new String[4];
        activity[0] = exp1.getPayingParticipant().getName();
        activity[1] = String.valueOf(exp1.getAmount());
        activity[2] = exp1.getCurrency();
        activity[3] = exp1.getTitle();
        assertArrayEquals(activity, exp1.getActivity());
    }

    @Test
    void testSameEquals() {
        assertEquals(exp1, exp1);
    }

    @Test
    void testNullEquals() {
        assertNotEquals(exp1, null);
    }

    @Test
    void testEquals() {
        assertEquals(exp1, exp2);
        assertNotEquals(exp1, exp3);
    }

    @Test
    void testHashCode() {
        assertEquals(exp2.hashCode(), exp1.hashCode());
        assertNotEquals(exp1.hashCode(), exp3.hashCode());
    }

    @Test
    void addParticipant() {
        Participant pepe = new Participant("Pepe");
        assertFalse(exp1.getParticipants().contains(pepe));
        exp1.addParticipant(pepe);
        assertTrue(exp1.getParticipants().contains(pepe));
    }

    @Test
    void delParticipant() {
        assertTrue(exp1.getParticipants().contains(a));
        exp1.delParticipant(a);
        assertFalse(exp1.getParticipants().contains(a));
    }

    @Test
    void testNotEqualsNullReference(){
        assertNotEquals(exp1, null);
    }

    @Test
    void getCurrency() {
        assertEquals(exp1.getCurrency(), "EUR");
    }

    @Test
    void setCurrency() {
        Expense exp4 = new Expense("party", a, 100, "EUR", list, "2/25/2024");
        exp4.setCurrency("CHF");
        assertEquals(exp4.getCurrency(), "CHF");
    }

    @Test
    void getTag() {
        Expense exp4 = new Expense("party", a, 100, "EUR", list, "2/25/2024");
        Tag t = new Tag("tag" , "tag green");
        exp4.setTag(t);
        assertNotNull(exp4.getTag());
        assertEquals(t, exp4.getTag());
    }

    @Test
    void setTag() {
        Expense exp4 = new Expense("party", a, 100, "EUR", list, "2/25/2024");
        Tag t = new Tag("tag" , "tag green");
        exp4.setTag(t);
        assertNotNull(exp4.getTag());
        assertEquals(t, exp4.getTag());
    }
}