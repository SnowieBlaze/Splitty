package server.Repositories;

import commons.Expense;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A test implementation of the EventRepository interface.
 */
public class TestExpenseRepository implements ExpenseRepository {


    private List<Expense> expenses = new ArrayList<>();

    /**
     * Returns the list of expenses.
     *
     * @return The list of expenses.
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Sets the list of expenses.
     *
     * @param expenses The list of expenses to set.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     *
     */
    @Override
    public void flush() {

    }

    /**
     * @param entity entity to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<Expense> entities) {

    }

    /**
     * @param longs the ids of the entities to be deleted. Must not be {@literal null}.
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
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Expense getOne(Long aLong) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Expense getById(Long aLong) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Expense getReferenceById(Long aLong) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort the results by,
     *               may be {@link Sort#unsorted()}, must not be
     *                {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable the pageable to request a paged result,
     *                 can be {@link Pageable#unpaged()}, must not be
     *                 {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check.
     *                Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends Expense, R> R findBy(Example<S> example,
                                           Function<FluentQuery.FetchableFluentQuery<S>,
                                                   R> queryFunction) {
        return null;
    }

    /**
     * @param entity must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> S save(S entity) {
        expenses.add(entity);
        return entity;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends Expense> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public Optional<Expense> findById(Long aLong) {
        List<Expense> res =  expenses.stream().filter(x -> x.getId() == aLong).toList();
        if (res.isEmpty())  {
            return Optional.empty();
        }
        return Optional.of(res.getFirst());
    }

    /**
     * @param aLong must not be {@literal null}.
     * @return
     */
    @Override
    public boolean existsById(Long aLong) {
        Optional<Expense> exp = findById(aLong);
        if(exp.isPresent()){
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Expense> findAll() {
        return expenses;
    }

    /**
     * @param longs must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<Expense> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * @param aLong must not be {@literal null}.
     */
    @Override
    public void deleteById(Long aLong) {
        Optional<Expense> del = findById(aLong);
        if(del.isPresent()){
            expenses.remove(del.get());
        }
    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(Expense entity) {

    }

    /**
     * @param longs must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends Expense> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param sort the {@link Sort} specification to sort the results by,
     *             can be {@link Sort#unsorted()}, must not be
     *             {@literal null}.
     * @return
     */
    @Override
    public List<Expense> findAll(Sort sort) {
        return null;
    }

    /**
     * @param pageable the pageable to request a paged result,
     *                 can be {@link Pageable#unpaged()}, must not be
     *                 {@literal null}.
     * @return
     */
    @Override
    public Page<Expense> findAll(Pageable pageable) {
        return null;
    }
}
