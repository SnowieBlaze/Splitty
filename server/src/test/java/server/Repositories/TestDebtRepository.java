package server.Repositories;

import commons.Debt;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.DebtRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestDebtRepository implements DebtRepository {

    private  List<Debt> debts = new ArrayList<>();
    private final List<String> calledMethods = new ArrayList<>();

    /**
     * Add a method to the list of called methods
     * @param name
     */
    private void call(String name) {
        calledMethods.add(name);
    }

    /**
     * Get the list of debts
     * @return -
     */
    public List<Debt> getDebts() {
        return debts;
    }

    /**
     * Set the list of debts
     * @param debts -
     */
    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    /**
     * Get the list of called methods
     * @return -
     */
    public List<String> getCalledMethods() {
        return calledMethods;
    }

    /**
     * Not implemented
     */
    @Override
    public void flush() {

    }
    /**
     * Not implemented
     * @param entity
     * @return
     */
    @Override
    public <S extends Debt> S saveAndFlush(S entity) {
        return null;
    }
    
    /**
     * Not implemented
     * @param entities
     * @return
     */
    @Override
    public <S extends Debt> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    /**
     * Not implemented
     * @param entities
     */
    @Override
    public void deleteAllInBatch(Iterable<Debt> entities) {

    }

    /**
     * Not implemented
     * @param longs
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    /**
     * Not implemented
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * Not implemented
     * @param aLong
     * @return
     */
    @Override
    public Debt getOne(Long aLong) {
        return null;
    }

    /**
     * Get by id
     * @param aLong
     * @return
     */
    @Override
    public Debt getById(Long aLong) {
        calledMethods.add("getById");
        return debts.stream().filter(x -> x.getId() == aLong).toList().getFirst();
    }

    /**
     * Not implemented
     * @param aLong
     * @return
     */
    @Override
    public Debt getReferenceById(Long aLong) {
        return null;
    }

    /**
     * Not implemented
     * @param example
     * @return
     */
    @Override
    public <S extends Debt> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * Not implemented
     * @param example
     * @return
     */
    @Override
    public <S extends Debt> List<S> findAll(Example<S> example) {
        return null;
    }

    /**
     * Not implemented
     * @param example
     * @param sort
     * @return
     */
    @Override
    public <S extends Debt> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    /**
     * Not implemented
     * @param example
     * @param pageable
     * @return
     */
    @Override
    public <S extends Debt> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * Not implemented
     * @param example
     * @return
     */
    @Override
    public <S extends Debt> long count(Example<S> example) {
        return 0;
    }

    /**
     * Not implemented
     * @param example
     * @return
     */
    @Override
    public <S extends Debt> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * Not implemented
     * @param example
     * @param queryFunction
     * @return
     */
    @Override
    public <S extends Debt, R> R findBy(Example<S> example,
                                        Function<FluentQuery
                                            .FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * Save a debt
     * @param entity
     * @return
     */
    @Override
    public <S extends Debt> S save(S entity) {
        calledMethods.add("save");
        debts.add(entity);
        return entity;
    }

    /**
     * Save all debts
     * @param entities
     * @return
     */
    @Override
    public <S extends Debt> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Find by id
     * @param id
     * @return
     */
    @Override
    public Optional<Debt> findById(Long id) {
        calledMethods.add("findById");
        List<Debt> rightDebts =  debts.stream().filter(x -> x.getId() == id).toList();
        if (rightDebts.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rightDebts.getFirst());
    }

    /**
     * Check if a debt exists by id
     * @param aLong
     * @return
     */
    @Override
    public boolean existsById(Long aLong) {
        calledMethods.add("existsById");
        List<Debt> matchingDebts = debts.stream().filter(x -> x.getId() == aLong).toList();
        if(matchingDebts.size() == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Find all debts
     * @return
     */
    @Override
    public List<Debt> findAll() {
        calledMethods.add("findAll");
        return debts;
    }

    /**
     * Find all debts by id
     * @param longs
     * @return
     */
    @Override
    public List<Debt> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     * Count the number of debts
     * @return
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * Delete a debt by id
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        calledMethods.add("deleteById");
        debts = debts.stream().filter(x -> x.getId() != id).toList();
    }

    /**
     * Delete a debt
     * @param entity
     */
    @Override
    public void delete(Debt entity) {

    }

    /**
     * Delete all debts
     * @param longs
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    /**
     * Delete all debts
     * @param entities
     */
    @Override
    public void deleteAll(Iterable<? extends Debt> entities) {

    }

    /**
     * Delete all debts
     */
    @Override
    public void deleteAll() {

    }

    /**
     * Find all debts by sort
     * @param sort
     * @return
     */
    @Override
    public List<Debt> findAll(Sort sort) {
        return null;
    }

    /**
     * Find all debts by page
     * @param pageable
     * @return
     */
    @Override
    public Page<Debt> findAll(Pageable pageable) {
        return null;
    }
}
