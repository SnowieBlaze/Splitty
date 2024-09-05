package server.Repositories;

import commons.Event;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A test implementation of the EventRepository interface.
 */
public class TestEventRepository implements EventRepository {

    private List<Event> events = new ArrayList<>();
    private final List<String> calledMethods = new ArrayList<>();



    /**
     * Adds the name of the called method to the list of called methods.
     *
     * @param name The name of the called method.
     */
    private void call(String name) {
        calledMethods.add(name);
    }

    /**
     * Returns the list of events.
     *
     * @return The list of events.
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Sets the list of events.
     *
     * @param events The list of events to set.
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Returns the list of called methods.
     *
     * @return The list of called methods.
     */
    public List<String> getCalledMethods() {
        return calledMethods;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Event> findByInviteCode(String inviteCode) {
        List<Event> rightEvent = events.stream()
            .filter(x -> x.getInviteCode().equals(inviteCode)).toList();
        if (!rightEvent.isEmpty()) {
            return Optional.of(rightEvent.getFirst());
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        calledMethods.add("flush");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllInBatch(Iterable<Event> entities) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event getOne(Long aLong) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event getById(Long aLong) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event getReferenceById(Long aLong) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> List<S> findAll(Example<S> example) {
        return (List<S>) events;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> long count(Example<S> example) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event, R> R findBy(Example<S> example,
                                         Function<FluentQuery
                                             .FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> S save(S entity) {
        calledMethods.add("save");
        events.remove(entity);
        events.add(entity);
        return entity;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Event> findById(Long aLong) {
        calledMethods.add("findById");
        List<Event> rightEvents =  events.stream().filter(x -> x.getId() == aLong).toList();
        if (rightEvents.isEmpty())  {
            return Optional.empty();
        }
        return Optional.of(rightEvents.getFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long aLong) {
        calledMethods.add("existsById");
        if (events.stream().map(x -> x.getId()).toList().contains(aLong)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findAll() {
        return events;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long aLong) {
        calledMethods.add("deleteById");
        events = events.stream().filter(x -> x.getId() != aLong).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Event entity) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> findAll(Sort sort) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Event> findAll(Pageable pageable) {
        return null;
    }
}
