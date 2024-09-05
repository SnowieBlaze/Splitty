package server.Services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.DebtRepository;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepo;
    private final DebtRepository debtRepo;
    private final ExpenseRepository expenseRepo;
    private final EventRepository eventRepo;

    /**
     * Constructor for ParticipantService
     * @param participantRepo - the repository for participants
     * @param debtRepo - the repository for debts
     * @param expenseRepo - the repository for expenses
     * @param eventRepo - the repository for events
     */
    public ParticipantService(ParticipantRepository participantRepo,
                              DebtRepository debtRepo, ExpenseRepository expenseRepo,
                              EventRepository eventRepo) {
        this.participantRepo = participantRepo;
        this.debtRepo = debtRepo;
        this.expenseRepo = expenseRepo;
        this.eventRepo = eventRepo;
    }

    /**
     * Get all participants
     * @return all participants
     */
    public List<Participant> getAll() {
        return participantRepo.findAll();
    }

    /**
     * Updates a participants in the database iff it exists
     * @param id The id of the participant to be updated
     * @param updatedParticipant The already updated participant object
     * @return A participant object which reflects the one that was just persisted
     */
    public Participant update(long id, Participant updatedParticipant){
        Optional<Participant> existingParticipant = participantRepo.findById(id);
        if (existingParticipant.isPresent()){
            existingParticipant.get().setName(updatedParticipant.getName());
            existingParticipant.get().setEmail(updatedParticipant.getEmail());
            existingParticipant.get().setBic(updatedParticipant.getBic());
            existingParticipant.get().setIban(updatedParticipant.getIban());
            Participant savedParticipant = participantRepo.save(existingParticipant.get());
            return savedParticipant;
        }
        return null;
    }

    /**
     * Creates a new participant in the database
     * @param newParticipant The participant to be created
     * @return A participant object which reflects the one that was just created
     */
    public Participant create(Participant newParticipant){
        Participant savedParticipant = participantRepo.save(newParticipant);
        return savedParticipant;
    }

    /**
     * Deletes a participant if it exists in the database
     * @param participantId the id of the participant to be deleted
     * @return boolean if participant was deleted or not
     */
    public boolean delete(long participantId){
        if (participantRepo.existsById(participantId)){
            Participant participant = participantRepo.findById(participantId).get();
            Event event = eventRepo.findAll().stream()
                .filter(x -> x.getParticipants().contains(participant)).toList().getFirst();
            List<Expense> expenses = new ArrayList<>(event.getExpenses());
            List<Debt> debtsToDelete = new ArrayList<>(event.getSettledDebts());
            for(Expense expense : expenses) {
                debtsToDelete.addAll(expense.getDebts());
            }
            debtsToDelete = debtsToDelete.stream()
                .filter(x -> x.getPersonPaying().equals(participant) ||
                    x.getPersonOwing().equals(participant)).toList();

            for (Expense expense : expenses) {
                expense.getDebts().removeAll(debtsToDelete);
                expense.getParticipants().remove(participant);
                if (expense.getPayingParticipant().equals(participant)) {
                    event.removeExpense(expense);
                    event = eventRepo.save(event);
                    expenseRepo.delete(expense);
                }
                else {
                    expenseRepo.save(expense);
                }
            }
            for(Debt debt : debtsToDelete) {
                debtRepo.deleteById(debt.getId());
            }
            event.getSettledDebts().removeAll(debtsToDelete);
            event.getParticipants().remove(participant);
            participantRepo.deleteById(participantId);
            eventRepo.save(event);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns optional Participant by its id
     * @param participantId id of the participant to be fetched
     * @return An optional Participant, which will be present if found in the database
     */
    public Optional<Participant> getById(long participantId){
        Optional<Participant> participantOptional = participantRepo.findById(participantId);
        return participantOptional;
    }

    /**
     * Method to flush the participant repository
     */
    public void flush() {
        participantRepo.flush();
    }
}
