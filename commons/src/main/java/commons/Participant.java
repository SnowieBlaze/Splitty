package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    private String email;
    private String bic;
    private String iban;

    /**
     * A constructor for creating Participant object by a name
     *
     * @param name - the name of the Participant
     */
    public Participant(String name) {
        this.name = name;
    }

    /**
     * A constructor for creating Participant object by id and name
     *
     * @param id   - the id of the Participant
     * @param name - the name of the Participant
     * @param email - the email of the Participant
     * @param iban -  the IBAN of the Participant
     * @param bic - the BIC of the Participant
     */
    public Participant(long id, String name, String email, String iban, String bic) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
    }

    /**
     *  Empty constructor
     */
    public Participant() {
        //empty
    }

    /**
     *  Constructor with id and name
     * @param id
     * @param name
     */
    public Participant(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Getter for the id of Participant
     *
     * @return the id of Participant
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for name of the Participant
     *
     * @return the name of the Participant
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for email of the Participant
     *
     * @return the email of the Participant
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for BIC of the Participant
     *
     * @return the BIC of the Participant
     */
    public String getBic() {
        return bic;
    }

    /**
     * Getter for IBAN of the Participant
     *
     * @return the IBAN of the Participant
     */
    public String getIban() {
        return iban;
    }

    /**
     * Setter for the id of the Participant
     *
     * @param id - the id to be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Setter for the name of the Participant
     *
     * @param name - the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Setter for the email of the participant
     * @param email - the email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter for the BIC of the participant
     * @param bic - the BIC to be set
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * Setter for the IBAN of the participant
     * @param iban - the IBAN to be set
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * Equals method for Participant
     *
     * @param o - the object we compare with
     * @return true if the objects are equal,
     * false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Participant that = (Participant) o;
        return id == that.id && Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(bic, that.bic) && Objects.equals(iban, that.iban);
    }

    /**
     * HashCode method for participant
     *
     * @return the hashCode of the Participant
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, iban, bic);
    }
}
