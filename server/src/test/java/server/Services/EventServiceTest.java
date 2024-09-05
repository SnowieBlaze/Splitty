
package server.Services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.*;
import server.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    @Mock
    private TestEventRepository eventRepo;
    @Mock
    private ParticipantRepository participantRepo;
    @Mock
    private DebtRepository debtRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private TagRepository tagRepo;

    private EventService sut;

    Event event1;
    Event event2;
    Participant p1;
    Participant p2;
    Expense expense1;
    Debt debt1;

    @BeforeEach
    void eventServiceSetUp() {
        eventRepo = new TestEventRepository();
        participantRepo = new TestParticipantRepository();
        debtRepo = new TestDebtRepository();
        expenseRepo = new TestExpenseRepository();
        tagRepo = new TestTagRepository();
        sut = new EventService(eventRepo, participantRepo,
            debtRepo, expenseRepo, tagRepo);
        event1 = new Event();
        event2 = new Event();
        p1 = new Participant("Bob");
        p2 = new Participant("Ana");
        expense1 = new Expense();
        expense1.addParticipant(p1);
        expense1.addParticipant(p2);
        debt1 = new Debt(p1, p2, 100);
        expense1.add(debt1);
        event1.addParticipant(p1);
        event1.addParticipant(p2);
        event1.addExpense(expense1);
    }

    @Test
    void testEventServiceConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testFindAll() {
        eventRepo.save(event1);
        eventRepo.save(event2);
        List<Event> events = sut.findAll();
        assertEquals(List.of(event1, event2), events);
    }

    @Test
    void testFindByInviteCode() {
        event1.setInviteCode("abc");
        eventRepo.save(event1);
        assertEquals(event1, sut.findByInviteCode("abc").get());
    }

    @Test
    void testCreate() {
        sut.create(event1);
        assertTrue(eventRepo.findAll().contains(event1));
    }

    @Test
    void testUpdate() {
        event1 = eventRepo.save(event1);
        event1.setInviteCode("abc");
        sut.update(event1.getId(), event1);
        assertEquals(event1, eventRepo.findById(event1.getId()).get());
    }

    @Test
    void testUpdateNull() {
        assertNull(sut.update(5, event1));
    }

    @Test
    void testFindById() {
        eventRepo.save(event1);
        assertEquals(Optional.of(event1), sut.findById(event1.getId()));
    }

    @Test
    void testDeleteById() {
        sut.create(event1);
        sut.deleteById(event1.getId());
        assertEquals(new ArrayList<>(), eventRepo.findAll());
        assertEquals(new ArrayList<>(), participantRepo.findAll());
        assertEquals(new ArrayList<>(), expenseRepo.findAll());
        assertEquals(new ArrayList<>(), debtRepo.findAll());
    }

    @Test
    void testExistsById() {
        eventRepo.save(event1);
        assertTrue(sut.existsById(event1.getId()));
    }

    @Test
    void testFlush() {
        sut.flush();
        assertEquals("flush", eventRepo.getCalledMethods().getLast());
    }



    @Test
    void testImportEvent() {
        expense1.setPayingParticipant(p1);
        sut.importEvent(event1);
        assertEquals(List.of(event1), eventRepo.findAll());
    }
}
