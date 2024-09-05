package server.Repositories;

import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTagRepository implements TagRepository {

    private List<Tag> tags = new ArrayList<>();
    private List<String> calledMethods = new ArrayList<>();

    /**
     * Add a method to the list of called methods
     * @param name
     */
    private void call(String name) {
        calledMethods.add(name);
    }

    /**
     * Get the list of tags
     * @return -
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Set the list of debts
     * @param tags -
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Get the list of called methods
     * @return -
     */
    public List<String> getCalledMethods() {
        return calledMethods;
    }

    /**
     *
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

    }

    /**
     *
     * @param entity -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> S saveAndFlush(S entity) {
        return null;
    }

    /**
     *
     * @param entities -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     *
     * @param entities -
     */
    @Override
    public void deleteAllInBatch(Iterable<Tag> entities) {

    }

    /**
     *
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
     *
     * @param aLong -
     * @return -
     */
    @Override
    public Tag getOne(Long aLong) {
        return null;
    }

    /**
     *
     * @param aLong -
     * @return -
     */
    @Override
    public Tag getById(Long aLong) {
        return null;
    }

    /**
     *
     * @param aLong -
     * @return -
     */
    @Override
    public Tag getReferenceById(Long aLong) {
        return null;
    }

    /**
     *
     * @param example -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     *
     * @param example -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     *
     * @param example -
     * @param sort -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     *
     * @param example -
     * @param pageable -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     *
     * @param example -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> long count(Example<S> example) {
        return 0;
    }

    /**
     *
     * @param example -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> boolean exists(Example<S> example) {
        return false;
    }

    /**
     *
     * @param example -
     * @param queryFunction -
     * @return -
     * @param <S> -
     * @param <R> -
     */
    @Override
    public <S extends Tag, R> R findBy(Example<S> example,
                                       Function<FluentQuery.FetchableFluentQuery<S>,
                                           R> queryFunction) {
        return null;
    }

    /**
     *
     * @param entity -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> S save(S entity) {
        calledMethods.add("save");
        tags.remove(entity);
        tags.add(entity);
        return entity;
    }

    /**
     *
     * @param entities -
     * @return -
     * @param <S> -
     */
    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     *
     * @param aLong -
     * @return -
     */
    @Override
    public Optional<Tag> findById(Long aLong) {
        calledMethods.add("findById");
        List<Tag> rightTags =  tags.stream().filter(x -> x.getId() == aLong).toList();
        if (rightTags.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rightTags.getFirst());
    }

    /**
     *
     * @param aLong -
     * @return -
     */
    @Override
    public boolean existsById(Long aLong) {
        calledMethods.add("existsById");
        List<Tag> matchingTags = tags.stream().filter(x -> x.getId() == aLong).toList();
        if(matchingTags.size() >= 1) {
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
    public List<Tag> findAll() {
        calledMethods.add("findAll");
        return tags;
    }

    /**
     *
     * @param longs -
     * @return -
     */
    @Override
    public List<Tag> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     *
     * @return -
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     *
     * @param aLong -
     */
    @Override
    public void deleteById(Long aLong) {
        calledMethods.add("deleteById");
        tags = tags.stream().filter(x -> x.getId() != aLong).toList();
    }

    /**
     *
     * @param entity -
     */
    @Override
    public void delete(Tag entity) {

    }

    /**
     *
     * @param longs -
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     *
     * @param entities -
     */
    @Override
    public void deleteAll(Iterable<? extends Tag> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
     *
     * @param sort -
     * @return -
     */
    @Override
    public List<Tag> findAll(Sort sort) {
        return null;
    }

    /**
     *
     * @param pageable -
     * @return -
     */
    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return null;
    }
}
