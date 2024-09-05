package server.Controllers;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.Repositories.TestDebtRepository;
import server.Repositories.TestEventRepository;
import server.Repositories.TestExpenseRepository;
import server.Repositories.TestParticipantRepository;
import server.Services.ParticipantService;
import server.api.ParticipantController;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestParticipantController {
    @Mock
    private ParticipantRepository participantRepo;
    @Mock
    private ParticipantService participantService;
    @Mock
    private DebtRepository debtRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private EventRepository eventRepo;
    private ParticipantController sut;

    @BeforeEach
    void participantControllerSetUp() {
        participantRepo = new TestParticipantRepository();
        debtRepo = new TestDebtRepository();
        expenseRepo = new TestExpenseRepository();
        eventRepo = new TestEventRepository();
        participantService = new ParticipantService(participantRepo, debtRepo,
            expenseRepo, eventRepo);
        sut = new ParticipantController(participantService);
    }

    @Test
    void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        Participant p1 = new Participant("Bob");
        Participant p2 = new Participant("Ana");
        List<Participant> participants = new ArrayList<>();
        List<Participant> participantsSaved = sut.getAll();
        assertEquals(participants, participantsSaved);
        participantService.create(p1);
        participantService.create(p2);
        participants.add(p1);
        participants.add(p2);
        participantsSaved = sut.getAll();
        assertEquals(participantsSaved, participants);
    }

    @Test
    void testUpdate() {
        Participant p1 = new Participant("Bob");
        Participant participantSaved = participantService.create(p1);
        p1 = new Participant("Ana");
        ResponseEntity<?> participantReturned = sut.update(p1);
        assertEquals(p1, participantReturned.getBody());
    }

    @Test
    void testUpdateNull() {
        Participant p1 = new Participant("Ana");
        ResponseEntity<?> participantReturned = sut.update(p1);
        assertEquals(participantReturned.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreate() {
        Participant p1 = new Participant("Ana");
        ResponseEntity<?> response = sut.create(p1);
        assertEquals(response.getBody(), p1);
        List<Participant> participants = participantService.getAll();
        assertEquals(participants, List.of(p1));
    }

    @Test
    void testDelete() {
        Event event = new Event();
        Participant p1 = new Participant("Ana");
        event.addParticipant(p1);
        eventRepo.save(event);
        Participant participantSaved = participantService.create(p1);
        ResponseEntity<?> response = sut.delete(participantSaved.getId());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<Participant> participantsFound = participantService.getAll();
        assertEquals(participantsFound, new ArrayList<>());
    }

    @Test
    void testDeleteNonExistent() {
        ResponseEntity<?> response = sut.delete(0);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "Participant not found!");
    }

    @Test
    void testGetById() {
        Participant p1 = new Participant("Bob");
        Participant participantSaved = participantService.create(p1);
        ResponseEntity<?> response = sut.getById(participantSaved.getId());
        assertEquals(response.getBody(), participantSaved);
    }

    @Test
    void testGetByIdNonExistent() {
        ResponseEntity<?> response = sut.getById(0);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(), "No participant by id");
    }


}
