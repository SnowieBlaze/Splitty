package server.Controllers;


import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.Repositories.TestDebtRepository;
import server.Repositories.TestEventRepository;
import server.Services.DebtService;
import server.api.DebtController;
import server.database.DebtRepository;
import server.database.EventRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDebtController {
    @Mock
    private DebtRepository debtRepo;
    @Mock
    private EventRepository eventRepo;
    @Mock
    private DebtService debtService;
    private DebtController sut;

    @BeforeEach
    void debtControllerSetUp() {
        eventRepo = new TestEventRepository();
        debtRepo = new TestDebtRepository();
        debtService = new DebtService(debtRepo, eventRepo);
        sut = new DebtController(debtService);
    }

    @Test
    void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        Debt debt1 = new Debt(p1, p2, 200);
        Debt debt2 = new Debt(p2, p1, 100);
        List<Debt> debts = new ArrayList<>();
        List<Debt> debtsSaved = sut.getAll();
        assertEquals(debtsSaved, debts);
        debtService.add(debt1);
        debtService.add(debt2);
        debts.add(debt1);
        debts.add(debt2);
        debtsSaved = sut.getAll();
        assertEquals(debtsSaved, debts);
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
        List<Debt> paymentInstructions = sut.getPaymentInstructions(event.getId()).getBody();
        assertNotNull(paymentInstructions);
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
    void testGetPaymentInstructionsNull() {
        ResponseEntity<List<Debt>> paymentInstructions = sut.getPaymentInstructions(0);
        assertEquals(HttpStatus.BAD_REQUEST, paymentInstructions.getStatusCode());
    }

    @Test
    void testAdd() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        Debt debt = new Debt(p1, p2, 100);
        ResponseEntity<Debt> response = sut.add(debt);
        assertEquals(response.getBody(), debt);
    }

    @Test
    void testAddNull() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        Debt debt = new Debt(p1, p2, -100);
        ResponseEntity<Debt> response = sut.add(debt);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void testDelete() {
        Event event = new Event();
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        event.addParticipant(p1);
        event.addParticipant(p2);
        Expense expense = new Expense();
        expense.setPayingParticipant(p1);
        expense.setPayingParticipant(p2);
        Debt debt = new Debt(p1, p2, 100);
        expense.add(debt);
        event.addExpense(expense);
        eventRepo.save(event);
        debtService.add(debt);
        ResponseEntity<String> response = sut.delete(debt.getId());
        assertEquals(response.getBody(), "Successful delete");
    }

    @Test
    void testDeleteNull() {
        ResponseEntity<String> response = sut.delete(0);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGetById() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        Debt debt = new Debt(p1, p2, 100);
        debt = debtService.add(debt);
        ResponseEntity<Debt> response = sut.getById(debt.getId());
        assertEquals(response.getBody(), debt);
    }

    @Test
    void testGetByIdNull() {
        ResponseEntity<Debt> response = sut.getById(0);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
