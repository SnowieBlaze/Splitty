package server.Services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.Repositories.TestDebtRepository;
import server.Repositories.TestEventRepository;
import server.Repositories.TestExpenseRepository;
import server.Repositories.TestParticipantRepository;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantServiceTest {

    @Mock
    private TestParticipantRepository participantRepo;
    @Mock
    private DebtRepository debtRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private EventRepository eventRepo;
    private ParticipantService sut;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        participantRepo = new TestParticipantRepository();
        debtRepo = new TestDebtRepository();
        expenseRepo = new TestExpenseRepository();
        eventRepo = new TestEventRepository();
        sut = new ParticipantService(participantRepo, debtRepo,
            expenseRepo, eventRepo);
    }


    @Test
    void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        participantRepo.save(p1);
        participantRepo.save(p2);
        List<Participant> participantsSaved = sut.getAll();
        List<Participant> participants = List.of(p1, p2);
        assertEquals(participants, participantsSaved);
    }

    @Test
    void testUpdate() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        Participant participantSaved = participantRepo.save(p1);
        Participant returnedParticipant = sut.update(participantSaved.getId(), p2);
        assertEquals(returnedParticipant, p2);
        Optional<Participant> participantFound = participantRepo.findById(returnedParticipant.getId());
        assertTrue(participantFound.isPresent());
        assertEquals(p2, participantFound.get());
    }

    @Test
    void testUpdateNull() {
        Participant p1 = new Participant("Bob");
        Participant returnedParticipant = sut.update(0, p1);
        assertNull(returnedParticipant);
    }

    @Test
    void testCreate() {
        Participant p1 = new Participant("Bob");
        Participant savedParticipant = sut.create(p1);
        assertEquals(p1, savedParticipant);
        List<Participant> participants = participantRepo.findAll();
        assertEquals(participants, List.of(p1));
    }

    @Test
    void testDelete() {
        Event event = new Event();
        Participant p1 = new Participant("Bob");
        event.addParticipant(p1);
        Expense expense = new Expense();
        Debt debt = new Debt(p1, p1, 100);
        expense.add(debt);
        expense.addParticipant(p1);
        expense.setPayingParticipant(p1);
        event.addExpense(expense);
        Participant participantSaved = participantRepo.save(p1);
        eventRepo.save(event);
        assertTrue(sut.delete(participantSaved.getId()));
        assertFalse(sut.delete(participantSaved.getId()));
    }

    @Test
    void testGetById() {
        Participant p1 = new Participant("Bob");
        Participant participantSaved = participantRepo.save(p1);
        Optional<Participant> participantOptional = sut.getById(participantSaved.getId());
        assertTrue(participantOptional.isPresent());
        assertEquals(participantOptional.get(), participantSaved);
    }

    @Test
    void testFlush() {
        sut.flush();
        assertEquals("flush", participantRepo.getCalledMethods().getLast());
    }
}
