package server.Services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import server.Repositories.TestEventRepository;
import server.Repositories.TestExpenseRepository;
import server.Repositories.TestTagRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TagServiceTest {
    @Mock
    private TagRepository tagRepo;
    @Mock
    private EventRepository eventRepo;
    @Mock
    private ExpenseRepository expenseRepo;
    private TagService sut;

    Tag tag1;

    @BeforeEach
    void TagServiceSetUp() {
        tagRepo = new TestTagRepository();
        eventRepo = new TestEventRepository();
        expenseRepo = new TestExpenseRepository();
        sut = new TagService(tagRepo, eventRepo, expenseRepo);
        tag1 = new Tag("food", "#666666");
    }

    @Test
    void testTagServiceConstructor() {
        assertNotNull(sut);
    }

    @Test
    void testGetAll() {
        tagRepo.save(tag1);
        assertEquals(List.of(tag1), sut.getAll());
    }

    @Test
    void testAdd() {
        sut.add(tag1);
        assertEquals(List.of(tag1), sut.getAll());
    }

    @Test
    void testAddAlreadyThere() {
        tagRepo.save(tag1);
        Tag tag = sut.add(tag1);
        assertEquals(tag, tag1);
    }

    @Test
    void testAddInvalid() {
        Tag tag2 = new Tag(null, "");
        assertNull(sut.add(tag2));
    }

    @Test
    void testUpdate() {
        tag1 = tagRepo.save(tag1);
        tag1.setType("drinks");
        assertEquals(tag1, sut.update(tag1));
    }

    @Test
    void testUpdateInvalid() {
        tag1 = tagRepo.save(tag1);
        tag1.setType(null);
        assertNull(sut.update(tag1));
    }

    @Test
    void testDelete() {
        Expense expense = new Expense();
        Event event = new Event();
        event.addExpense(expense);
        expenseRepo.save(new Expense());
        eventRepo.save(event);
        tag1 = tagRepo.save(tag1);
        assertEquals(tag1, sut.delete(tag1.getId()));
        assertEquals(new ArrayList<>(), tagRepo.findAll());
    }

    @Test
    void testDeleteNotPresent() {
        assertNull(sut.delete(5));
    }
}
