package server.Controllers;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.Repositories.TestEventRepository;
import server.Repositories.TestExpenseRepository;
import server.Repositories.TestTagRepository;
import server.Services.TagService;
import server.api.TagController;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestTagController {
    @Mock
    private TagRepository tagRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    @Mock
    private EventRepository eventRepo;
    @Mock
    private TagService tagService;

    private TagController sut;

    private Tag tag1;
    private Tag tag2;
    private Tag tag3;

    @BeforeEach
    void setUpTagController() {
        tagRepo = new TestTagRepository();
        expenseRepo = new TestExpenseRepository();
        eventRepo = new TestEventRepository();
        tagService = new TagService(tagRepo, eventRepo, expenseRepo);
        sut = new TagController(tagService);
        tag1 = new Tag("food", "#93C47D");
        tag2 = new Tag("entrance fees", "#4A86E8");
        tag3 = new Tag("travel", "#E06666");
    }

    @Test
    void testConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        assertEquals(List.of(tag1, tag2, tag3), sut.getAll());
    }

    @Test
    void testAdd() {
        Tag tag4 = new Tag("", "");
        ResponseEntity<Tag> response =  sut.add(tag4);
        assertEquals(response.getBody(), tag4);
    }

    @Test
    void testAddInvalid() {
        Tag tag4 = new Tag();
        ResponseEntity<Tag> response =  sut.add(tag4);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void testUpdate() {
        tag3 = tagRepo.save(tag3);
        tag3.setType("taxi");
        ResponseEntity<Tag> response = sut.update(tag3);
        assertEquals(response.getBody(), tag3);
    }

    @Test
    void testUpdateInvalid() {
        tag3.setType(null);
        ResponseEntity<Tag> response = sut.update(tag3);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void testDelete() {
        ResponseEntity<String> response = sut.delete(tag3.getId());
        assertEquals("Successful delete", response.getBody());
    }

    @Test
    void testDeleteInvalid() {
        ResponseEntity<String> response = sut.delete(5);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
