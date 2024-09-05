package server.Controllers;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.Repositories.*;
import server.Services.EventService;
import server.api.EventController;
import server.database.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestEventController {
    @Mock
    private EventRepository eventRepo;
    @Mock
    private ParticipantRepository participantRepo;
    @Mock
    private DebtRepository debtRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private EventService eventService;

    private EventController sut;

    private Event event1;

    @BeforeEach
    void setUpEventController() {
        eventRepo = new TestEventRepository();
        participantRepo = new TestParticipantRepository();
        debtRepo = new TestDebtRepository();
        expenseRepo = new TestExpenseRepository();
        tagRepository = new TestTagRepository();
        eventService = new EventService(eventRepo, participantRepo,
            debtRepo, expenseRepo, tagRepository);
        sut = new EventController(eventService);
        event1 = new Event();
    }

    @Test
    void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        eventRepo.save(event1);
        assertEquals(List.of(event1), sut.getAll());
    }

    @Test
    void testGetByCode() {
        event1.setInviteCode("abc");
        eventRepo.save(event1);
        ResponseEntity<?> response = sut.getByCode("abc");
        assertEquals(event1, response.getBody());
    }

    @Test
    void testGetByCodeInvalid() {
        event1.setInviteCode("a");
        eventRepo.save(event1);
        ResponseEntity<?> response = sut.getByCode("abc");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Event not found.", response.getBody());
    }

    @Test
    void testCreateEvent() {
        ResponseEntity<?> response = sut.createEvent(event1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(event1, response.getBody());
    }

    @Test
    void testUpdate() {
        eventRepo.save(event1);
        event1.setInviteCode("abc");
        ResponseEntity<?> response = sut.update(event1.getId(), event1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event1, response.getBody());
    }

    @Test
    void testUpdateNonExistent() {
        event1.setInviteCode("abc");
        ResponseEntity<?> response = sut.update(5, event1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Event not found", response.getBody());
    }

    @Test
    void testGetById() {
        eventRepo.save(event1);
        ResponseEntity<Event> response = sut.getById(event1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event1, response.getBody());
    }

    @Test
    void testGetByIdInvalid() {
        ResponseEntity<Event> response = sut.getById(event1.getId());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDelete() {
        eventRepo.save(event1);
        ResponseEntity<String> response = sut.delete(event1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successful delete", response.getBody());
    }

    @Test
    void testDeleteInvalid() {
        ResponseEntity<String> response = sut.delete(event1.getId());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAddEvent() {
        Event event = sut.addEvent(event1);
        assertEquals(event1, event);
    }

    @Test
    void testDeleteEvent() {
        Event event = sut.deleteEvent(event1);
        assertEquals(event1, event);
    }

}
