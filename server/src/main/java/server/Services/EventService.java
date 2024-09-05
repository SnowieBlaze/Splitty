package server.Services;

import commons.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.database.*;

import java.util.List;
import java.util.Optional;


@Service
public class EventService {
    private final EventRepository eventRepo;
    private final ParticipantRepository participantRepo;
    private final DebtRepository debtRepo;
    private final ExpenseRepository expenseRepo;
    private final TagRepository tagRepo;

    /**
     * Constructor for EventService
     * @param eventRepo - the repository for events
     * @param participantRepo - the repository for participants
     * @param debtRepo - the repository for debts
     * @param expenseRepo - the repository for expenses
     * @param tagRepo - the repository for tags
     */
    public EventService(EventRepository eventRepo,ParticipantRepository participantRepo,
                        DebtRepository debtRepo, ExpenseRepository expenseRepo,
                        TagRepository tagRepo ) {
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
        this.expenseRepo = expenseRepo;
        this.debtRepo = debtRepo;
        this.tagRepo = tagRepo;
    }

    /**
     * Get all events
     * @return all events
     */
    public List<Event> findAll() {
        return eventRepo.findAll();
    }

    /**
     * Method to get an event by inviteCode
     * @param inviteCode code to join event
     * @return Either the event or null
     */
    public Optional<Event> findByInviteCode(String inviteCode) {
        return eventRepo.findByInviteCode(inviteCode);
    }

    /**
     * Method to create a new event
     * @param newEvent the event to be created
     * @return the event that was just created
     */

    public Event create(Event newEvent){
        Event savedEvent = eventRepo.save(newEvent);
        return savedEvent;
    }

    /**
     * Method to update an event
     * @param id The id of the event to be updated
     * @param updatedEvent The already updated event object
     * @return an event object which reflects the one that was just persisted
     */
    public Event update(long id, Event updatedEvent){
        Optional<Event> existingEvent = eventRepo.findById(id);
        if (existingEvent.isPresent()){
            existingEvent.get().setTitle(updatedEvent.getTitle());
            existingEvent.get().setInviteCode(updatedEvent.getInviteCode());
            existingEvent.get().setParticipants(updatedEvent.getParticipants());
            existingEvent.get().setExpenses(updatedEvent.getExpenses());
            existingEvent.get().setSettledDebts(updatedEvent.getSettledDebts());
            Event savedEvent = eventRepo.save(existingEvent.get());
            return savedEvent;
        }
        return null;
    }

    /**
     * Method to get an event by id
     * @param id of event
     * @return Either the event or null
     */
    public Optional<Event> findById(long id){
        return eventRepo.findById(id);
    }

    /**
     * Method to delete an event by id
     * @param id of event
     */
    @Transactional
    public void deleteById(long id){
        Optional<Event> optional = eventRepo.findById(id);
        Event event = optional.get();
        List<Participant> participants = event.getParticipants();
        List<Expense> expenses = event.getExpenses();
        List<Debt> debts = event.getSettledDebts();
        event.setSettledDebts(null);
        event.setExpenses(null);
        event.setParticipants(null);
        eventRepo.save(event);
        eventRepo.flush();
        eventRepo.deleteById(id);
        for(Expense expense : expenses){
            List<Debt> debts1 = expense.getDebts();
            expense.setDebts(null);
            expenseRepo.save(expense);
            expenseRepo.deleteById(expense.getId());
            for(Debt debt: debts1){
                debtRepo.deleteById(debt.getId());
            }
        }
        for(Debt debt : debts){
            debtRepo.deleteById(debt.getId());
        }
        for(Participant participant: participants){
            participantRepo.deleteById(participant.getId());
        }
        participantRepo.flush();
        expenseRepo.flush();
        debtRepo.flush();
    }

    /**
     * Method to check if an event exists by id
     * @param id of event
     * @return boolean if event exists or not
     */
    public boolean existsById(long id){
        return eventRepo.existsById(id);
    }

    /**
     * Method to flush the event repository
     */
    public void flush(){
        eventRepo.flush();
    }


    /**
     * Method that is called when the admin wants to import an event
     * It gets an event as a parameter
     * and imports it into the database
     * @param e event to import
     * @return the imported/created event
     */
    @Transactional
    public Event importEvent(Event e) {
        Event newEvent = new Event();
        newEvent.setTitle(e.getTitle());
        newEvent.setInviteCode(e.getInviteCode());
        newEvent.setDateTime(e.getDateTime());
        newEvent = eventRepo.save(newEvent);
        for(Participant participant : e.getParticipants()){
            if(participantRepo.findById(participant.getId()).isPresent()){
                throw new DataIntegrityViolationException("Participant with id "
                        + participant.getId() + " already exists event cannot be imported" );
            }
            Long oldid = participant.getId();
            newEvent.addParticipant(participant);
            eventRepo.save(newEvent);
            participant.setId(newEvent.getParticipants().getLast().getId());
            changePId(e, participant.getId(), oldid);
        }
        for(Expense expense : e.getExpenses()){
            if(expense.getTag() != null) {
                expense.setTag(tagRepo.save(expense.getTag()));
            }
            expense = expenseRepo.save(expense);
        }
        newEvent.setExpenses(e.getExpenses());
        newEvent.setSettledDebts(e.getSettledDebts());
        eventRepo.save(newEvent);
        return newEvent;
    }

    private void changePId(Event e, Long id, Long oldid) {
        for(Expense expense : e.getExpenses()){
            changeDebts(expense.getDebts(), id, oldid);
            if(expense.getPayingParticipant().getId() == oldid){
                expense.getPayingParticipant().setId(id);
            }
            for(Participant participant : expense.getParticipants()){
                if(participant.getId() == oldid){
                    participant.setId(id);
                }
            }
        }
        changeDebts(e.getSettledDebts(), id, oldid);
    }

    private void changeDebts(List<Debt> debts, Long id, Long oldid){
        for(Debt debt : debts){
            if(debt.getPersonOwing().getId() == oldid){
                debt.getPersonOwing().setId(id);
            }
            if( debt.getPersonPaying().getId() == oldid){
                debt.getPersonPaying().setId(id);
            }
        }
    }
}
