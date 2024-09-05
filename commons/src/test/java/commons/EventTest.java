package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    private Event event1;
    private Event event2;
    private Event event3;

    private Participant p1;
    private Participant p2;
    private Participant p3;

    private Expense e1;
    private Expense e2;
    private Expense e3;

    private List<Participant> participants1;
    private List<Participant> participants2;
    private List<Expense> expenses1;
    private List<Expense> expenses2;

    @BeforeEach
    void first() {
        p1 = new Participant("Obama");
        p2 = new Participant("Joe");
        p3 = new Participant("Donald");

        e1 = new Expense("drinks", p1, 100, "EUR", participants1, "2/25/2024");
        e2 = new Expense("snacks", p1, 100, "EUR", participants1, "2/25/2024");
        e3 = new Expense("fuel", p3, 56, participants1);

        participants1 = new ArrayList<>();
        participants1.add(p1);
        participants1.add(p2);
        participants1.add(p3);

        expenses1 = new ArrayList<>();
        expenses1.add(e1);
        expenses1.add(e2);
        expenses1.add(e3);

        event1 = new Event("BBQ", "inviteCode1", participants1, expenses1);
        event2 = new Event("Paintball", "inviteCode1", participants1, expenses2);
        event3 = new Event("Swimming", "inviteCode2", participants2, expenses1);
    }

    @Test
    void testEmptyConstructor() {
        Event event = new Event();
        assertNotNull(event);
    }

    @Test
    void testConstructorWithId() {
        long id = 1;
        Event event = new Event(id, "New Year", "sdfsdafsdfsa",
            new ArrayList<>(), new ArrayList<>(), "26/03/2024");
        assertNotNull(event);
    }

    @Test
    void getTitle() {
        assertEquals("BBQ", event1.getTitle());
    }

    @Test
    void setTitle() {
        event1.setTitle("Lasergaming");
        assertEquals("Lasergaming", event1.getTitle());
    }

    @Test
    void getInviteCode() {
        assertEquals("inviteCode1", event1.getInviteCode());
    }

    @Test
    void setInviteCode() {
        event1.setInviteCode("inviteCode2");
        assertEquals("inviteCode2", event1.getInviteCode());
    }

    @Test
    void getParticipants() {
        assertEquals(participants1, event1.getParticipants());
    }

    @Test
    void setParticipants() {
        event1.setParticipants(participants2);
        assertEquals(participants2, event1.getParticipants());
    }

    @Test
    void addParticipant() {
        Participant participant = new Participant();
        event1.addParticipant(participant);
        assertTrue(event1.getParticipants().contains(participant));
    }

    @Test
    void removeParticipant() {
        event1.removeParticipant(p1);
        assertFalse(event1.getParticipants().contains(p1));
    }

    @Test
    void testParticipantsEmpty() {
        List<Participant> participants = new ArrayList<>();
        event1.setParticipants(participants);
        assertTrue(event1.getParticipants().isEmpty());
    }

    @Test
    void getExpenses() {
        assertEquals(expenses1, event1.getExpenses());
    }

    @Test
    void setExpenses() {
        event1.setExpenses(expenses2);
        assertEquals(expenses2, event1.getExpenses());
    }

    @Test
    void addExpense() {
        Expense expense = new Expense();
        event1.addExpense(expense);
        assertTrue(event1.getExpenses().contains(expense));

    }

    @Test
    void removeExpense() {
        event1.removeExpense(e1);
        assertFalse(event1.getExpenses().contains(e1));
    }

    @Test
    void testExpensesEmpty() {
        List<Expense> expenses = new ArrayList<>();
        event1.setExpenses(expenses);
        assertTrue(event1.getExpenses().isEmpty());
    }

    @Test
    void getId() {
        assertEquals(0, event1.getId());
    }


    @Test
    void checkEquals() {
        assertNotSame(event1, event2);
    }

    @Test
    void getTotal() {
        double amount = event1.getTotal();
        assertEquals(256, amount);
    }

    @Test
    void testLastActivityNull() {
        expenses2 = new ArrayList<>();
        event1.setExpenses(expenses2);
        assertNull(event1.getLastActivity());
    }

    @Test
    void getLastActivity() {
        Expense expense = event1.getLastActivity();
        assertEquals(e3, expense);
    }

    @Test
    void getLastActivityTimeNull() {
        Event ea = new Event();
        Expense expense = ea.getLastActivity();
        assertNull(expense);
        assertEquals(ea.getLastActivityTime(), "No activity yet");
    }

    @Test
    void getLastActivityTimeSuccessful() {
        Event ea = new Event();
        Expense exp = new Expense();
        ea.addExpense(exp);
        Expense expense = ea.getLastActivity();
        assertNotNull(expense);
        assertNotEquals(ea.getLastActivityTime(), "No activity yet");
    }

    @Test
    void getSettledDebts() {
        Debt debt = new Debt(p1, p2, 100);
        event1.addSettledDebt(debt);
        assertEquals(event1.getSettledDebts(), List.of(debt));
    }

    @Test
    void setSettledDebts() {
        Debt debt = new Debt(p1, p2, 100);
        event1.setSettledDebts(List.of(debt));
        assertEquals(event1.getSettledDebts(), List.of(debt));
    }

    @Test
    void addSettledDebt() {
        Debt debt = new Debt(p1, p2, 100);
        assertEquals(event1.getSettledDebts(), new ArrayList<>());
        event1.addSettledDebt(debt);
        assertEquals(event1.getSettledDebts(), List.of(debt));
    }

    @Test
    void setDateTime() {
        event1.setDateTime("test");
        assertEquals("test", event1.getDateTime());
    }

    @Test
    void testUpdateDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        event1.updateDateTime();
        assertEquals(event1.getDateTime(), dtf.format(now));
    }

    @Test
    void testCreateInviteCode() {
        String inviteCode = event1.createInviteCode();
        assertNotNull(inviteCode);
        assertEquals(inviteCode.length(), 8);
        assertTrue(Pattern.matches("[a-zA-Z0-9]+", inviteCode));
    }

    @Test
    void testEqualsEqual() {
        Event event4 = new Event("Swimming", "inviteCode2", participants2, expenses1);
        assertTrue(event3.equals(event4));
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(event1.equals(event1));
    }

    @Test
    void testEqualsDifferent() {
        assertFalse(event1.equals(event3));
    }

    @Test
    void testEqualsDifferentType() {
        assertFalse(event1.equals(new Debt()));
    }

    @Test
    void testHashCodeSame() {
        Event event4 = new Event("Swimming", "inviteCode2", participants2, expenses1);
        assertEquals(event4.hashCode(), event3.hashCode());
    }

    @Test
    void testHashCodeDifferent() {
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

}
