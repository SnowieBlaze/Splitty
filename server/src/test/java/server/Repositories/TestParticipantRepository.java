package server.Repositories;

import commons.Participant;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestParticipantRepository implements ParticipantRepository {

    private List<Participant> participants = new ArrayList<>();
    private List<String> calledMethods = new ArrayList<>();

    /**
     * Adds the name of the called method to the list of called methods.
     *
     * @param name The name of the called method.
     */
    private void call(String name) {
        calledMethods.add(name);
    }

    /**
     * Returns the list of participants.
     *
     * @return The list of participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Sets the list of participants.
     *
     * @param participants The list of participants to set.
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
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
--
     * @param calledMethods -
     */
    public void setCalledMethods(List<String> calledMethods) {
        this.calledMethods = calledMethods;
    }

    /**
     *
     */
    @Override
    public void flush() {
        calledMethods.add("flush");
    }

    /**
     * @param entity -
     * @param <S> -
     * @return -
     */
    @Override
    public <S extends Participant> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities -
     * @param <S> -
     * @return -
     */
    @Override
    public <S extends Participant> java.util.List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities -
     */
    @Override
    public void deleteAllInBatch(Iterable<Participant> entities) {

    }

    /**
     * @param longs -
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     *
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * @param aLong -
     * @return -
     */
    @Override
    public Participant getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong -
     * @return -
     */
    @Override
    public Participant getById(Long aLong) {
        return null;
    }

    /**
     * @param aLong -
     * @return -
     */
    @Override
    public Participant getReferenceById(Long aLong) {
        return null;
    }

    /**
     * @param example -
     * @param <S> -
     * @return -
     */
    @Override
    public <S extends Participant> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example -
     * @param <S> -
     * @return -
     */
    @Override
    public <S extends Participant> java.util.List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * @param example -
     * @param sort -
     * @param <S> -
     * @return -
     */
    @Override
    public <S extends Participant> java.util.List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * @param example -
     * @param pageable -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Participant> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example -
     * @param queryFunction -
     * @param <S> -
     * @param <R> -
     * @return -
     */
    @Override
    public <S extends Participant, R> R findBy(Example<S> example,
                                               Function<FluentQuery.FetchableFluentQuery<S>,
                                                   R> queryFunction) {
        return null;
    }

    /**
     * @param entity -
     * @param <S> -
     * @return -
     */
    @Override
    public <S extends Participant> S save(S entity) {
        calledMethods.add("save");
        participants.add(entity);
        return entity;
    }

    /**
     * @param entities -
     * @param <S> -
     * @return -
     */
    @Override
    public <S extends Participant> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     *
     * @param aLong -
     * @return -
     */
    @Override
    public Optional<Participant> findById(Long aLong) {
        calledMethods.add("findById");
        List<Participant> rightParticipants =  participants.stream()
            .filter(x -> x.getId() == aLong).toList();
        if (rightParticipants.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rightParticipants.getFirst());
    }

    /**
     *
     * @param aLong -
     * @return -
     */
    @Override
    public boolean existsById(Long aLong) {
        calledMethods.add("existsById");
        List<Participant> matchingParticipants = participants.stream()
            .filter(x -> x.getId() == aLong).toList();
        if(matchingParticipants.size() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @return -
     */
    @Override
    public List<Participant> findAll() {
        calledMethods.add("findAll");
        return participants;
    }

    /**
     *
     * @param longs -
     * @return -
     */
    @Override
    public List<Participant> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * @return -
     */
    @Override
    public long count() {
        return 0;
    }


    /**
     * @param aLong -
     */
    @Override
    public void deleteById(Long aLong) {
        calledMethods.add("deleteById");
        participants = participants.stream().filter(x -> x.getId() != aLong).toList();
    }

    /**
     * @param entity -
     */
    @Override
    public void delete(Participant entity) {

    }

    /**
     * @param longs -
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * @param entities -
     */
    @Override
    public void deleteAll(Iterable<? extends Participant> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param sort -
     * @return -
     */
    @Override
    public List<Participant> findAll(Sort sort) {
        return null;
    }

    /**
     *
     * @param pageable -
     * @return -
     */
    @Override
    public Page<Participant> findAll(Pageable pageable) {
        return null;
    }
}
