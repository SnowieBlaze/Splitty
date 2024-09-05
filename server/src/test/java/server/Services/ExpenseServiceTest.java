package server.Services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.TestEventRepository;
import server.Repositories.TestExpenseRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private EventRepository eventRepo;
    private ExpenseService expenseService;

    private Event event;
    private Participant a = new Participant("Ella");
    private Participant b = new Participant("John");
    private List<Participant> list = new ArrayList<>();
    private Expense exp1;
    private Expense exp2;

    @BeforeEach
    void first() {
        expenseRepo = new TestExpenseRepository();
        eventRepo = new TestEventRepository();
        expenseService =  new ExpenseService(expenseRepo, eventRepo);

        list = new ArrayList<>();
        list.add(a);
        list.add(b);

        event = new Event();
        event.setTitle("March");
        event.setParticipants(list);

        exp1 = new Expense("birthday", a, 12.0d, list);
        exp2 = new Expense("food", b, 1.0d, list);
        event.addExpense(exp1);
        event.addExpense(exp2);

        eventRepo.save(event);
        expenseRepo.save(exp1);
        expenseRepo.save(exp2);
    }

    @Test
    void getAll() {
        List<Expense> res = expenseService.getAll();

        assertEquals(2, res.size());
        assertEquals(true, res.contains(exp1));
        assertEquals(true, res.contains(exp2));
    }

    @Test
    void create() {
        Expense exp3 = new Expense("shop", a, 11.0d, list);
        assertEquals(false, expenseService.getAll().contains(exp3));

        expenseService.create(exp3);
        assertEquals(3, expenseService.getAll().size());
        assertEquals(true, expenseService.getAll().contains(exp3));
    }

    @Test
    void testCreate() {
        Expense exp3 = new Expense("shop", a, 11.0d, list);
        assertEquals(false, expenseService.getAll().contains(exp3));

        expenseService.create(event.getId(), exp3);
        assertEquals(3, expenseService.getAll().size());
        assertEquals(true, expenseService.getAll().contains(exp3));
        assertEquals(true, eventRepo.findById(event.getId()).get().getExpenses().contains(exp3));
    }

    @Test
    void testCreateNull() {
        assertNull(expenseService.create(5, exp1));
    }

    @Test
    void update() {
        Expense exp3 = new Expense("shop", a, 11.0d, new ArrayList<>());

        assertEquals("birthday", exp1.getTitle());
        assertEquals(2, exp1.getParticipants().size());

        expenseService.update(exp1.getId(), exp3);
//        assertEquals("shop", exp1.getTitle());
//        assertEquals(a, exp1.getPayingParticipant());
//        assertEquals(11.0d, exp1.getAmount());
//        assertEquals(0, exp1.getParticipants().size());
    }

    @Test
    void testUpdateNull() {
        assertNull(expenseService.update(5, exp1));
    }

    @Test
    void updateEvent() {
        expenseService.updateEvent(event.getId(), exp1);
        assertTrue(eventRepo.findAll().contains(event));
        assertTrue(expenseRepo.findAll().contains(exp1));
    }

    @Test
    void testUpdateEventNull() {
        assertNull(expenseService.updateEvent(5, exp1));
    }

    @Test
    void findById() {
        Optional<Expense> exp = expenseService.findById(exp1.getId());

        assertEquals(true, exp.isPresent());
        assertEquals(exp1, exp.get());
    }

    @Test
    void deleteById() {
        assertEquals(2, exp1.getParticipants().size());
        expenseService.deleteById(exp1.getId());
        assertEquals(1, expenseService.getAll().size());
        assertEquals(false, expenseService.getAll().contains(exp1));
    }

    @Test
    void existsById() {
        Expense exp3 = new Expense("shop", a, 11.0d, new ArrayList<>());

        assertEquals(true, expenseService.existsById(exp1.getId()));
        assertEquals(true, expenseService.existsById(exp2.getId()));
    }

    @Test
    void testFlush() {
        expenseService.flush();
        assertEquals(List.of(exp1, exp2), expenseRepo.findAll());
    }

    @Test
    void findByEventId() {
        List<Expense> actual = event.getExpenses();
        List<Expense> res = expenseService.findByEventId(event.getId());

        assertEquals(2, res.size());
        assertEquals(actual, res);
    }

    @Test
    void findByIdNotPresent() {
        assertEquals(new ArrayList<>(), expenseService.findByEventId(5));
    }

    @Test
    void deleteByEventId() {
        expenseService.deleteByEventId(event.getId(), exp1);
        assertEquals(List.of(exp2), expenseRepo.findAll());
    }

    @Test
    void deleteByEventIdNotPresent() {
        expenseRepo.deleteById(exp1.getId());
        exp1.setId(5);
        assertFalse(expenseService.deleteByEventId(event.getId(), exp1));
    }
}