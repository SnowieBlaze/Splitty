package server.Services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.TestDebtRepository;
import server.Repositories.TestEventRepository;
import server.database.DebtRepository;
import server.database.EventRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DebtServiceTest {
    @Mock
    private DebtRepository debtRepo;
    @Mock
    private EventRepository eventRepo;
    private DebtService sut;

    @BeforeEach
    void debtServiceSetUp() {
        debtRepo = new TestDebtRepository();
        eventRepo = new TestEventRepository();
        sut =  new DebtService(debtRepo, eventRepo);
    }
    @Test
    void testDebtServiceConstructor() {
        assertNotNull(sut);
    }
    @Test
    void testRemoveAllDebts() {
        List<Debt> debts = debtRepo.findAll();
        for(Debt debt : debts) {
            debtRepo.deleteById(debt.getId());
        }
        List<Debt> noDebts = debtRepo.findAll();
        assertEquals(noDebts.size(), 0);
    }

    @Test
    void testGetAll() {
        Debt debt1 = new Debt(new Participant("Ana"), new Participant("Bob"), 50);
        Debt debt2 = new Debt(new Participant("Bob"), new Participant("Ana"), 5);
        debtRepo.save(debt1);
        debtRepo.save(debt2);
        List<Debt> debts = sut.getAll();
        assertTrue(debts.contains(debt1));
        assertTrue(debts.contains(debt2));
        assertEquals(debts.size(), 2);
    }

    @Test
    void testGetPaymentInstructionsNonExistentEvent() {
        assertNull(sut.getPaymentInstructions(0));
    }

    @Test
    void testGetPaymentInstructions() {
        Participant a = new Participant("A");
        Participant b = new Participant("B");
        Participant c = new Participant("C");
        Participant d = new Participant("D");
        Debt debt1 = new Debt(b, a, 20);
        Debt debt2 = new Debt(d, a, 20);
        Debt debt3 = new Debt(b, d, 35);
        Debt debt4 = new Debt(a, d, 35);
        Debt debt5 = new Debt(a, c, 15);
        Debt debt6 = new Debt(d, c, 15);
        Debt debt7 = new Debt(c, d, 35);
        Event event = new Event();
        event.addSettledDebt(debt1);
        event.addSettledDebt(debt2);
        event.addSettledDebt(debt3);
        event.addSettledDebt(debt4);
        event.addSettledDebt(debt5);
        event.addSettledDebt(debt6);
        event.addSettledDebt(debt7);
        event.addParticipant(a);
        event.addParticipant(b);
        event.addParticipant(c);
        event.addParticipant(d);
        eventRepo.save(event);
        List<Debt> paymentInstructions = sut.getPaymentInstructions(event.getId());
        Debt instruction1 = new Debt(d, b, 55);
        Debt instruction2 = new Debt(d, a, 10);
        Debt instruction3 = new Debt(d, c, 5);
        List<Debt> instructions = List.of(instruction1, instruction2, instruction3);
        boolean equal;
        for(Debt instruction : instructions) {
            equal = false;
            for(Debt debt : paymentInstructions) {
                if(debt.getPersonOwing().equals(instruction.getPersonOwing())
                && debt.getAmount() == instruction.getAmount()
                && debt.getPersonPaying().equals(instruction.getPersonPaying())) {
                    equal = true;
                }
            }
            assertTrue(equal);
        }

        assertEquals(paymentInstructions.size(), 3);
    }

    @Test
    void testAddInvalidDebt() {
        Debt debt = sut.add(new Debt(null, new Participant("Ana"), 8));
        assertNull(debt);
    }

    @Test
    void testAddDebt() {
        Debt debt = new Debt(new Participant("Ana"), new Participant("Bob"), 500);
        sut.add(debt);
        List<Debt> debts = debtRepo.findAll();
        assertTrue(debts.contains(debt));
    }

    @Test
    void testDeleteInvalidDebt() {
        Debt debt = new Debt(new Participant("Ana"), new Participant("Bob"), 500);
        assertNull(sut.delete(debt.getId()));
    }

    @Test
    void testDeleteDebt() {
        Event event = new Event();
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        event.addParticipant(p1);
        event.addParticipant(p2);
        Expense expense = new Expense();
        expense.setPayingParticipant(p1);
        expense.setPayingParticipant(p2);
        Debt debt = new Debt(new Participant("Ana"), new Participant("Bob"), 50);
        expense.add(debt);
        event.addExpense(expense);
        eventRepo.save(event);
        List<Debt> debts = debtRepo.findAll();
        assertFalse(debts.contains(debt));
        debtRepo.save(debt);
        debts = debtRepo.findAll();
        assertTrue(debts.contains(debt));
        sut.delete(debts.getFirst().getId());
        debts = debtRepo.findAll();
        assertFalse(debts.contains(debt));
    }

    @Test
    void testGetById() {
        Debt debt = new Debt(new Participant("Ana"), new Participant("Bob"), 500);
        sut.add(debt);
        long debtId = debtRepo.findAll().getFirst().getId();
        assertNull(sut.getById(-5));
        assertNull(sut.getById(1));
        assertEquals(debt, sut.getById(debtId));
    }
}
