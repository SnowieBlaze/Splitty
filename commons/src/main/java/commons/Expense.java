package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    @ManyToOne//(cascade = CascadeType.PERSIST) //keep
    private Participant payingParticipant;
    private double amount;
    private String currency;
    @ManyToMany//(cascade=CascadeType.ALL) //keep
    private List<Participant> participants;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Debt> debts;
    private String dateTime;
    @ManyToOne
    private Tag tag;

    /**
     * Empty new Expense
     */
    @SuppressWarnings("unused")
    public Expense(){
        this.title = "";
        //this.payingParticipant = payingParticipant;
        this.amount = 0.0d;
        this.currency = "EUR";
        this.participants = new ArrayList<>();
        this.debts = new ArrayList<>();
        this.dateTime = "";
    }


    /**
     * New Expense, Participant only
     * @param payingParticipant payer
     */
    @SuppressWarnings("unused")
    public Expense(Participant payingParticipant){
        this.title = "";
        this.payingParticipant = payingParticipant;
        this.amount = 0.0d;
        this.currency = "EUR";
        this.participants = new ArrayList<>();
        this.debts = new ArrayList<>();
        this.dateTime = "";
    }

    /**
     * Default constructor, no set Date
     * @param title of Expense
     * @param payingParticipant of Expense
     * @param amount of Expense
     * @param participants of Expense
     */
    public Expense(String title, Participant payingParticipant,
                   double amount, List<Participant> participants) {
        this.title = title;
        this.payingParticipant = payingParticipant;
        this.amount = amount;
        this.currency = "EUR";
        this.participants = participants;
        this.debts = new ArrayList<>();
        this.dateTime = "";
    }

    /**
     * Default constructor with Date
     * @param title of Expense
     * @param payingParticipant of Expense
     * @param amount of Expense
     * @param currency of Expense
     * @param participants of Expense
     * @param dateTime of Expense
     */
    public Expense(String title, Participant payingParticipant, double amount,
                   String currency, List<Participant> participants, String dateTime) {
        this.title = title;
        this.payingParticipant = payingParticipant;
        this.amount = amount;
        this.currency = currency;
        this.participants = participants;
        this.debts = new ArrayList<>();
        this.dateTime = dateTime;
    }

    /**
     * Get id
     * @return id of expense
     */
    public long getId() {
        return id;
    }

    /**
     * Set id
     * @param id of expense
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * @return Expense title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Update Expense title
     * @param title new title of Expense
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return paying Participant
     */
    public Participant getPayingParticipant() {
        return payingParticipant;
    }

    /**
     * Update paying Participant
     * @param payingParticipant new payer of Expense
     */
    public void setPayingParticipant(Participant payingParticipant) {
        this.payingParticipant = payingParticipant;
    }

    /**
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Update amount
     * @param amount new amount of Expense
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return Expense currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Update Expense currency
     * @param currency new currency of Expense
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Update participants
     * @param participants new list of participants of Expense
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * Adding a new Participant to list
     * @param participant new Participant added to expense
     */
    public void addParticipant(Participant participant){
        participants.add(participant);
    }

    /**
     * Removing a new Participant to list
     * @param participant Participant removed from expense
     */
    public void delParticipant(Participant participant){
        participants.remove(participant);
    }

    /**
     * Adds a debt to the expense
     * @param debt - the debts to be added
     */
    public void add(Debt debt) {
        this.debts.add(debt);
    }

    /**
     * Returns the debts associated with the Expense
     * @return - the debts of the expense
     */
    public List<Debt> getDebts() {
        return debts;
    }

    /**
     * Set the debts of the expense
     * @param debts - the debts to be set
     */
    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    /**
     * @return date
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Update date
     * @param dateTime new date of Expense
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Getter for the tag
     * @return the tag
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Setter for the tag
     * @param tag - the tag to be set
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * @return activity
     */
    @JsonIgnore
    public String[] getActivity() {
        String[] activity = new String[4];
        activity[0] = this.payingParticipant.getName();
        activity[1] = String.valueOf(this.getAmount());
        activity[2] = this.getCurrency();
        activity[3] = this.getTitle();
        return activity;
    }

    /**
     * Equals method
     * @param o object we check equality with
     * @return True if they are the same, otherwise False
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expense expense = (Expense) o;
        return id == expense.id && Double.compare(amount, expense.amount) == 0 &&
            Objects.equals(title, expense.title) &&
            Objects.equals(payingParticipant, expense.payingParticipant) &&
            Objects.equals(currency, expense.currency) &&
            Objects.equals(participants, expense.participants) &&
            Objects.equals(debts, expense.debts) &&
            Objects.equals(dateTime, expense.dateTime) &&
            Objects.equals(tag, expense.tag);
    }

    /**
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, payingParticipant, amount, currency, participants, debts,
            dateTime, tag);
    }
}
