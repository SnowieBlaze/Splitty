package commons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
//import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String inviteCode;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Participant> participants;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Expense> expenses;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Debt> settledDebts;
    private String dateTime;

    /**
     * Default constructor for the class
     */
    public Event() {
        participants = new ArrayList<>();
        expenses = new ArrayList<>();
        settledDebts = new ArrayList<>();
        updateDateTime();
    }

    /**
     * Constructor for the Event class
     * @param title Title used to differentiate the different Events
     * @param inviteCode Unique code used to join an Event
     * @param participants Participants who participate in the Event
     * @param expenses Expenses made during the Event
     */
    public Event(String title, String inviteCode,
                 List<Participant> participants, List<Expense> expenses) {
        this.title = title;
        this.inviteCode = inviteCode;
        this.participants = participants;
        this.expenses = expenses;
        this.settledDebts = new ArrayList<>();
        // Placeholder ID
        this.id = 0;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateTime = dtf.format(now);
    }

    /**
     * Constructor for the Event class
     * @param id id of the event
     * @param title Title used to differentiate the different Events
     * @param inviteCode Unique code used to join an Event
     * @param participants Participants who participate in the Event
     * @param expenses Expenses made during the Event
     * @param dateTime datetime of the event
     */
    public Event(long id, String title, String inviteCode,
                 List<Participant> participants, List<Expense> expenses, String dateTime) {
        this.id = id;
        this.title = title;
        this.inviteCode = inviteCode;
        this.participants = participants;
        this.expenses = expenses;
        this.settledDebts = new ArrayList<>();
        this.dateTime = dateTime;
    }

    /**
     * Method to add an Empty Expense to the Event
     * @param expense - expense to be added
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    /**
     * Method to remove an Expense from the Event
     * @param expense - expense to be removed
     */
    public void removeExpense(Expense expense) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).equals(expense)) {
                expenses.remove(i);
            }
        }
    }

    /**
     * Method to find the Last Activity added to the Event
     * @return the last Activity added to the Event
     */
    @JsonIgnore
    public Expense getLastActivity() {
        if (expenses.isEmpty()){
            return null;
        }
        else {
            int lastIndex = expenses.size() - 1;
            return expenses.get(lastIndex);
        }
    }

    /**
     * Method to find the actual time of the last Activity added to the Event
     * @return the time of the last Activity added to the Event
     */
    @JsonIgnore
    public String getLastActivityTime() {
        Expense lastExpense = getLastActivity();
        if(lastExpense != null){
            return lastExpense.getDateTime();
        }
        return "No activity yet";
    }

    /**
     * Method to calculate the total expenses of the Event
     * @return total sum of Expenses
     */
    @JsonIgnore
    public double getTotal() {
        double totalExpenses = 0;
        for (int i = 0; i < expenses.size(); i++) {
            totalExpenses = totalExpenses + expenses.get(i).getAmount();
        }
        return totalExpenses;
    }

    /**
     * Method to add a Participant to the Event
     * @param participant - participant to be added
     */
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    // Might be better to assign a number to every participant
    // when they get added, so it's easier to remove
    // the correct participant.

    /**
     * Method to remove a Participant from the Event
     * @param participant - participant to be removed
     */
    public void removeParticipant(Participant participant) {
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).equals(participant)) {
                participants.remove(i);
            }
        }
    }

    /**
     * Method to manually update the time when the Event was last updated
     * @return the Date and Time in string format
     */
    public String updateDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        dateTime = dtf.format(now);
        return dateTime;
    }

    /**
     * Method that creates a random 8-character invite code from the characters in 'characters'
     * it then sets the inviteCode for the event to be the randomly generated invite code
     * @return the randomly generated invite code
     */
    public String createInviteCode(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++){
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }
        this.setInviteCode(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * Getter for the Title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the Title
     * @param title - title to be set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the Invite Code
     * @return inviteCode
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Setter for the Invite Code
     * @param inviteCode - Invite Code to be set
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }


    /**
     * Getter for the Participants
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Setter for the Participants
     * @param participants - participants to be set
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * Getter for the Expenses
     * @return expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Setter for the Expenses
     * @param expenses - expenses to be set
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Getter for settleDebts
     * @return - the settled debts
     */
    public List<Debt> getSettledDebts() {
        return settledDebts;
    }

    /**
     * Setter for settledDebts
     * @param settledDebts - the settledDebts
     */
    public void setSettledDebts(List<Debt> settledDebts) {
        this.settledDebts = settledDebts;
    }

    /**
     * Adds a new settled debt
     * @param debt - the debt to be added
     */
    public void addSettledDebt(Debt debt) {
        this.settledDebts.add(debt);
    }

    /**
     * Getter for the dateTime
     * @return dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Setter for the dateTime
     * @param dateTime - dateTime to be set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Getter for the ID
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Equals method for Event
     * @param o - object to check equality with
     * @return The boolean equality of this with Object o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event event)) {
            return false;
        }
        return getId() == event.getId() && Objects.equals(getTitle(), event.getTitle()) &&
                Objects.equals(getInviteCode(), event.getInviteCode()) &&
                Objects.equals(getParticipants(), event.getParticipants()) &&
                Objects.equals(getExpenses(), event.getExpenses()) &&
                Objects.equals(getDateTime(), event.getDateTime());
    }



    /**
     * HashCode method for Event
     * @return the hashCode of this
     */
    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getInviteCode(), /*getCreator(),*/ getParticipants(),
                getExpenses(), getDateTime(), getId());
    }
}
